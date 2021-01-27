package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TripDao {

    Trip create(long userId, Place startPlace, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate);

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

    TripPendingConfirmation createPendingConfirmation(Trip trip, User user, String token);

    List<TripPendingConfirmation> getTripJoinRequests(long tripId);

    boolean editJoinRequest(Trip trip, User u, String token, boolean accepted);

    Optional<TripPendingConfirmation> findJoinRequestByToken(String token);

    TripInvitation createTripInvitation(Trip trip, User invitedUser, User admin, String token);

    Optional<TripPendingConfirmation> findTripConfirmationByUser(Trip trip, User user);

    Optional<TripInvitation> findTripInvitationByToken(String token);

    void deleteTripInvitation(String token, Trip trip);

    Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user);

    void updateTripData(String tripName, String description, long tripId);

}
