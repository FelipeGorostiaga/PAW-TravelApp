package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "trip_comments")
public class TripComment implements Comparable<TripComment>{

    /* package */ TripComment() {
        // Just for Hibernate
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_comments_id_seq")
    @SequenceGenerator(sequenceName = "trip_comments_id_seq", name = "trip_comments_id_seq", allocationSize = 1)
    private long id;

    @Column(length = 160, nullable = false)
    private String comment;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    public TripComment(String comment, Trip trip, User user, LocalDateTime createdOn) {
        this.comment = comment;
        this.trip = trip;
        this.user = user;
        this.createdOn = createdOn;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public int compareTo(TripComment o) {
        return this.createdOn.isBefore(o.createdOn) ? -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TripComment)) return false;
        TripComment that = (TripComment) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
