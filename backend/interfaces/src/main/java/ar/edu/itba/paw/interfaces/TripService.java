package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;
import se.walkercrou.places.exception.GooglePlacesException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TripService {

    Trip create(long createdBy, double latitude, double longitude, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate,
                String googlePlaceId, String placeInput) throws GooglePlacesException;

    Optional<Trip> findById(long id);

    int countAllPublicTrips();

    List<Trip> getAllTripsPerPage(int pageNum);

    List<Trip> findByName(String name, int page);

    List<Place> findTripPlaces(Trip trip);

    void addActivityToTrip(long actId, long tripId);

    void addUserToTrip(long userId, long tripId);

    void removeUserFromTrip(User user, Trip trip);

    void deleteTrip(Trip trip);

    void deleteTripActivity(long activityId, long tripId);

    PaginatedResult<Trip> findWithFilters(Map<String, Object> filterMap, int page);

    List<TripComment> getTripComments(long tripId);

    boolean createJoinRequest(Trip trip, User user);

    List<TripPendingConfirmation> getTripJoinRequests(long tripId);

    boolean updateJoinRequest(Trip trip, User loggedUser, String token, boolean accepted, User requester);

    Optional<TripPendingConfirmation> findJoinRequestByToken(String token);

    TripInvitation inviteUserToTrip(Trip trip, User invitedUser, User admin);

    Optional<TripInvitation> findTripInvitationByToken(String token);

    void acceptOrRejectTripInvitation(String token, boolean accepted, User invitedUser, Trip trip);

    Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user);

    void editTripImage(Trip trip, byte[] resizedImage);

    void editTripData(String tripName, String description, long tripId);

    void grantAdminRole(long tripId, long invitedUserId);

    boolean isCreator(Trip trip, User loggedUser);

    boolean isAdmin(Trip trip, User loggedUser);

    boolean isMember(Trip trip, User user);

    PaginatedResult<Trip> getUserTrips(User user, int page);

    void markTripAsCompleted(long tripId);

    int countUserTripsWithStatus(long userId, TripStatus status);

    List<TripMember> getTripMembers(long tripId);
}
