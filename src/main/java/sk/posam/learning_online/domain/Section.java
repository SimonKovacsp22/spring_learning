package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import sk.posam.learning_online.domain.views.views;

import java.util.HashSet;
import java.util.Set;
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @JsonView(views.Public.class)
    @Column(name = "section_id")
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @JsonView(views.Public.class)
    private String title;
    @JsonView(views.Public.class)
    private int rank;


    @OneToMany(mappedBy="section",fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonView(views.Public.class)
    private Set<Video> videos = new HashSet<>();



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public void addVideo(Video video) {
        this.videos.add(video);
        video.setSection(this);
    }
}
