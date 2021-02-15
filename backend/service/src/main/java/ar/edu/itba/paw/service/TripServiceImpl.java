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
import java.util.stream.Collectors;

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

    @Autowired
    private UserRatesService userRatesService;

    @Override
    public Trip create(long userId, double latitude, double longitude, String name, String description,
                       LocalDate startDate, LocalDate endDate, boolean isPrivate, String googlePlaceId, String placeInput) throws GooglePlacesException {

        Place startPlace;
        List<se.walkercrou.places.Place> googleMapsPlaces = googleMapsService.queryGoogleMapsPlaces(latitude, longitude);
        startPlace = googleMapsService.createGooglePlaceReference(googleMapsPlaces, placeInput, latitude, longitude);
        Optional<User> u = ud.findById(userId);
        Place finalStartPlace = startPlace;
        return u.map(user -> td.create(user, finalStartPlace, name, description, startDate, endDate, isPrivate)).orElse(null);
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
    public List<Trip> getUserTrips(User user) {
        return user.getTrips().stream().map(TripMember::getTrip).distinct().collect(Collectors.toList());
    }

    @Override
    public void markTripAsCompleted(long tripId) {
        Optional<Trip> tripOptional = findById(tripId);
        if (!tripOptional.isPresent()) {
            return;
        }
        Trip trip = tripOptional.get();
        td.markTripAsCompleted(tripId);
        Set<TripMember> tripMembers = trip.getMembers();
        // create bidirectional rates for trip members
        for (TripMember member : tripMembers) {
            for (TripMember member2 : tripMembers) {
                if (!member.equals(member2)) {
                    userRatesService.createRate(trip, member.getUser(), member2.getUser());
                }
            }
        }
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
    public void addActivityToTrip(long activityId, long tripId) {
        Optional<Trip> ot = td.findById(tripId);
        Optional<Activity> oa = ad.findById(activityId);
        if (ot.isPresent() && oa.isPresent()) {
            ot.get().getActivities().add(oa.get());
        }
    }

    @Override
    public void addUserToTrip(long userId, long tripId) {
        Optional<User> userOptional = ud.findById(userId);
        Optional<Trip> tripOptional = td.findById(tripId);
        if (userOptional.isPresent() && tripOptional.isPresent()) {
            Trip trip = tripOptional.get();
            User user = userOptional.get();
            TripMember tripMember = createTripMember(trip, user, TripMemberRole.MEMBER);
            mailService.sendJoinTripMail(user, trip);
            trip.getMembers().add(tripMember);
            user.getTrips().add(tripMember);
        }
    }

    private TripMember createTripMember(Trip trip, User user, TripMemberRole role) {
        return td.createTripMember(trip, user, role);
    }

    @Override
    public void removeUserFromTrip(User loggedUser, Trip trip) {
        td.deleteTripMember(loggedUser.getId(), trip.getId());
        mailService.sendExitTripMail(loggedUser, trip);
    }

    @Override
    public void deleteTrip(Trip trip) {
        mailService.sendDeleteTripMail(trip);
        tpd.deleteByTripId(trip.getId());
        td.deleteTripInvitations(trip.getId());
        ad.deleteActivities(trip.getId());
        tcd.deleteComments(trip.getId());
        td.deleteAllTripMembers(trip.getId());
        td.deleteTrip(trip.getId());
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
    public boolean createJoinRequest(Trip trip, User user, String token) {
        TripPendingConfirmation pendingConfirmation = td.createPendingConfirmation(trip, user, token);
        if (pendingConfirmation != null)
            mailService.sendJoinRequestMail(trip, user, token);
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
        tripPicturesService.create(trip, imageBytes);
    }

    @Override
    public void editTripData(String tripName, String description, long tripId) {
        td.updateTripData(tripName, description, tripId);
    }

    @Override
    public void grantAdminRole(long tripId, long invitedUserId) {
        td.updateRoleToAdmin(tripId, invitedUserId);
    }

    @Override
    public boolean traveledTogether(Trip trip, User ratedUser, User ratedBy) {
        return isMember(trip, ratedUser) && isMember(trip, ratedBy);
    }

    @Override
    public boolean isCreator(Trip trip, User user) {
        return trip.getMembers().stream().anyMatch(member -> member.getUser().equals(user) && member.getRole().equals(TripMemberRole.CREATOR));
    }

    @Override
    public boolean isAdmin(Trip trip, User user) {
        return trip.getMembers().stream().anyMatch(member -> member.getUser().equals(user) && (
                member.getRole().equals(TripMemberRole.ADMIN) || member.getRole().equals(TripMemberRole.CREATOR)));
    }

    @Override
    public boolean isMember(Trip trip, User user) {
        return trip.getMembers().stream().anyMatch(member -> member.getUser().equals(user));
    }

}
