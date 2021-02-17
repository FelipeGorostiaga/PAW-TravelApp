package ar.edu.itba.paw.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "trips")
public class Trip implements Comparable<Trip> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_id_seq")
    @SequenceGenerator(sequenceName = "trip_id_seq", name = "trip_id_seq", allocationSize = 1)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status;

    @Column(nullable = false)
    private boolean isPrivate;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 400, nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripComment> comments;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "place_id")
    private Place startPlace;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TripMember> members = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activities = new LinkedList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip")
    private List<UserRate> rates = new LinkedList<>();

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private TripPicture profilePicture;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripInvitation> pendingConfirmations;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripInvitation> invitations;

    /* package */ Trip() {
        // Just for Hibernate
    }

    public Trip(Place startPlace, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate) {
        this.name = name;
        this.description = description;
        this.startPlace = startPlace;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPrivate = isPrivate;
        this.status = TripStatus.DUE;
    }

    public List<TripComment> getComments() {
        return comments;
    }

    public void setComments(List<TripComment> comments) {
        this.comments = comments;
    }

    public List<UserRate> getRates() {
        return rates;
    }

    public void setRates(List<UserRate> rates) {
        this.rates = rates;
    }

    public List<TripInvitation> getPendingConfirmations() {
        return pendingConfirmations;
    }

    public void setPendingConfirmations(List<TripInvitation> pendingConfirmations) {
        this.pendingConfirmations = pendingConfirmations;
    }

    public List<TripInvitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<TripInvitation> invitations) {
        this.invitations = invitations;
    }

    public Place getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(Place startPlace) {
        this.startPlace = startPlace;
    }

    public long getId() {
        return id;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public TripPicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(TripPicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public int compareTo(Trip o) {
        return (this.startDate.isBefore(o.startDate)) ? -1 : 1;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return id == trip.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Set<TripMember> getMembers() {
        return members;
    }

    public void setMembers(Set<TripMember> members) {
        this.members = members;
    }
}
