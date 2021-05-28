package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

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
    private URI imageURL;
    private URI tripsURL;
    private URI ratesURL;
    private URI pendingRatesURL;
    private URI invitationsURL;

    public UserDTO() {
        // Empty constructor needed by JAX-RS
    }

    public UserDTO(User user, final URI baseUri) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.nationality = user.getNationality();
        this.biography = user.getBiography();
        this.sex = user.getSex();

        this.url = baseUri.resolve("users/" + id);
        this.tripsURL = baseUri.resolve("users/" + id + "/trips");
        this.ratesURL = baseUri.resolve("users/" + id + "/rates");
        this.pendingRatesURL = baseUri.resolve("users/" + id + "/pending-rates");
        this.invitationsURL = baseUri.resolve("users/" + id + "/invitations");
        if (user.getProfilePicture() != null) {
            this.imageURL = baseUri.resolve("users/" + id + "/image");
        }

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

    public URI getImageURL() {
        return imageURL;
    }

    public void setImageURL(URI imageURL) {
        this.imageURL = imageURL;
    }

    public URI getPendingRatesURL() {
        return pendingRatesURL;
    }

    public void setPendingRatesURL(URI pendingRatesURL) {
        this.pendingRatesURL = pendingRatesURL;
    }

    public URI getTripsURL() {
        return tripsURL;
    }

    public void setTripsURL(URI tripsURL) {
        this.tripsURL = tripsURL;
    }

    public URI getRatesURL() {
        return ratesURL;
    }

    public void setRatesURL(URI ratesURL) {
        this.ratesURL = ratesURL;
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
