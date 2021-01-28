package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.walkercrou.places.exception.GooglePlacesException;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    @Autowired
    private TripDao td;

    @Autowired
    private ActivityDao ad;

    @Autowired
    private TripCommentsDao tcd;

    @Autowired
    private TripPicturesDao tpd;

    @Autowired
    private UserDao ud;

    @Autowired
    private GoogleMapsService googleMapsService;

    @Autowired
    private MailingService mailService;

    @Autowired
    private TripPicturesService tripPicturesService;

    @Override
    public Trip create(long userId, double latitude, double longitude, String name, String description,
                       LocalDate startDate, LocalDate endDate, boolean isPrivate) throws GooglePlacesException {
        List<se.walkercrou.places.Place> googleMapsPlaces = googleMapsService.queryGoogleMapsPlaces(latitude, longitude);
        Place startPlace = googleMapsService.createGooglePlaceReference(googleMapsPlaces);
        Trip t;
        Optional<User> u = ud.findById(userId);
        if (u.isPresent()) {
            t = td.create(userId, startPlace, name, description, startDate, endDate, isPrivate);
            t.getUsers().add(u.get());
            t.getAdmins().add(u.get());
            return t;
        }
        return null;
    }

    @Override
    public Optional<Trip> findById(long id) {
        return td.findById(id);
    }

    @Override
    public List<Trip> getAllTripsPerPage(int pageNum) {
        return td.getAllTripsPerPage(pageNum);
    }

    @Override
    public List<Trip> getAllTrips() {
        return td.getAllTrips();
    }

    @Override
    public List<Trip> findByName(String name) {
        return td.findByName(name);
    }

    @Override
    public Set<Trip> getAllUserTrips(User user) {
        Set<Trip> trips = new HashSet<>(user.getTrips());
        List<Trip> createdTrips = td.findUserCreatedTrips(user.getId());
        trips.addAll(createdTrips);
        return trips;
    }

    @Override
    public List<Place> findTripPlaces(Trip trip) {
        List<Activity> activities = trip.getActivities();
        List<Place> places = new LinkedList<>();
        for (Activity activity : activities) {
            places.add(activity.getPlace());
        }
        return places;
    }

    @Override
    public void addActivityToTrip(long actId, long tripId) {
        Optional<Trip> ot = td.findById(tripId);
        Optional<Activity> oa = ad.findById(actId);
        if (ot.isPresent() && oa.isPresent()) {
            ot.get().getActivities().add(oa.get());
        }
    }

    @Override
    public void addUserToTrip(long userId, long tripId) {
        Optional<User> ou = ud.findById(userId);
        Optional<Trip> ot = td.findById(tripId);
        if (ou.isPresent() && ot.isPresent()) {
            ou.get().getTrips().add(ot.get());
            ot.get().getUsers().add(ou.get());
        }
    }

    @Override
    public void removeUserFromTrip(long userId, long tripId) {
        Optional<User> ou = ud.findById(userId);
        Optional<Trip> ot = td.findById(tripId);
        if (ou.isPresent() && ot.isPresent()) {
            ou.get().getTrips().remove(ot.get());
            ot.get().getUsers().remove(ou.get());
        }
    }

    @Override
    public void deleteTrip(long tripId) {
        tcd.deleteComments(tripId);
        ad.deleteActivities(tripId);
        tpd.deleteByTripId(tripId);
        td.deleteTrip(tripId);
    }

    @Override
    public void addCommentToTrip(long commentId, long tripId) {
        Optional<TripComment> otc = tcd.findById(commentId);
        Optional<Trip> ot = td.findById(tripId);
        if (otc.isPresent() && ot.isPresent()) {
            ot.get().getComments().add(otc.get());
        }
    }

    @Override
    public void deleteTripActivity(long activityId, long tripId) {
        Optional<Trip> ot = td.findById(tripId);
        Optional<Activity> oa = ad.findById(activityId);
        if (ot.isPresent() && oa.isPresent()) {
            ot.get().getActivities().remove(oa.get());
            oa.get().getPlace().getActivities().remove(oa.get());
            ad.deleteActivity(activityId);
        }
    }

    @Override
    public int countAllTrips() {
        return td.countAllTrips();
    }

    @Override
    public List<Trip> findByCategory(String category) {
        return td.findByCategory(category);
    }

    @Override
    public List<Trip> findByPlace(String placeName) {
        return td.findByPlace(placeName);
    }

    @Override
    public List<Trip> findWithFilters(Map<String, Object> filterMap) {
        return td.findWithFilters(filterMap);
    }

    @Override
    public List<TripComment> getTripComments(long tripId) {
        List<TripComment> comments = td.getTripComments(tripId);
        Collections.sort(comments);
        return comments;
    }

    @Override
    public List<User> getTripUsers(long tripId) {
        return this.td.getTripUsers(tripId);
    }

    @Override
    public List<User> getTripAdmins(long tripId) {
        return td.getTripAdmins(tripId);
    }

    @Override
    public boolean createJoinRequest(Trip trip, User user, String token) {
        TripPendingConfirmation pendingConfirmation = td.createPendingConfirmation(trip, user, token);
        return pendingConfirmation != null;
    }

    @Override
    public List<TripPendingConfirmation> getTripJoinRequests(long tripId) {
        return td.getTripJoinRequests(tripId);
    }

    @Override
    public boolean updateJoinRequest(Trip trip, User loggedUser, String token, boolean accepted, User requester) {
        boolean edited = td.editJoinRequest(trip, loggedUser, token, accepted);
        if (edited) {
            addUserToTrip(requester.getId(), trip.getId());
            mailService.sendEditedJoinRequestMail(trip, requester, loggedUser, accepted);
            return true;
        }
        return false;
    }

    @Override
    public Optional<TripPendingConfirmation> findJoinRequestByToken(String token) {
        return td.findJoinRequestByToken(token);
    }

    @Override
    public TripInvitation inviteUserToTrip(Trip trip, User invitedUser, User admin) {
        String token = RandomStringUtils.random(64, true, true);
        TripInvitation invitation = td.createTripInvitation(trip, invitedUser, admin, token);
        mailService.sendTripInviteMail(trip, invitedUser, admin, token);
        return invitation;
    }

    @Override
    public Boolean isWaitingJoinTripConfirmation(Trip trip, User user) {
        Optional<TripPendingConfirmation> tripPendingConfirmationOptional = td.findTripConfirmationByUser(trip, user);
        return tripPendingConfirmationOptional.isPresent();
    }

    @Override
    public Optional<TripInvitation> findTripInvitationByToken(String token) {
        return td.findTripInvitationByToken(token);
    }

    @Override
    public void acceptOrRejectTripInvitation(String token, boolean accepted, User invitedUser, Trip trip) {
        addUserToTrip(invitedUser.getId(), trip.getId());
        td.deleteTripInvitation(token, trip);
    }

    @Override
    public Optional<TripInvitation> findTripInvitationByUser(Trip trip, User user) {
        return td.findTripInvitationByUser(trip, user);
    }

    @Override
    public void editTripImage(Trip trip, byte[] imageBytes) {
        if (tripPicturesService.findByTripId(trip.getId()).isPresent()) {
            tripPicturesService.deleteByTripId(trip.getId());
        }
        TripPicture picture = tripPicturesService.create(trip, imageBytes);
    }

    @Override
    public void editTripData(String tripName, String description, long tripId) {
        td.updateTripData(tripName, description, tripId);
    }

    @Override
    public void grantAdminRole(Trip trip, User invitedUser) {
        // todo: check if this works!!!
        trip.getAdmins().add(invitedUser);
    }

    @Override
    public boolean isAdmin(Trip trip, User user) {
        return trip.getAdminId() == user.getId() || trip.getAdmins().contains(user);
    }

    @Override
    public boolean isMember(Trip trip, User user) {
        return isAdmin(trip, user) || trip.getUsers().contains(user);
    }

}
