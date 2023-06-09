package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import sk.posam.learning_online.domain.views.views;

@Entity
public class Video {
    @JsonView(views.Public.class)
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "video_id")
    private long id;
    @JsonView(views.Public.class)
    private String title;
    @JsonView(views.Public.class)
    private String duration;
    @JsonView(views.Public.class)
    @Column(name = "duration_seconds",
    nullable = true)
    private int durationInSeconds;

    @JsonView(views.VideosWithUrl.class)
    private String sourceUrl;
    @JsonView(views.Public.class)
    private int rank;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }




}
