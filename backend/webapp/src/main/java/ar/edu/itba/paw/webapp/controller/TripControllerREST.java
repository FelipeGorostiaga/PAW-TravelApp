package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.constraints.ConstraintViolationsDTO;
import ar.edu.itba.paw.webapp.form.ActivityCreateForm;
import ar.edu.itba.paw.webapp.form.TripCommentForm;
import ar.edu.itba.paw.webapp.form.TripCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Path("trips")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class TripControllerREST {

    // TODO - SWITCH TO DTOS

    private static final Logger LOGGER = LoggerFactory.getLogger(TripControllerREST.class);
    private static final String ADD = "Add";
    private static final String DELETE = "Delete";

    @Autowired
    SecurityUserService securityUserService;

    @Autowired
    TripService tripService;

    @Autowired
    GooglePlaces googleClient;

    @Autowired
    PlaceService placeService;

    @Autowired
    UserService userService;

    @Autowired
    MailingService mailingService;

    @Autowired
    TripPicturesService tripPicturesService;

    @Autowired
    TripCommentsService tripCommentsService;

    @Autowired
    ActivityService activityService;

    @Autowired
    Validator validator;

    @GET
    @Path("/all")
    public Response getAllTrips(@DefaultValue("1") @QueryParam("page") int pageNum) {
        List<Trip> trips = tripService.getAllTrips(pageNum);
        return Response.ok(trips.stream().map(TripDTO::new).collect(Collectors.toList())).build();
    }

    @POST
    @Path("/{id}/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrip(@Valid TripCreateForm tripCreateForm, @PathParam("id") final long userId) {
        Optional<User> userOpt = userService.findById(userId);
        if(!userOpt.isPresent()) {
            LOGGER.debug("Invalid userId");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Set<ConstraintViolation<TripCreateForm>> violations = validator.validate(tripCreateForm);
        if(!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ConstraintViolationsDTO(violations)).build();
        }
        List<Place> places;
        try {
            places = googleClient.getPlacesByQuery(tripCreateForm.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
        }
        catch(GooglePlacesException gpe) {
            LOGGER.debug("Invalid google maps query location");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Place place = places.get(0);
        LOGGER.debug("Google Place name is {}", place.getName());
        Optional<ar.edu.itba.paw.model.Place> maybePlace = placeService.findByGoogleId(place.getPlaceId());

        ar.edu.itba.paw.model.Place dbPlace = maybePlace.orElseGet(() -> placeService
                .create(place.getPlaceId(), place.getName(), place.getLatitude(), place.getLongitude(), place.getAddress()));
        Trip trip = tripService.create(userOpt.get().getId(), dbPlace.getId(), tripCreateForm.getName(),
                tripCreateForm.getDescription(), DateManipulation.stringToLocalDate(tripCreateForm.getStartDate()),
                DateManipulation.stringToLocalDate(tripCreateForm.getEndDate()));
        return Response.ok(new TripDTO(trip)).build();
    }

    @GET
    @Path("/{id}")
    public Response getTrip(@PathParam("id") final long tripId) {
        Optional<Trip> trip = tripService.findById(tripId);
        if(trip.isPresent()) {
            return Response.ok(new TripDTO(trip.get())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/places")
    public Response getTripPlaces(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            List<ar.edu.itba.paw.model.Place> places = tripService.findTripPlaces(tripOptional.get());
            return Response.ok(places.stream().map(PlaceDTO::new).collect(Collectors.toList())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}/delete")
    public Response deleteTrip(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            tripService.deleteTrip(tripId);
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/rates")
    public Response getTripRates(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            List<TripRate> tripRates = tripService.getTripRates(tripId);
            return Response.ok(tripRates.stream().map(RateDTO::new).collect(Collectors.toList())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/add/{userId}")
    public Response addUserToTrip(@PathParam("id") final long tripId, @PathParam("userId") final long userId) {
        return addOrDeleteUserFromTrip(tripId, userId, ADD);
    }

    private Response addOrDeleteUserFromTrip(final long tripId, final long userId, final String type) {
        Optional<User> userOptional = userService.findById(userId);
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(userOptional.isPresent() && tripOptional.isPresent()) {
            User u = userOptional.get();
            Trip t = tripOptional.get();
            Optional<User> tripAdmin = userService.findById(t.getAdminId());
            if(tripAdmin.isPresent()) {
                User admin = tripAdmin.get();
                switch (type) {
                    case ADD:
                        tripService.addUserToTrip(userId, tripId);
                        mailingService.sendJoinTripMail(u.getEmail(), u.getFirstname(), t.getName(), admin.getFirstname(),
                                admin.getLastname(), getLocale());
                        break;
                    case DELETE:
                        tripService.removeUserFromTrip(userId, tripId);
                        mailingService.sendExitTripMail(u.getEmail(), u.getFirstname(), t.getName(), admin.getFirstname(),
                                admin.getLastname(), getLocale());
                        break;
                }
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}/remove/{userId}")
    public Response removeUserFromTrip(@PathParam("id") final long tripId,
                                       @PathParam("userId") final long userId) {
        return addOrDeleteUserFromTrip(tripId, userId, DELETE);
    }

    @GET
    @Path("/{id}/image")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getTripImage(@PathParam("id") final long tripId) {
        final Optional<TripPicture> pictureOpt = tripPicturesService.findByTripId(tripId);
        if (!pictureOpt.isPresent()) {
            LOGGER.warn("Cannot render trip picture, trip with id {} not found", tripId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new ImageDTO(pictureOpt.get())).build();
    }

    @GET
    @Path("/{id}/comments")
    public Response getTripComments(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            return Response.ok(tripService.getTripComments(tripId)
                    .stream()
                    .map(TripCommentDTO::new)
                    .collect(Collectors.toList())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/comments/add/{userId}")
    public Response addCommentToTripChat(@PathParam("id") final long tripId, @PathParam("userId") final long userId,
                                         @Valid TripCommentForm tripCommentForm) {
        Optional<User> userOptional = userService.findById(userId);
        Optional<Trip> tripOptional = tripService.findById(tripId);
        Set<ConstraintViolation<TripCommentForm>> violations = validator.validate(tripCommentForm);
        if(!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ConstraintViolationsDTO(violations)).build();
        }
        if(userOptional.isPresent() && tripOptional.isPresent()) {
            TripComment tripComment = tripCommentsService.create(userOptional.get(), tripOptional.get(), tripCommentForm.getComment());
            tripService.addCommentToTrip(tripComment.getId(), tripId);
            return Response.ok(new TripCommentDTO(tripComment)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/activities")
    public Response getTripActivities(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            List<Activity> activities = activityService.getTripActivities(tripId);
            return Response.ok(activities.stream().map(ActivityDTO::new).collect(Collectors.toList())).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/activities/create")
    public Response createTripActivity(@PathParam("id") final long tripId, @Valid ActivityCreateForm activityCreateForm) {
        Set<ConstraintViolation<ActivityCreateForm>> violations = validator.validate(activityCreateForm);
        if(!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ConstraintViolationsDTO(violations)).build();
        }

        // TODO
        // form.checkDates(trip.getStartDate(), trip.getEndDate()
        // form.checkTimeline(trip.getActivities())

        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(!tripOptional.isPresent()) return Response.status(Response.Status.BAD_REQUEST).build();
        ar.edu.itba.paw.model.Place modelPlace;
        List<Place> googlePlaces;
        try {
            googlePlaces = googleClient.getPlacesByQuery(activityCreateForm.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
        }
        catch(GooglePlacesException gpe) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Place googlePlace = googlePlaces.get(0);
        Optional<ar.edu.itba.paw.model.Place> maybePlace = placeService.findByGoogleId(googlePlace.getPlaceId());
        modelPlace = maybePlace.orElseGet(() -> placeService.create(googlePlace.getPlaceId(), googlePlace.getName(), googlePlace.getLatitude(),
                googlePlace.getLongitude(), googlePlace.getAddress()));
        Activity activity = activityService.create(activityCreateForm.getName(), activityCreateForm.getCategory(), modelPlace,
                tripOptional.get(), DateManipulation.stringToLocalDate(activityCreateForm.getStartDate()),
                DateManipulation.stringToLocalDate(activityCreateForm.getEndDate()));
        tripService.addActivityToTrip(activity.getId(), tripId);
        return Response.ok(new ActivityDTO(activity)).build();
    }

    @DELETE
    @Path("/{id}/activities/delete/{activityId}")
    public Response deleteTripActivity(@PathParam("id") final long tripId, @PathParam("activityId") final long activityId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            if(tripOptional.get().getAdminId() != securityUserService.getLoggedUser().getId()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        tripService.deleteTripActivity(activityId, tripId);
        return Response.ok().build();
    }

}
