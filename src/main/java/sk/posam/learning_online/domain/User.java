package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import sk.posam.learning_online.domain.views.views;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@JsonView(views.Public.class)
@Table(name = "\"user\"", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    @Column(name = "user_id")
    private long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Course> coursesTaught;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;
    @Column
    private String role;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @JsonIgnore
    @OneToMany(mappedBy="user",fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Authority> authorities;


    @JsonIgnore
    @ManyToMany(mappedBy = "students",fetch = FetchType.EAGER)
    private Set<Course> coursesTaken = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Rating> ratings;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Cart> carts;

    private String Avatar;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Progress> progresses = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    public User() {
    }

    public User(String firstName, String lastName, String email, String pwd, String role, LocalDateTime createDt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
        this.role = role;
        this.createDt = createDt;
        this.coursesTaught = new ArrayList<>();
    }

    public void addCourseToStudy(Course course) {
        if(!coursesTaught.contains(course)) {
            coursesTaken.add(course);

        }
    }

    public Set<Course> getCoursesTaken() {
        return coursesTaken;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateDt() {
        return createDt;
    }

    public void setCreateDt(LocalDateTime createDt) {
        this.createDt = createDt;
    }
    public List<Course> getCoursesTaught() {
        return coursesTaught;
    }

    public void addCourseTaught(Course course) {
        if(!this.coursesTaught.contains(course)){
            coursesTaught.add(course);
            course.setUser(this);
        }
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
        authorities.forEach(authority -> authority.setUser(this));
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
        rating.setUser(this);
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public Set<Cart> getCarts() {
        return carts;
    }

    public void addCartToUser(Cart cart) {
        this.carts.add(cart);
    }

    public Set<Progress> getProgresses() {
        return progresses;
    }

    public void addProgress (Progress progress) {
        this.progresses.add(progress);
        progress.setUser(this);
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        if(order != null) {
            this.orders.add(order);
            order.setUser(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

