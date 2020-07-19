package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TripDao {

     Trip create(long userId, long startPlaceId, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate);
     Optional<Trip> findById(long id);
     List<Trip> findByName(String name);
     List<Trip> findUserCreatedTrips(long userId);
     void deleteTrip(long tripId);
     int countAllTrips();
     List<Trip> findByCategory(String category);
     List<Trip> findByPlace(String placeName);
     List<Trip> findWithFilters(Map<String, Object> filterMap);
     List<TripComment> getTripComments(long tripId);
     List<Trip> getAllTrips();
     List<Trip> getAllTripsPerPage(int pageNum);
     List<User> getTripUsers(long tripId);
     List<User> getTripAdmins(long tripId);

}
