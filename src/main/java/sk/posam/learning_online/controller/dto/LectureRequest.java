package sk.posam.learning_online.controller.dto;

public class LectureRequest {
    Long lectureId;
    String title;
    Integer durationInSeconds;
    Integer rank;
    Long sectionId;

    public Long getLectureId() {
        return lectureId;
    }

    public String getTitle() {
        return title;
    }

    public Integer getDurationInSeconds() {
        return durationInSeconds;
    }

    public Integer getRank() {
        return rank;
    }

    public Long getSectionId() {
        return sectionId;
    }
}
