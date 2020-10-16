package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.*;

public interface TripService {

    Trip create(long createdBy, Place place, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate);
    Optional<Trip> findById(long id);
    List<Trip> getAllTripsPerPage(int pageNum);
    List<Trip> getAllTrips();
    List<Trip> findByName(String name);
    List<Trip> getAllUserTrips(User user);
    List<Place> findTripPlaces(Trip trip);
    void addActivityToTrip(long actId, long tripId);
    void addUserToTrip(long userId, long tripId);
    void removeUserFromTrip(long userId, long tripId);
    void deleteTrip(long tripId);
    void addCommentToTrip(long commentId, long tripId);
    void deleteTripActivity(long activityId, long tripId);
    int countAllTrips();
    List<Trip> findByCategory(String category);
    List<Trip> findByPlace(String placeName);
    List<Trip> findWithFilters(Map<String,Object> filterMap);
    List<TripComment> getTripComments(long tripId);
    List<User> getTripUsers(long tripId);
    List<User> getTripAdmins(long tripId);
}
