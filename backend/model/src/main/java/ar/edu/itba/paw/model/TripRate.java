package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_rate")
public class TripRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_comments_id_seq")
    @SequenceGenerator(sequenceName = "trip_comments_id_seq", name = "trip_comments_id_seq", allocationSize = 1)
    private long id;

    public TripRate(int rate, LocalDateTime createdOn, Trip trip, User user) {
        this.rate = rate;
        this.createdOn = createdOn;
        this.trip = trip;
        this.user = user;
    }

    @Column(nullable = false)
    private int rate;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    ////////////

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    ////////////

    /* package */ TripRate() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public int getRate() {
        return rate;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public Trip getTrip() {
        return trip;
    }

    public User getUser() {
        return user;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
