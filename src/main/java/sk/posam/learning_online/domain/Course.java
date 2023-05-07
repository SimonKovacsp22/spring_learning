package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Course {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "course_id")
    private long id;

    @Column String title;

    public Course() {
    }

    @Column String description;

    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false,
            referencedColumnName = "user_id",
            foreignKey = @ForeignKey(
                    name = "user_course_fk"
            )
    )
    private User user;
    @JsonIgnore
    @ManyToMany( cascade = CascadeType.ALL,
                    fetch = FetchType.EAGER)
    @JoinTable(name="course_category",
    joinColumns = @JoinColumn(name = "course_id"),
    inverseJoinColumns = @JoinColumn(name ="category_id"))
    private Set<Category> categories = new HashSet<>();

    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return this.user;
    }

    public void addCategory(Category category){
        this.categories.add(category);
        category.addCourseToCategory(this);
    }

    public Set<Category> getCategories() {
        return this.categories;
    }
}
