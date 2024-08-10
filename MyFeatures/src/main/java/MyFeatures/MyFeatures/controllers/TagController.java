
package MyFeatures.MyFeatures.controllers;


import MyFeatures.MyFeatures.models.Tag;
import MyFeatures.MyFeatures.models.dto.TagDTO;
import MyFeatures.MyFeatures.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Controller for managing Tags.
@RestController
@RequestMapping("gitartsy/api/tags")
@CrossOrigin(origins = "http://localhost:5173")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    // Retrieves all tags.
    @GetMapping
    public List<TagDTO> getAllTags() {
        List<Tag> tags = (List<Tag>) tagRepository.findAll();
        return tags.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Retrieves a tag by its ID.
    @GetMapping("/{id}")
    public TagDTO getTagById(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id).orElse(null);
        return tag != null ? convertToDTO(tag) : null;
    }

    // Creates a new tag.
    @PostMapping
    public TagDTO createTag(@RequestBody TagDTO tagDTO) {
        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        Tag savedTag = tagRepository.save(tag);
        return convertToDTO(savedTag);
    }

    // Updates an existing tag.
    @PutMapping("/{id}")
    public TagDTO updateTag(@PathVariable Long id, @RequestBody TagDTO tagDTO) {
        Optional<Tag> existingTag = tagRepository.findById(id);
        if (existingTag.isPresent()) {
            Tag tag = existingTag.get();
            tag.setName(tagDTO.getName());
            Tag updatedTag = tagRepository.save(tag);
            return convertToDTO(updatedTag);
        }
        return null;
    }

    // Deletes a tag by its ID.
    @DeleteMapping("/{id}")
    public boolean deleteTag(@PathVariable Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Converts Tag entity to TagDTO
    private TagDTO convertToDTO(Tag tag) {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setTagId(tag.getTagId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }
}