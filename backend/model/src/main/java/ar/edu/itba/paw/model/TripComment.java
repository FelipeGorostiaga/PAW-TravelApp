package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

/*    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Trip trip;*/

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    private TripMember member;

    public TripComment(Trip trip, String comment, TripMember member, LocalDateTime createdOn) {

        this.comment = comment;
        this.member = member;
        this.createdOn = createdOn;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

/*    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    public TripMember getMember() {
        return member;
    }

    public void setMember(TripMember member) {
        this.member = member;
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
