package ar.edu.itba.paw.model;


import javax.persistence.*;

@Entity
@Table(name = "trip_pending_confirmation")
public class TripPendingConfirmation {

    /* package */ TripPendingConfirmation() {
        // Just for Hibernate
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_confirmation_id_seq")
    @SequenceGenerator(sequenceName = "trip_confirmation_id_seq", name = "trip_confirmation_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User requestingUser;

    @Column(nullable = false)
    private String token;

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

    public User getRequestingUser() {
        return requestingUser;
    }

    public void setRequestingUser(User requestingUser) {
        this.requestingUser = requestingUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
