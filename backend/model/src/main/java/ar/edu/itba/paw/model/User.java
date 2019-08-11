package ar.edu.itba.paw.model;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(sequenceName = "user_id_seq", name = "user_id_seq", allocationSize = 1)
    private long id;

    @Column(length = 100, nullable = false)
    private String firstname;

    @Column(length = 100, nullable = false)
    private String lastname;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(length = 500)
    private String biography;

    //////////////

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Trip> trips;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserPicture profilePicture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripComment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripComment> rates;

    /////////////

    @Column(length = 100, nullable = false)
    private String nationality;

    public User(long id, String firstname, String lastname, String email, String password, LocalDate birthday, String nationality) {
        this(firstname, lastname, email, password, birthday, nationality);
        this.id = id;
    }

    public User(String firstname, String lastname, String email, String password, LocalDate birthday, String nationality) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.nationality = nationality;
    }

    /* package */ User() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
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

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public UserPicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(UserPicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                firstname.equals(user.firstname) &&
                lastname.equals(user.lastname) &&
                email.equals(user.email) &&
                password.equals(user.password) &&
                birthday.equals(user.birthday) &&
                nationality.equals(user.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, password, birthday, nationality);
    }

    @Override
    public String toString() {
        return "USER = [" + id + "]" +  firstname + " " + lastname;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<TripComment> getComments() {
        return comments;
    }

    public void setComments(List<TripComment> comments) {
        this.comments = comments;
    }
}
