package ar.edu.itba.paw.model;


import javax.persistence.*;

@Entity
@Table(name = "trip_invitation")
public class TripInvitation {

    /* package */ TripInvitation() {
        // Just for Hibernate
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tripInvite_id_seq")
    @SequenceGenerator(sequenceName = "tripInvite_id_seq", name = "tripInvite_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User inviter;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User invitee;

    @Column(nullable = false)
    private String token;

    public TripInvitation(Trip trip, User invitee, User inviter,  String token) {
        this.trip = trip;
        this.invitee = invitee;
        this.inviter = inviter;
        this.token = token;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public User getInviter() {
        return inviter;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }

    public User getInvitee() {
        return invitee;
    }

    public void setInvitee(User invitee) {
        this.invitee = invitee;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
