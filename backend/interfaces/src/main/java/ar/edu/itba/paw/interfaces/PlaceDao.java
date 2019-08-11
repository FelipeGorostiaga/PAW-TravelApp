package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Place;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface PlaceDao {

    public Place create(String googleId, String name, double latitude, double longitude, String address);
    public Optional<Place> findById(long id);
    public Optional<Place> findByGoogleId(String googleId);


}
