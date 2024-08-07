package MyFeatures.MyFeatures.controllers;


import MyFeatures.MyFeatures.models.Profile;
import MyFeatures.MyFeatures.models.User;
import MyFeatures.MyFeatures.repositories.ProfileRepo;
import MyFeatures.MyFeatures.repositories.UserRepository;
import org.launchcode.git_artsy_backend.models.dto.ProfileDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/artist-profiles")
@CrossOrigin(origins = "http://localhost:8082")
public class ProfileController {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/new")
    public ResponseEntity<Profile> createArtistProfile(@RequestBody ProfileDto profileDTO) {
        Optional<User> userOptional = userRepo.findById(profileDTO.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Profile artistProfile = new Profile();

            artistProfile.setUser(user);
            artistProfile.setName(profileDTO.getName());
            artistProfile.setLocation(profileDTO.getLocation());
            artistProfile.setEmail(profileDTO.getEmail());
            artistProfile.setPhone(profileDTO.getPhone());
            artistProfile.setProfilePic(profileDTO.getProfilePic());
            artistProfile.setBioDescription(profileDTO.getBioDescription());
            artistProfile.setCreatedAt(LocalDateTime.now());
            artistProfile.setUpdatedAt(LocalDateTime.now());
            try {
                Profile savedArtistProfile = profileRepo.save(artistProfile);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedArtistProfile);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<Profile>> getAllArtistProfiles() {
        List<Profile> artistProfiles = profileRepo.findAll();
        return ResponseEntity.ok(artistProfiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profile> getArtistProfileById(@PathVariable Integer id) {
        Optional<Profile> optionalArtistProfile = profileRepo.findById(id);
        if (optionalArtistProfile.isPresent()) {
            return ResponseEntity.ok(optionalArtistProfile.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateArtistProfile(@PathVariable Integer id, @RequestParam Long userId, @RequestBody
                                                        ProfileDto profileDTO) {

        Optional<Profile> existingArtistProfile = profileRepo.findById(id);

        if (existingArtistProfile.isPresent()) {
            Optional<User> userOptional = userRepo.findById(userId);
            if (userOptional.isPresent()) {

                Profile artistProfileToUpdate = existingArtistProfile.get();
                artistProfileToUpdate.setUser(userOptional.get());
                artistProfileToUpdate.setName(profileDTO.getName());
                artistProfileToUpdate.setLocation(profileDTO.getLocation());
                artistProfileToUpdate.setEmail(profileDTO.getEmail());
                artistProfileToUpdate.setPhone(profileDTO.getPhone());
                artistProfileToUpdate.setProfilePic(profileDTO.getProfilePic());
                artistProfileToUpdate.setBioDescription(profileDTO.getBioDescription());
                artistProfileToUpdate.setUpdatedAt(LocalDateTime.now());

                try {
                    Profile savedArtistProfile = profileRepo.save(artistProfileToUpdate);
                    return ResponseEntity.ok(savedArtistProfile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtistProfile(@PathVariable Integer id) {
        Optional<Profile> existingArtistProfile = profileRepo.findById(id);
        if (existingArtistProfile.isPresent()) {
            try {
                profileRepo.deleteById(id);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
