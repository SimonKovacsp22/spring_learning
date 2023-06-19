package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import sk.posam.learning_online.domain.enumeration.LanguageName;
import sk.posam.learning_online.domain.views.views;

import java.util.HashSet;
import java.util.Set;


@Entity
@JsonView(views.Public.class)
public class Language {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "language_id")
    private long id;
    @Enumerated(EnumType.STRING)
    private LanguageName name;

    @JsonIgnore
    @ManyToMany(mappedBy = "languages",fetch = FetchType.EAGER)
    private Set<Course> courses = new HashSet<>();

    public Language(LanguageName name) {
        this.name = name;
    }

    public Language() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LanguageName getName() {
        return name;
    }

    public void setName(LanguageName name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", name=" + name +
                "}";
    }
}
