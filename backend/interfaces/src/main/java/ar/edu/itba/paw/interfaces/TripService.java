package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;
import se.walkercrou.places.exception.GooglePlacesException;

import java.time.LocalDate;
import java.util.*;

public interface TripService {

    Trip create(long createdBy, double latitude, double longitude, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate) throws GooglePlacesException;

    Optional<Trip> findById(long id);

    List<Trip> getAllTripsPerPage(int pageNum);

    List<Trip> getAllTrips();

    List<Trip> findByName(String name);

    // Set<Trip> getAllUserTrips(User user);

    List<Place> findTripPlaces(Trip trip);

    void addActivityToTrip(long actId, long tripId);

    void addUserToTrip(long userId, long tripId);

    void removeUserFromTrip(long userId, long tripId);

    void deleteTrip(long tripId);

    void deleteTripActivity(long activityId, long tripId);

    int countAllTrips();

    List<Trip> findByCategory(String category);

    List<Trip> findByPlace(String placeName);

    List<Trip> findWithFilters(Map<String, Object> filterMap);

    List<TripComment> getTripComments(long tripId);

    boolean createJoinRequest(Trip trip, User user, String token);

    List<TripPendingConfirmation> getTripJoinRequests(long tripId);

    boolean updateJoinRequest(Trip trip, User loggedUser, String token, boolean accepted, User requester);

    Optional<TripPendingConfirmation> findJoinRequestByToken(String token);

    TripInvitation inviteUserToTrip(Trip trip, User invitedUser, User admin);

    Boolean isWaitingJoinTripConfirmation(Trip trip, User user);

    Optional<TripInvitation> findTripInvitationByToken(String token);

    void acceptOrRejectTripInvitation(String token, boolean accepted, User invitedUser, Trip trip);

    Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user);

    void editTripImage(Trip trip, byte[] resizedImage);

    void editTripData(String tripName, String description, long tripId);

    void grantAdminRole(long tripId, long invitedUserId);

    boolean traveledTogether(Trip trip, User ratedUser, User ratedBy);

    boolean isCreator(Trip trip, User loggedUser);

    boolean isAdmin(Trip trip, User loggedUser);

    boolean isMember(Trip trip, User user);

    List<Trip> getUserTrips(User user);

    void markTripAsCompleted(long tripId);
}
