package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TripDao {

    public Trip create(User creator, Place startPlace, String name, String description, LocalDate startDate, LocalDate endDate, boolean isPrivate);

    public Optional<Trip> findById(long id);

    public List<Trip> findByName(String name, int page);

    public void deleteTrip(long tripId);

    public int countAllPublicTrips();

    public PaginatedResult<Trip> findWithFilters(Map<String, Object> filterMap, int page);

    public List<TripComment> getTripComments(long tripId);

    public List<Trip> getAllTripsPerPage(int pageNum);

    public TripPendingConfirmation createPendingConfirmation(Trip trip, User user, String token);

    public List<TripPendingConfirmation> getTripJoinRequests(long tripId);

    public boolean editJoinRequest(Trip trip, User u, String token, boolean accepted);

    public Optional<TripPendingConfirmation> findJoinRequestByToken(String token);

    public TripInvitation createTripInvitation(Trip trip, User invitedUser, User admin, String token);

    public Optional<TripPendingConfirmation> findTripConfirmationByUser(Trip trip, User user);

    public Optional<TripInvitation> findTripInvitationByToken(String token);

    public void deleteTripInvitation(String token, Trip trip);

    public Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user);

    public void updateTripData(String tripName, String description, long tripId);

    public TripMember createTripMember(Trip trip, User user, TripMemberRole role);

    public void updateRoleToAdmin(long tripId, long invitedUserId);

    public void markTripAsCompleted(long tripId);

    public Boolean hasImage(long tripId);

    public void deleteTripMember(long userId, long tripId);

    public void deleteAllTripMembers(long tripId);

    public void deleteTripInvitations(long tripId);

    public int countByNameSearch(String name);

    public void deleteTripPendingConfirmations(long id);

    public PaginatedResult<Trip> findUserTrips(long userId, int page);

    List<TripMember> getTripMembers(long tripId);
}
