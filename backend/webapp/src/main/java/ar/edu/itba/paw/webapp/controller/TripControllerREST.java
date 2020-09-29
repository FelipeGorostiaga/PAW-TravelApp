package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationDTO;
import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationsDTO;
import ar.edu.itba.paw.webapp.form.ActivityCreateForm;
import ar.edu.itba.paw.webapp.form.EditTripForm;
import ar.edu.itba.paw.webapp.form.TripCommentForm;
import ar.edu.itba.paw.webapp.form.TripCreateForm;
import ar.edu.itba.paw.webapp.utils.ImageValidator;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Path("trips")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class TripControllerREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripControllerREST.class);

    private static final String ADD = "Add";
    private static final String DELETE = "Delete";

    private static final int TRIPS_PER_PAGE = 9;

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
    @Path("/{id}/admins")
    public Response getTripAdmins(@PathParam("id") final long tripId) {
        Optional<Trip> trip = tripService.findById(tripId);
        if(!trip.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<UserDTO> users = this.tripService.getTripAdmins(tripId).stream().map(UserDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<UserDTO>>(users) {}).build();
    }

    @GET
    @Path("/{id}/users")
    public Response getTripUsers(@PathParam("id") final long tripId) {
        Optional<Trip> trip = tripService.findById(tripId);
        if(!trip.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<UserDTO> users = this.tripService.getTripUsers(tripId).stream().map(UserDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<UserDTO>>(users) {}).build();
    }

    @GET
    @Path("/{id}")
    public Response getTrip(@PathParam("id") final long tripId) {
        Optional<Trip> trip = tripService.findById(tripId);
        if(!trip.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new TripDTO(trip.get())).build();
    }

    @GET
    @Path("/all/{page}")
    public Response getAllTripsForPage(@PathParam("page") final int pageNum) {
        List<TripDTO> trips = tripService.getAllTripsPerPage(pageNum).stream().map(TripDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TripDTO>>(trips) {}).build();
    }

    @GET
    @Path("/all")
    public Response getAllTrips() {
        List<TripDTO> trips = tripService.getAllTrips().stream()
                                        .filter(trip -> !trip.isPrivate())
                                        .map(TripDTO::new)
                                        .collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TripDTO>>(trips) {}).build();
    }

    @PUT
    @Path("/{id}/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editTrip(@Valid final EditTripForm form, @PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        User loggedUser = securityUserService.getLoggedUser();
        if(!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        Trip trip = tripOptional.get();
        if(loggedUser.getId() != trip.getAdminId()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        Set<ConstraintViolation<EditTripForm>> violations = validator.validate(form);
        ConstraintViolationsDTO constraintViolationsDTO = new ConstraintViolationsDTO(violations);
        byte[] imageBytes;
        try {
            imageBytes = ImageValidator.validateImage(constraintViolationsDTO,  form.getImageUpload());
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorDTO("Server couldn't get image bytes"))
                    .build();
        }
        if(constraintViolationsDTO.getErrors().size() > 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(constraintViolationsDTO).build();
        }
        if(tripPicturesService.findByTripId(tripId).isPresent()) {
            tripPicturesService.deleteByTripId(tripId);
        }
        TripPicture picture = tripPicturesService.create(trip, imageBytes);
        trip.setProfilePicture(picture);
        return Response.ok(new TripDTO(trip)).build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrip(@Valid TripCreateForm tripCreateForm) {
        User loggedUser = securityUserService.getLoggedUser();
        Set<ConstraintViolation<TripCreateForm>> violations = validator.validate(tripCreateForm);
        ConstraintViolationsDTO violationsDTO = new ConstraintViolationsDTO(violations);
        List<Place> places = null;
        try {
            places = googleClient.getPlacesByQuery(tripCreateForm.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
        }
        catch(GooglePlacesException gpe) {
            LOGGER.debug("Invalid google maps query location");
            violationsDTO.add(new ConstraintViolationDTO("Invalid google maps location", "mapInput"));
        }
        if(violationsDTO.getErrors().size() > 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(violationsDTO).build();
        }
        ar.edu.itba.paw.model.Place customPlace = createGooglePlaceReference(places);
        if(customPlace == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        Trip trip = tripService.create(loggedUser.getId(), customPlace.getId(), tripCreateForm.getName(),
                tripCreateForm.getDescription(), DateManipulation.stringToLocalDate(tripCreateForm.getStartDate()),
                DateManipulation.stringToLocalDate(tripCreateForm.getEndDate()), tripCreateForm.isPrivate());
        if(trip != null) {
            return Response.ok(new TripDTO(trip)).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("/{id}/places")
    public Response getTripPlaces(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            List<PlaceDTO> places = tripService.findTripPlaces(tripOptional.get()).stream().map(PlaceDTO::new).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<PlaceDTO>>(places) {}).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}/delete")
    public Response deleteTrip(@PathParam("id") final long tripId) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            if(loggedUser.getId() == tripOptional.get().getAdminId()) {
                tripService.deleteTrip(tripId);
                return Response.ok().build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/add/{userId}")
    public Response addUserToTrip(@PathParam("id") final long tripId, @PathParam("userId") final long userId) {
        return addOrDeleteUserFromTrip(tripId, userId, ADD);
    }

    @PUT
    @Path("/{id}/remove/{userId}")
    public Response removeUserFromTrip(@PathParam("id") final long tripId, @PathParam("userId") final long userId) {
        return addOrDeleteUserFromTrip(tripId, userId, DELETE);
    }

    private Response addOrDeleteUserFromTrip(final long tripId, final long userId, final String type) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<User> userOptional = userService.findById(userId);
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(userOptional.isPresent() && tripOptional.isPresent()) {
            User u = userOptional.get();
            Trip t = tripOptional.get();
            if(t.getAdminId() == loggedUser.getId()) {
                switch (type) {
                    case ADD:
                        tripService.addUserToTrip(userId, tripId);
                        mailingService.sendJoinTripMail(u.getEmail(), u.getFirstname(), t.getName(), loggedUser.getFirstname(),
                                loggedUser.getLastname(), getLocale());
                        break;
                    case DELETE:
                        tripService.removeUserFromTrip(userId, tripId);
                        mailingService.sendExitTripMail(u.getEmail(), u.getFirstname(), t.getName(), loggedUser.getFirstname(),
                                loggedUser.getLastname(), getLocale());
                        break;
                }
                return Response.ok().build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
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
        User loggedUser = securityUserService.getLoggedUser();
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            Trip t = tripOptional.get();
            if(t.getUsers().contains(loggedUser)) {
                List<TripCommentDTO> comments = tripService.getTripComments(tripId).stream().map(TripCommentDTO::new).collect(Collectors.toList());
                return Response.ok(new GenericEntity<List<TripCommentDTO>>(comments){}).build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/comments/add/{userId}")
    public Response addCommentToTripChat(@PathParam("id") final long tripId, @Valid TripCommentForm tripCommentForm) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<Trip> tripOptional = tripService.findById(tripId);
        Set<ConstraintViolation<TripCommentForm>> violations = validator.validate(tripCommentForm);
        if(!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ConstraintViolationsDTO(violations)).build();
        }
        if(tripOptional.isPresent()) {
            Trip t = tripOptional.get();
            if(t.getUsers().contains(loggedUser) || t.getAdminId() == loggedUser.getId()) {
                TripComment tripComment = tripCommentsService.create(loggedUser, t, tripCommentForm.getComment());
                tripService.addCommentToTrip(tripComment.getId(), tripId);
                return Response.ok(new TripCommentDTO(tripComment)).build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/activities")
    public Response getTripActivities(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            List<ActivityDTO> activities = activityService.getTripActivities(tripId).stream().map(ActivityDTO::new).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<ActivityDTO>>(activities){}).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private ar.edu.itba.paw.model.Place createGooglePlaceReference(List<Place> googlePlaces) {
        if(googlePlaces != null && !googlePlaces.isEmpty()) {
            Place googlePlace = googlePlaces.get(0);
            LOGGER.debug("Google Place name is {}", googlePlace.getName());
            Optional<ar.edu.itba.paw.model.Place> maybePlace = placeService.findByGoogleId(googlePlace.getPlaceId());
            return maybePlace.orElseGet(() -> placeService
                    .create(googlePlace.getPlaceId(), googlePlace.getName(), googlePlace.getLatitude(),
                    googlePlace.getLongitude(), googlePlace.getAddress()));
        }
        return null;
    }

    @POST
    @Path("/{id}/activities/create")
    public Response createTripActivity(@PathParam("id") final long tripId, @Valid ActivityCreateForm activityCreateForm) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        User loggedUser = securityUserService.getLoggedUser();
        if(!tripOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Trip trip = tripOptional.get();
        if(loggedUser.getId() == trip.getAdminId()) {
            activityCreateForm.setTrip(tripOptional.get());
            Set<ConstraintViolation<ActivityCreateForm>> violations = validator.validate(activityCreateForm);
            ConstraintViolationsDTO violationsDTO = new ConstraintViolationsDTO(violations);
            List<Place> googlePlaces = null;
            try {
                googlePlaces = googleClient.getPlacesByQuery(activityCreateForm.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
            }
            catch(GooglePlacesException gpe) {
                violationsDTO.add(new ConstraintViolationDTO("Invalid maps location", "mapInput"));
            }
            if(violationsDTO.getErrors().size() > 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity(violationsDTO).build();
            }
            ar.edu.itba.paw.model.Place customPlace = createGooglePlaceReference(googlePlaces);
            if(customPlace == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            Activity activity = activityService.create(activityCreateForm.getName(), activityCreateForm.getCategory(), customPlace,
                    trip, DateManipulation.stringToLocalDate(activityCreateForm.getStartDate()),
                    DateManipulation.stringToLocalDate(activityCreateForm.getEndDate()));
            tripService.addActivityToTrip(activity.getId(), tripId);
            return Response.ok(new ActivityDTO(activity)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @DELETE
    @Path("/{id}/activities/delete/{activityId}")
    public Response deleteTripActivity(@PathParam("id") final long tripId, @PathParam("activityId") final long activityId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        User loggedUser = securityUserService.getLoggedUser();
        if(tripOptional.isPresent()) {
            Trip trip = tripOptional.get();
            if(trip.getAdminId() == loggedUser.getId()) {
                tripService.deleteTripActivity(activityId, tripId);
                return Response.ok().build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
