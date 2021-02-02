package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    /* package */ User() {
        // Just for Hibernate
    }

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

    @Column(length = 1, nullable = false)
    private String sex;

    @Column
    private boolean verified;

    @Column(updatable = false, length = 64)
    private String verificationCode;

    @Column(length = 500)
    private String biography;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<TripMember> trips = new LinkedList<>();

    /*@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripComment> comments;*/

    /*@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Trip> trips = new HashSet<>();

    @ManyToMany(mappedBy = "admins", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Trip> adminTrips = new HashSet<>();*/

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserPicture profilePicture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ratedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRate> myRates;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ratedByUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRate> othersRates;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "requestingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripPendingConfirmation> pendingConfirmations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invitee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripInvitation> receivedTripInvitations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inviter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripInvitation> sentTripInvitations;

    @Column(length = 100, nullable = false)
    private String nationality;

    public User(long id, String firstname, String lastname, String email, String password, LocalDate birthday, String nationality, String sex, String verificationCode) {
        this(firstname, lastname, email, password, birthday, nationality, sex, verificationCode);
        this.id = id;
    }

    public User(String firstname, String lastname, String email, String password, LocalDate birthday, String nationality, String sex, String verificationCode) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.nationality = nationality;
        this.sex = sex;
        this.verified = false;
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<TripPendingConfirmation> getPendingConfirmations() {
        return pendingConfirmations;
    }

    public void setPendingConfirmations(List<TripPendingConfirmation> pendingConfirmations) {
        this.pendingConfirmations = pendingConfirmations;
    }

    public List<TripInvitation> getReceivedTripInvitations() {
        return receivedTripInvitations;
    }

    public void setReceivedTripInvitations(List<TripInvitation> receivedTripInvitations) {
        this.receivedTripInvitations = receivedTripInvitations;
    }

    public List<TripInvitation> getSentTripInvitations() {
        return sentTripInvitations;
    }

    public void setSentTripInvitations(List<TripInvitation> sentTripInvitations) {
        this.sentTripInvitations = sentTripInvitations;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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
        return "USER = [" + id + "]" + firstname + " " + lastname;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<TripMember> getTrips() {
        return trips;
    }

    public void setTrips(List<TripMember> trips) {
        this.trips = trips;
    }

    /*public List<TripComment> getComments() {
        return comments;
    }

    public void setComments(List<TripComment> comments) {
        this.comments = comments;
    }*/

    /*public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    public Set<Trip> getAdminTrips() {
        return adminTrips;
    }

    public void setAdminTrips(Set<Trip> adminTrips) {
        this.adminTrips = adminTrips;
    }*/

    public List<UserRate> getMyRates() {
        return myRates;
    }

    public void setMyRates(List<UserRate> myRates) {
        this.myRates = myRates;
    }

    public List<UserRate> getOthersRates() {
        return othersRates;
    }

    public void setOthersRates(List<UserRate> othersRates) {
        this.othersRates = othersRates;
    }
}
