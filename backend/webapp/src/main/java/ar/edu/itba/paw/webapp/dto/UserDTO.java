package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@XmlRootElement
public class UserDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthday;
    private String biography;
    private String nationality;

    public UserDTO() {
        // Empty constructor needed by JAX-RS
    }

    public UserDTO(User user) {
        id = user.getId();
        firstName = user.getFirstname();
        lastName = user.getLastname();
        email = user.getEmail();
        birthday = user.getBirthday();
        nationality = user.getNationality();
        biography = user.getBiography();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstname) {
        this.firstName = firstname;
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

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
