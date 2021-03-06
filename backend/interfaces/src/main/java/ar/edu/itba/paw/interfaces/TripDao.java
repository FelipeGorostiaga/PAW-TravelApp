package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TripDao {

    Trip create(User creator, Place startPlace, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate);

    Optional<Trip> findById(long id);

    List<Trip> findByName(String name, int page);

    void deleteTrip(long tripId);

    int countAllPublicTrips();

    PaginatedResult<Trip> findWithFilters(Map<String, Object> filterMap, int page);

    List<TripComment> getTripComments(long tripId);

    List<Trip> getAllTripsPerPage(int pageNum);

    TripPendingConfirmation createPendingConfirmation(Trip trip, User user, String token);

    List<TripPendingConfirmation> getTripJoinRequests(long tripId);

    boolean editJoinRequest(Trip trip, User u, String token, boolean accepted);

    Optional<TripPendingConfirmation> findJoinRequestByToken(String token);

    TripInvitation createTripInvitation(Trip trip, User invitedUser, User admin, String token);

    Optional<TripInvitation> findTripInvitationByToken(String token);

    void deleteTripInvitation(String token, Trip trip);

    Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user);

    void updateTripData(String tripName, String description, long tripId);

    TripMember createTripMember(Trip trip, User user, TripMemberRole role);

    void updateRoleToAdmin(long tripId, long invitedUserId);

    void markTripAsCompleted(long tripId);

    void deleteTripMember(long userId, long tripId);

    void deleteAllTripMembers(long tripId);

    void deleteTripInvitations(long tripId);

    void deleteTripPendingConfirmations(long id);

    PaginatedResult<Trip> findUserTrips(long userId, int page);

    List<TripMember> getTripMembers(long tripId);
}
