package MyFeatures.MyFeatures.controllers;

import MyFeatures.MyFeatures.models.User;
import MyFeatures.MyFeatures.models.dto.LoginDTO;
import MyFeatures.MyFeatures.models.dto.RegisterDTO;
import MyFeatures.MyFeatures.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


//TBC
@RestController
@RequestMapping("gitartsy/api/register")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String userSessionKey = "user";

    @GetMapping
    public String getAllUsers(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "user/list";
    }

    public User getUserFromSession(HttpSession session) {
        Long userId = (Long) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getUser_id());
    }

    @PostMapping("/save")
    public ResponseEntity<?> processRegistrationForm(@RequestBody @Valid RegisterDTO registerDTO,
                                                     Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        User existingUser = userRepository.findByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("A user with that username already exists");
        }

        String password = registerDTO.getPassword();
        String verifyPassword = registerDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }

        User newUser = new User();
        newUser.setUsername(registerDTO.getUsername());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(registerDTO.getRole());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        userRepository.save(newUser);

        setUserInSession(request.getSession(), newUser);

        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> processLoginForm(@RequestBody @Valid LoginDTO loginDTO,
                                              Errors errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }

        User theUser = userRepository.findByUsername(loginDTO.getUsername());
        if (theUser == null) {
            return ResponseEntity.badRequest().body("The given username does not exist");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), theUser.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        setUserInSession(request.getSession(), theUser);



        Map<String, Object> response = new HashMap<>();
        response.put("username", theUser.getUsername());
        response.put("role", theUser.getRole());
        response.put("userid", theUser.getUser_id());

        return ResponseEntity.ok(response);
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}
