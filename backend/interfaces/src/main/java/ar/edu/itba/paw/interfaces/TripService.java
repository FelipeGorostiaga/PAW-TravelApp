package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.*;

public interface TripService {

    public Trip create(long createdBy, long place, String name, String description, LocalDate startDate, LocalDate endDate);
    public Optional<Trip> findById(long id);
    public List<Trip> getAllTrips(int pageNum);
    public List<Trip> findByName(String name);
    public List<Trip> getAllUserTrips(User user);
    public List<Place> findTripPlaces(Trip trip);
    public void addActivityToTrip(long actId, long tripId);
    public void addUserToTrip(long userId, long tripId);
    public void removeUserFromTrip(long userId, long tripId);
    public void deleteTrip(long tripId);
    public void addCommentToTrip(long commentId, long tripId);
    public void deleteTripActivity(long activityId, long tripId);
    public int countAllTrips();
    public List<Trip> findByCategory(String category);
    public List<Trip> findByPlace(String placeName);
    public List<Trip> findWithFilters(Map<String,Object> filterMap);
    public List<TripComment> getTripComments(long tripId);
    public List<TripRate> getTripRates(long tripId);
}
