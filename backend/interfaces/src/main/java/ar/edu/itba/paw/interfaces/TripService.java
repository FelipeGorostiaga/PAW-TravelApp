package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;
import se.walkercrou.places.exception.GooglePlacesException;

import java.time.LocalDate;
import java.util.*;

public interface TripService {

    public Trip create(long createdBy, double latitude, double longitude, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate,
                       String googlePlaceId, String placeInput) throws GooglePlacesException;

    public Optional<Trip> findById(long id);

    public int countAllPublicTrips();

    public List<Trip> getAllTripsPerPage(int pageNum);

    public List<Trip> findByName(String name, int page);

    public List<Place> findTripPlaces(Trip trip);

    public void addActivityToTrip(long actId, long tripId);

    public void addUserToTrip(long userId, long tripId);

    public void removeUserFromTrip(User user, Trip trip);

    public void deleteTrip(Trip trip);

    public void deleteTripActivity(long activityId, long tripId);

    public PaginatedResult<Trip> findWithFilters(Map<String, Object> filterMap, int page);

    public List<TripComment> getTripComments(long tripId);

    public boolean createJoinRequest(Trip trip, User user);

    public List<TripPendingConfirmation> getTripJoinRequests(long tripId);

    public boolean updateJoinRequest(Trip trip, User loggedUser, String token, boolean accepted, User requester);

    public Optional<TripPendingConfirmation> findJoinRequestByToken(String token);

    public TripInvitation inviteUserToTrip(Trip trip, User invitedUser, User admin);

    public Boolean isWaitingJoinTripConfirmation(Trip trip, User user);

    public Optional<TripInvitation> findTripInvitationByToken(String token);

    public void acceptOrRejectTripInvitation(String token, boolean accepted, User invitedUser, Trip trip);

    public Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user);

    public void editTripImage(Trip trip, byte[] resizedImage);

    public void editTripData(String tripName, String description, long tripId);

    public void grantAdminRole(long tripId, long invitedUserId);

    public boolean isCreator(Trip trip, User loggedUser);

    public boolean isAdmin(Trip trip, User loggedUser);

    public boolean isMember(Trip trip, User user);

    public PaginatedResult<Trip> getUserTrips(User user, int page);

    public void markTripAsCompleted(long tripId);

    public Boolean hasImage(long tripId);

    public int countUserTrips(User user);

    public int countUserTripsWithStatus(long userId, TripStatus status);

    public int countByNameSearch(String name);


}
