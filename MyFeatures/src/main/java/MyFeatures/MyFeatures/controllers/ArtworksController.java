
package MyFeatures.MyFeatures.controllers;

import MyFeatures.MyFeatures.models.Artworks;
import MyFeatures.MyFeatures.models.Profile;
import MyFeatures.MyFeatures.models.Tag;
import MyFeatures.MyFeatures.models.dto.ArtworksDto;
import MyFeatures.MyFeatures.models.dto.ArtworksGetDto;
import MyFeatures.MyFeatures.repositories.ArtworksRepo;
import MyFeatures.MyFeatures.repositories.ProfileRepo;
import MyFeatures.MyFeatures.repositories.TagRepository;
import jakarta.servlet.http.HttpServletRequest;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("gitartsy/api/artworks")
@CrossOrigin(origins = "http://localhost:5173")
public class ArtworksController {

    @Autowired
    private ArtworksRepo artworkRepo;

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private TagRepository tagRepo;


    private final Path fileStorageLocation;

    private static final Logger logger = LoggerFactory.getLogger(ArtworksController.class);

    public ArtworksController() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    //@PostMapping("/new")
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ArtworksDto createArtwork(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") Float price,
            @RequestParam("tagIds") String  tagIdsJson,
            @RequestParam("image") MultipartFile file,
            @RequestParam("profileId") Integer profileId ) {



        // Log request parameters for debugging
        logger.debug("Profile ID: {}", profileId);
        logger.debug("Title: {}", title);
        logger.debug("Description: {}", description);
        logger.debug("Price: {}", price);
        logger.debug("File: {}", (file != null ? file.getOriginalFilename() : "No file"));
        logger.debug("Tag IDs: {}", tagIdsJson);


        // Optionally verify the profileId if needed

        ArtworksDto artworksDto = new ArtworksDto();

        Optional<Profile> profileOptional = profileRepo.findById(profileId);

        if (profileOptional.isPresent()) {
            logger.error("Profile not found with ID: {}", profileId);
            Artworks artwork = new Artworks();
            artwork.setProfile(profileOptional.get());
            artwork.setTitle(title);
            artwork.setDescription(description);
            artwork.setPrice(price);
            artwork.setCreatedAt(LocalDateTime.now());
            artwork.setUpdatedAt(LocalDateTime.now());

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

                artwork.setFilename(fileName);
                artwork.setFileDownloadUri(fileDownloadUri);
                artwork.setFileType(file.getContentType());
                artwork.setSize(file.getSize());
            }

            // Convert tagIdsJson to List<Long>
            List<Long> tagIds = new ArrayList<>();
            try {
                ObjectMapper mapper = new ObjectMapper();
                tagIds = mapper.readValue(tagIdsJson, new TypeReference<List<Long>>() {});
            } catch (IOException e) {
                logger.error("Failed to parse tagIds JSON", e);
                // Handle error accordingly
            }

            // Add tags to the artwork
            for (Long tagId : tagIds) {
                Optional<Tag> tagOptional = tagRepo.findById(tagId);
                if (tagOptional.isPresent()) {
                    artwork.getTags().add(tagOptional.get());
                } else {
                    logger.warn("Tag ID {} not found", tagId);
                }
            }



            try {
                Artworks savedArtwork = artworkRepo.save(artwork);
                //Map<String, Object> response = new HashMap<>();
                //response.put("message", "Artwork created");
                //response.put("artwork", savedArtwork);
                //return ResponseEntity.status(HttpStatus.CREATED).body(response);

                artworksDto.setDescription(description);
                artworksDto.setImage(file);
                artworksDto.setPrice(price);
                artworksDto.setTitle(title);
                artworksDto.setTagIds(tagIds);

                //return response.toString();
                return artworksDto;
            } catch (Exception e) {
                e.printStackTrace();

                return artworksDto;
            }
        } else {

            return artworksDto;
        }
    }


    @GetMapping("/profile/{profileId}")
    public ResponseEntity<List<ArtworksGetDto>> getArtworksByProfile(@PathVariable Integer profileId) {
        List<Artworks> artworks = artworkRepo.findByProfileId(profileId);
        //List<Artworks> files = artworkRepo.findAll();

        List<ArtworksGetDto> allArtworks = new ArrayList<>();

        for (Artworks oneartwork : artworks)
        {
            ArtworksGetDto artworksGetDto = new ArtworksGetDto();
            artworksGetDto.setFileDownloadUri(oneartwork.getFileDownloadUri());
            artworksGetDto.setFileType(oneartwork.getFileType());
            artworksGetDto.setSize(oneartwork.getSize());

            // Set new fields

            artworksGetDto.setTitle(oneartwork.getTitle());
            artworksGetDto.setDescription(oneartwork.getDescription());
            artworksGetDto.setPrice(oneartwork.getPrice());
            artworksGetDto.setTags(oneartwork.getTags());
            allArtworks.add(artworksGetDto);
        }

        return ResponseEntity.ok(allArtworks);

    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Path filePath = fileStorageLocation.resolve(fileName).normalize();
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found", e);
        }

        String contentType = null;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
