package sk.posam.learning_online.controller.dto;

public class SectionRequest {
    Long sectionId;
    String title;

    Integer rank;

    public Long getSectionId() {
        return sectionId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getRank() {
        return rank;
    }
}
