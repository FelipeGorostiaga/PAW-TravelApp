package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.time.LocalDate;

@XmlRootElement
public class UserDTO {

    private long id;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private LocalDate birthday;

    private String biography;

    private String nationality;

    private URI url;

    public UserDTO(User user,  final URI baseUri) {
        id = user.getId();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        email = user.getEmail();

        url = baseUri.resolve("users/" + id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
