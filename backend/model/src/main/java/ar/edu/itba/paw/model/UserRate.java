package ar.edu.itba.paw.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "userRates")
public class UserRate {

    /* package */ UserRate() {
        // Just for Hibernate
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userRate_id_seq")
    @SequenceGenerator(sequenceName = "userRate_id_seq", name = "userRate_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User ratedByUser;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User ratedUser;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Trip trip;

    @Column(nullable = false)
    private int rate;

    @Column(length = 500)
    private String comment;

    @Column(nullable = false)
    private boolean pending;

    @Column
    private LocalDateTime createdOn;

    public UserRate(Trip trip, User ratedUser, User ratedByUser) {
        this.trip = trip;
        this.ratedByUser = ratedByUser;
        this.ratedUser = ratedUser;
        this.pending = true;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public long getId() {
        return id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public User getRatedByUser() {
        return ratedByUser;
    }

    public void setRatedByUser(User ratedByUser) {
        this.ratedByUser = ratedByUser;
    }

    public User getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(User ratedUser) {
        this.ratedUser = ratedUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
