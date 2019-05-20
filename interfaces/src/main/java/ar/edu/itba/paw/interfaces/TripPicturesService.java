package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripPicture;

import java.util.Optional;

public interface TripPicturesService {

    public TripPicture create(long tripId, byte[] image);

    public Optional<TripPicture> findByTripId(long tripId);
    public boolean deleteByTripId(long tripId);

}
