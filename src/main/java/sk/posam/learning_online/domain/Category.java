package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import sk.posam.learning_online.domain.views.views;

import java.util.HashSet;
import java.util.Set;

@Entity
@JsonView(views.Public.class)
public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @Column(name="category_id")
    private Long id;
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "categories",fetch = FetchType.EAGER)
    private Set<Course> courses = new HashSet<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void addCourseToCategory(Course course) {
        this.courses.add(course);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courses=" + courses +
                '}';
    }
}
