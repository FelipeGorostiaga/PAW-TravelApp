package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripRate;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface TripRateDao {
    TripRate create(User user, Trip trip, int rate);

    Optional<TripRate> findById(long id);

    void delete(long id);

    void update(TripRate id);

    Optional<TripRate> findByUserAndTrip(long tripId, long userId);
}
