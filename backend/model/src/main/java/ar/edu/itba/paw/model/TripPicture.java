package ar.edu.itba.paw.model;

import javax.persistence.*;


@Entity
@Table(name = "trip_pictures")
public class TripPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_picture_id_seq")
    @SequenceGenerator(sequenceName = "trip_picture_id_seq", name = "trip_picture_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "image")
    private byte[] picture;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="trip_id")
    private Trip trip;

    public TripPicture(long id, byte[] picture, Trip trip) {
        this(picture, trip);
        this.id = id;
    }

    public TripPicture(byte[] picture, Trip trip) {
        this.picture = picture;
        this.trip = trip;
    }

    /* package */ TripPicture() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
