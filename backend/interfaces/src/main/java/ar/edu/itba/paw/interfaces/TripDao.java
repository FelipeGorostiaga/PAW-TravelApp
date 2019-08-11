package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TripDao {

    public Trip create(long userId, long startPlaceId, String name, String description, LocalDate startDate, LocalDate endDate);
    public Optional<Trip> findById(long id);
    public List<Trip> findByName(String name);
    public List<Trip> getAllTrips(int pageNum);
    public List<Trip> findUserCreatedTrips(long userId);
    public void deleteTrip(long tripId);
    public int countAllTrips();
    public List<Trip> findByCategory(String category);
    public List<Trip> findByPlace(String placeName);
    public List<Trip> findWithFilters(Map<String,Object> filterMap);
    public List<TripComment> getTripComments(long tripId);
    public List<TripRate> getTripRates(long tripId);
}
