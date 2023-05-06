package sk.posam.learning_online.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Entity
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Course> courses;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;
    @Column
    private String role;

    @Column(name = "create_dt")
    private LocalDateTime createDt;

    @JsonIgnore
    @OneToMany(mappedBy="user",fetch= FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Authority> authorities;

    public User() {
    }

    public User(String firstName, String lastName, String email, String pwd, String role, LocalDateTime createDt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pwd = pwd;
        this.role = role;
        this.createDt = createDt;
        this.courses = new ArrayList<>();
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
    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        if(!this.courses.contains(course)){
            courses.add(course);
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
}

