package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Course {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "course_id")
    private long id;

    String title;

    private Double price;

    @Column(name = "last_update")
    private LocalDateTime lastUpdated;
    @Column(columnDefinition = "TEXT")
    private String description;

    private String subtitle;

    private String imageUrl;

    @ManyToMany( cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(name="course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name ="user_id"))
    private Set<User> students = new HashSet<>();

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

    @ManyToMany( cascade = CascadeType.ALL,
                    fetch = FetchType.EAGER)
    @JoinTable(name="course_category",
    joinColumns = @JoinColumn(name = "course_id"),
    inverseJoinColumns = @JoinColumn(name ="category_id"))
    private Set<Category> categories = new HashSet<>();


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<WhatYouWillLearn> whatYouWillLearn;

    @ManyToMany( cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(name="course_languages",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name ="language_id"))
    private Set<Language> languages = new HashSet<>();

    @OneToMany(mappedBy="course",fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Video> videos = new HashSet<>();;

    @OneToMany(mappedBy = "course",fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Rating> ratings = new HashSet<>();;

    @ManyToMany
    @JoinTable(name="course_cart",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name ="cart_id"))
    @JsonIgnore
    private Set<Cart> carts = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course",fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Progress> progresses = new HashSet<>();



    public Course() {
    }

    public Course(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void addStudent(User student) {
        if(student != user && !students.contains(student)) {
            students.add(student);
            student.addCourseToStudy(this);
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public List<WhatYouWillLearn> getWhatYouWillLearn() {
        return whatYouWillLearn;
    }

    public void addWYWL(WhatYouWillLearn whatYouWillLearn) {
        this.whatYouWillLearn.add(whatYouWillLearn);
        whatYouWillLearn.setCourse(this);
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void addLanguage(Language language) {
        this.languages.add(language);
        language.addCourse(this);
    }

    public Set<Video> getVideos() {
        return videos;
    }

    public void addVideo(Video video) {
        this.videos.add(video);
        video.setCourse(this);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setCourse(this);
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void addCartToCourse(Cart cart) {
        this.carts.add(cart);
    }

    public void removeCartFromCourse(Cart cart) {
        this.carts.remove(cart);
    }

    public Set<Progress> getProgresses() {
        return progresses;
    }

    public void addProgress(Progress progress) {
        this.progresses.add(progress);
        progress.setCourse(this);
    }


    @Override
    public String toString() {
        return "Course{" +
                "progresses=" + progresses +
                "id=" + id +
                '}';
    }
}
