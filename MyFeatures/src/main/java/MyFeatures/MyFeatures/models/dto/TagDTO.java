package MyFeatures.MyFeatures.models.dto;

public class TagDTO {
    private Long tagId;
    private String name;

    public TagDTO(Long tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    public TagDTO() {
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}