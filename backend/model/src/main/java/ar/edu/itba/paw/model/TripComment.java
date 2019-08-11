package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_comments")
public class TripComment implements Comparable<TripComment>{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_comments_id_seq")
    @SequenceGenerator(sequenceName = "trip_comments_id_seq", name = "trip_comments_id_seq", allocationSize = 1)
    private long id;

    @Column(length = 160, nullable = false)
    private String comment;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    ////////////

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    ////////////

    /* package */ TripComment() {
        // Just for Hibernate
    }

    public TripComment(Trip trip, String comment, User user, LocalDateTime createdOn) {
        this.trip = trip;
        this.comment = comment;
        this.user = user;
        this.createdOn = createdOn;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
