package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;

import java.util.Optional;

public interface TripPicturesDao {

    public TripPicture create(Trip trip, byte[] image);
    public Optional<TripPicture> findByTripId(long tripId);
    public boolean deleteByTripId(long tripId);

}
