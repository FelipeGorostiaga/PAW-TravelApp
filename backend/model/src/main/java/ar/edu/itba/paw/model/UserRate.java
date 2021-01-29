package ar.edu.itba.paw.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "userRates")
public class UserRate {

    /* package */ UserRate() {
        // Just for Hibernate
    }

    public UserRate(int rate, User ratedUser, User ratedByUser, String comment, LocalDateTime createdOn) {
        this.rate = rate;
        this.ratedByUser = ratedByUser;
        this.ratedUser = ratedUser;
        this.comment = comment;
        this.createdOn = createdOn;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userRate_id_seq")
    @SequenceGenerator(sequenceName = "userRate_id_seq", name = "userRate_id_seq", allocationSize = 1)
    private long id;

    @Column(nullable = false)
    private int rate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User ratedByUser;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User ratedUser;

    @Column(length = 500, nullable = false)
    private String comment;

    private LocalDateTime createdOn;

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
