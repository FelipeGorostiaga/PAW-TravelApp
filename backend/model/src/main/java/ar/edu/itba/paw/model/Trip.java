package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "trips")
public class Trip implements Comparable<Trip>{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_id_seq")
    @SequenceGenerator(sequenceName = "trip_id_seq", name = "trip_id_seq", allocationSize = 1)
    private long id;

    @Column
    private long startPlaceId;

    @Column
    private long adminId;

    @Column
    private boolean isPrivate;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Trip_Users",
            joinColumns = { @JoinColumn(name = "trip_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Trip_Admins",
            joinColumns = { @JoinColumn(name = "trip_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> admins = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trip")
    private List<Activity> activities = new LinkedList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "trip")
    private TripPicture profilePicture;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trip")
    private List<TripComment> comments = new LinkedList<>();


    public Trip(long id, long adminId, long startPlaceId, String name, String description, LocalDate startDate, LocalDate endDate,
                boolean isPrivate) {
        this(adminId, startPlaceId, name, description, startDate, endDate, isPrivate);
        this.id = id;
    }

    public Trip(long adminId, long startPlaceId, String name, String description, LocalDate startDate, LocalDate endDate,
                boolean isPrivate) {
        super();
        this.adminId = adminId;
        this.startPlaceId = startPlaceId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPrivate = isPrivate;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    /* package */ Trip() {
        // Just for Hibernate
    }

    public long getStartPlaceId() {
        return startPlaceId;
    }

    public void setStartPlaceId(long startPlaceId) {
        this.startPlaceId = startPlaceId;
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

    public List<TripComment> getComments() {
        return comments;
    }

    public void setComments(List<TripComment> comments) {
        this.comments = comments;
    }

    @Override
    public int compareTo(Trip o) {
        return (this.startDate.isBefore(o.startDate)) ? -1 : 1;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<User> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<User> admins) {
        this.admins = admins;
    }
}
