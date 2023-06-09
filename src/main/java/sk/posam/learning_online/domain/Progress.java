package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import sk.posam.learning_online.domain.views.views;

@Entity
@JsonView(views.Public.class)
public class Progress {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "progress_id")
    private long id;

    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private double ratio = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getCourse() {
        return course.getId();
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
