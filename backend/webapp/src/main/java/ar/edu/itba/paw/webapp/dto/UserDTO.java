package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.time.LocalDate;
import java.util.Objects;

public class UserDTO {

    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate birthday;
    private String biography;
    private String nationality;
    private String sex;

    private URI url;
    private URI pictureURL;
    private URI userTripsURL;
    private URI profileDataURL;
    private URI userRatesURL;



    public UserDTO() {
        // Empty constructor needed by JAX-RS
    }

    public UserDTO(User user, final URI baseUri) {
        id = user.getId();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        email = user.getEmail();
        birthday = user.getBirthday();
        nationality = user.getNationality();
        biography = user.getBiography();
        sex = user.getSex();

        url = baseUri.resolve("users/" + id);
        pictureURL = baseUri.resolve("users/picture/" + id);
        userTripsURL = baseUri.resolve("users/" + id + "/trips");
        profileDataURL = baseUri.resolve("users/" + id + "/profile");
        userRatesURL = baseUri.resolve("users/" + id + "/rates");

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public URI getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(URI pictureURL) {
        this.pictureURL = pictureURL;
    }

    public URI getUserTripsURL() {
        return userTripsURL;
    }

    public void setUserTripsURL(URI userTripsURL) {
        this.userTripsURL = userTripsURL;
    }

    public URI getProfileDataURL() {
        return profileDataURL;
    }

    public void setProfileDataURL(URI profileDataURL) {
        this.profileDataURL = profileDataURL;
    }

    public URI getUserRatesURL() {
        return userRatesURL;
    }

    public void setUserRatesURL(URI userRatesURL) {
        this.userRatesURL = userRatesURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return getId() == userDTO.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
