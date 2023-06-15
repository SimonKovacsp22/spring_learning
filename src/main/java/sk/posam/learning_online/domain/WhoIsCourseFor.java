package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import sk.posam.learning_online.domain.views.views;

@Entity
@JsonView(views.Public.class)
public class WhoIsCourseFor {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "wicf_id")
    private Long id;

    private String targetedAudience;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(
            name = "course_id",
            nullable = false,
            referencedColumnName = "course_id",
            foreignKey = @ForeignKey(
                    name = "wicf_course_fk"
            )
    )
    private Course course;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetedAudience() {
        return targetedAudience;
    }

    public void setTargetedAudience(String targetedAudience) {
        this.targetedAudience = targetedAudience;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
