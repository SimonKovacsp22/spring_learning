package sk.posam.learning_online.controller.dto;

import sk.posam.learning_online.domain.Progress;
import sk.posam.learning_online.domain.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserDto {

    public UserDto(User user) {
        this.id = user.getId();
        this.firstName =  user.getFirstName();
        this.lastName =  user.getLastName();
        this.email =  user.getEmail();
        this.role =  user.getRole();
        this.createDt =  user.getCreateDt();
        this.progresses =  createProgressesCopy(user.getProgresses());
    }

    public Set<Progress> createProgressesCopy( Set<Progress> progresses) {
       Set<Progress> progressesCopy = new HashSet<>(progresses);
       return progressesCopy;
    }

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String role;

    private LocalDateTime createDt;

    private Set<Progress> progresses;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public Set<Progress> getProgresses() {
        return progresses;
    }

    public void setProgresses(Set<Progress> progresses) {
        this.progresses = progresses;
    }
}
