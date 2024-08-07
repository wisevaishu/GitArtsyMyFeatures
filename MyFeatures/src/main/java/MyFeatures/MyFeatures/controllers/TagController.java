
package MyFeatures.MyFeatures.controllers;

import MyFeatures.MyFeatures.models.Tag;
import MyFeatures.MyFeatures.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

//Controller for managing Tags.
@RestController
@RequestMapping("gitartsy/api/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    // List to store tags in memory (replace with database logic in a real application)
    private List<Tag> tags = new ArrayList<>();

    //Retrieves all tags.
    @GetMapping
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    // Retrieves a tag by its ID.
    @GetMapping(".{id}")
    public Tag getTagById(Long id) {
        for (Tag tag : tags) {
            if (tag.getTagId().equals(id)) {
                return tag;
            }
        }
        return null;
    }

    //Creates a new tag.
    @PostMapping
    public Tag createTag(Tag tag) {
        tags.add(tag);
        return tag;
    }


    //Updates an existing tag.
    @PutMapping("/{id}")
    public Tag updateTag(Long id, Tag newTag) {
        for (Tag tag : tags) {
            if (tag.getTagId().equals(id)) {
                tag.setName(newTag.getName());
                return tag;
            }
        }
        return null;
    }

    // Deletes a tag by its ID.
    @DeleteMapping("/{id}")
    boolean deleteTag(Long id) {

        return tags.removeIf(tag -> tag.getTagId().equals(id));
    }
}