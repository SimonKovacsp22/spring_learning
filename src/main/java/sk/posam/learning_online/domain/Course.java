package sk.posam.learning_online.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

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
                    name = "student_book_fk"
            )
    )
    private User user;

    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return this.user;
    }
}
