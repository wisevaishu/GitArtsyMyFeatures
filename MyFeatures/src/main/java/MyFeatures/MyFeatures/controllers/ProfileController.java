package MyFeatures.MyFeatures.controllers;


import MyFeatures.MyFeatures.models.Profile;
import MyFeatures.MyFeatures.models.User;
import MyFeatures.MyFeatures.models.dto.ProfileDto;
import MyFeatures.MyFeatures.repositories.ProfileRepo;
import MyFeatures.MyFeatures.repositories.UserRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("gitartsy/api/profiles")
@CrossOrigin(origins = "http://localhost:8082")
public class ProfileController {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private UserRepository userRepo;

    private final Path fileStorageLocation;

    private static final Logger logger = LoggerFactory.getLogger(ArtworksController.class);

    public ProfileController() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<ProfileDto> createArtistProfile(
            @RequestParam("userId") Long userId,
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("profilePic") MultipartFile file,
            @RequestParam("bioDescription") String bioDescription) {

        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Profile artistProfile = new Profile();

            artistProfile.setUser(user);
            artistProfile.setName(name);
            artistProfile.setLocation(location);
            artistProfile.setEmail(email);
            artistProfile.setPhone(phone);
            artistProfile.setBioDescription(bioDescription);
            artistProfile.setCreatedAt(LocalDateTime.now());
            artistProfile.setUpdatedAt(LocalDateTime.now());

            // Handle file upload
            if (file != null && !file.isEmpty()) {
                String fileName;
                String fileDownloadUri;
                fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                try {
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
                }

                fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/")
                        .path(fileName)
                        .toUriString();

                artistProfile.setFilename(fileName);
                artistProfile.setFileDownloadUri(fileDownloadUri);
                artistProfile.setFileType(file.getContentType());
                artistProfile.setSize(file.getSize());
            }

            try {
                Profile savedArtistProfile = profileRepo.save(artistProfile);
                ProfileDto profileDto = new ProfileDto();
                profileDto.setId(savedArtistProfile.getId());
                profileDto.setSize(savedArtistProfile.getSize());
                profileDto.setFilename(savedArtistProfile.getFilename());
                profileDto.setBioDescription(savedArtistProfile.getBioDescription());
                profileDto.setLocation(savedArtistProfile.getLocation());
                profileDto.setEmail(savedArtistProfile.getEmail());
                profileDto.setPhone(savedArtistProfile.getPhone());
                profileDto.setFileDownloadUri(savedArtistProfile.getFileDownloadUri());
                profileDto.setFileType(savedArtistProfile.getFileType());
                profileDto.setName(savedArtistProfile.getName());

                return ResponseEntity.status(HttpStatus.CREATED).body(profileDto);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Path file = fileStorageLocation.resolve(filename);
        Resource fileResource;

        try {
            fileResource = new UrlResource(file.toUri());
            if (fileResource.exists() || fileResource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                        .body(fileResource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@PathVariable("id") Integer id) {
        Optional<Profile> profileOptional = profileRepo.findById(id);

        ProfileDto profileDto = new ProfileDto();
        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();

            profileDto.setId(profile.getId());
            profileDto.setSize(profile.getSize());
            profileDto.setFilename(profile.getFilename());
            profileDto.setBioDescription(profile.getBioDescription());
            profileDto.setLocation(profile.getLocation());
            profileDto.setEmail(profile.getEmail());
            profileDto.setPhone(profile.getPhone());
            profileDto.setFileDownloadUri(profile.getFileDownloadUri());
            profileDto.setFileType(profile.getFileType());
            profileDto.setName(profile.getName());

            return ResponseEntity.ok(profileDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(profileDto);
        }
    }

    @GetMapping("/exists/{userId}")
    public ResponseEntity<Boolean> doesProfileExist(@PathVariable("userId") Long userId) {
        // Check if the user exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            // Check if a profile exists for the user
            Optional<Profile> profileOptional = profileRepo.findByUser(userOptional.get());
            if (profileOptional.isPresent()) {
                // Profile exists, return true
                return ResponseEntity.ok(true);
            } else {
                // Profile does not exist, return false
                return ResponseEntity.ok(false);
            }
        } else {
            // User not found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @GetMapping("/profileid/{userId}")
    public ResponseEntity<Integer> getProfileIdByUserId(@PathVariable("userId") Long userId) {
        // Check if the user exists
        Optional<User> userOptional = userRepo.findById(userId);
        if (userOptional.isPresent()) {
            // Check if a profile exists for the user
            Optional<Profile> profileOptional = profileRepo.findByUser(userOptional.get());
            if (profileOptional.isPresent()) {
                // Profile exists, return the ProfileId
                return ResponseEntity.ok(profileOptional.get().getId());
            } else {
                // Profile does not exist, return 0
                return ResponseEntity.ok(0);
            }
        } else {
            // User not found, return 0
            return ResponseEntity.ok(0);
        }
    }

}
