package ar.edu.itba.paw.model;


import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trip_members")
public class TripMember {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_members_id_seq")
    @SequenceGenerator(sequenceName = "trip_members_id_seq", name = "trip_members_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private TripMemberRole role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member")
    private List<TripComment> comments = new LinkedList<>();

    TripMember() {
        //Hibernate
    }

    public TripMember(Trip trip, User user, TripMemberRole role) {
        this.trip = trip;
        this.user = user;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TripMemberRole getRole() {
        return role;
    }

    public void setRole(TripMemberRole role) {
        this.role = role;
    }

    public List<TripComment> getComments() {
        return comments;
    }

    public void setComments(List<TripComment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripMember)) return false;
        TripMember that = (TripMember) o;
        return getTrip().equals(that.getTrip()) &&
                getUser().equals(that.getUser()) &&
                getRole() == that.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTrip(), getUser(), getRole());
    }

    @Override
    public String toString() {
        return "TripMember{" +
                "id=" + id +
                ", trip=" + trip.getId() +
                ", user=" + user.getId() +
                ", role=" + role +
                ", comments=" + comments +
                '}';
    }
}

