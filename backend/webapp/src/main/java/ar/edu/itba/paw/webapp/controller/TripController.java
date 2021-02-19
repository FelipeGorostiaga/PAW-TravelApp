package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.ActivityCreateForm;
import ar.edu.itba.paw.webapp.form.TripCommentForm;
import ar.edu.itba.paw.webapp.form.TripCreateForm;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.exception.GooglePlacesException;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("trips")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class TripController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripController.class);

    private static final int TRIP_IMAGE_WIDTH = 480;
    private static final int TRIP_IMAGE_HEIGHT = 360;

    private static final int TRIP_CARD_IMAGE_WIDTH = 478;
    private static final int TRIP_CARD_IMAGE_HEIGHT = 280;

    private static final int PAGE_SIZE = 4;

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
    MailingService mailService;

    @Autowired
    TripPicturesService tripPicturesService;

    @Autowired
    TripCommentsService tripCommentsService;

    @Autowired
    ActivityService activityService;

    @Autowired
    Validator validator;

    @GET
    @Path("/{id}")
    public Response getTrip(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(new FullTripDTO(tripOptional.get())).build();
    }


    @GET
    @Path("/")
    public Response getAllTripsForPage(@DefaultValue("1") @QueryParam("page") int page) {
        page = (page < 1) ? 1 : page;
        final int totalPublicTrips = this.tripService.countAllPublicTrips();
        final int maxPage = (int) (Math.ceil((float) totalPublicTrips / PAGE_SIZE));;
        if (page > maxPage) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<TripDTO> trips = tripService.getAllTripsPerPage(page).stream().map(TripDTO::new).collect(Collectors.toList());
        return Response.ok(new TripListDTO(trips, totalPublicTrips, maxPage)).build();
    }


    @POST
    @Path("/{id}/edit")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response editTrip(@FormDataParam("tripName") FormDataBodyPart name,
                             @FormDataParam("description") FormDataBodyPart description,
                             @FormDataParam("image") File imageFile,
                             @FormDataParam("image") FormDataContentDisposition fileMetaData,
                             @PathParam("id") final int id) {

        Optional<Trip> tripOptional = tripService.findById(id);
        User loggedUser = securityUserService.getLoggedUser();
        if (!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        Trip trip = tripOptional.get();

        if (!tripService.isCreator(trip, loggedUser)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (trip.getStatus().equals(TripStatus.COMPLETED)) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        if (imageFile != null) {
            byte[] imageBytes;
            try {
                imageBytes = FileUtils.readFileToByteArray(imageFile);
            } catch (IOException e) {
                return Response.serverError().build();
            }
            if (!ImageUtils.validateImage(fileMetaData, imageBytes.length)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("Invalid image extension or file size too big", "image")).build();
            }
            byte[] resizedImage;
            try {
                resizedImage = ImageUtils.resizeToProfileSize(imageBytes, TRIP_IMAGE_WIDTH, TRIP_IMAGE_HEIGHT);
            } catch (IOException e) {
                return Response.serverError().build();
            }
            tripService.editTripImage(trip, resizedImage);
            imageFile.delete();
        }
        tripService.editTripData(name.getValue(), description.getValue(), trip.getId());
        return Response.ok().build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrip(@Valid TripCreateForm tripCreateForm) {
        User loggedUser = securityUserService.getLoggedUser();
        Set<ConstraintViolation<TripCreateForm>> violations = validator.validate(tripCreateForm);
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        if (!violations.isEmpty()) {
            errorDTOS.addAll(violations.stream().map(violation -> new ErrorDTO(violation.getMessage(), violation.getInvalidValue().toString())).collect(Collectors.toList()));
        }
        if (!tripCreateForm.validateDates()) {
            errorDTOS.add(new ErrorDTO("Invalid dates, start date must be before end date", "dates"));
        }
        if (!errorDTOS.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<List<ErrorDTO>>(errorDTOS) {
            }).build();
        Trip trip;
        try {
            trip = tripService.create(loggedUser.getId(), tripCreateForm.getLatitude(), tripCreateForm.getLongitude(), tripCreateForm.getName(),
                    tripCreateForm.getDescription(), DateManipulation.stringToLocalDate(tripCreateForm.getStartDate()),
                    DateManipulation.stringToLocalDate(tripCreateForm.getEndDate()), tripCreateForm.isPrivate(), tripCreateForm.getGooglePlaceId(), tripCreateForm.getPlaceInput());
        } catch (GooglePlacesException exception) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorDTO("There was an error connecting to the GoogleMaps API", "googleMaps")).build();
        }
        if (trip != null) {
            return Response.ok(new TripDTO(trip)).build();
        }
        return Response.serverError().build();
    }

    @GET
    @Path("/{id}/places")
    public Response getTripPlaces(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (tripOptional.isPresent()) {
            List<PlaceDTO> places = tripService.findTripPlaces(tripOptional.get()).stream().map(PlaceDTO::new).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<PlaceDTO>>(places) {
            }).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTrip(@PathParam("id") final long tripId) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (tripOptional.isPresent()) {
            if (tripOptional.get().getStatus().equals(TripStatus.COMPLETED))
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            if (tripService.isCreator(tripOptional.get(), loggedUser)) {
                tripService.deleteTrip(tripOptional.get());
                return Response.ok().build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @POST
    @Path("/{id}/exit")
    public Response exitTrip(@PathParam("id") final long tripId) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        Trip trip = tripOptional.get();
        if (tripService.isMember(trip, loggedUser)) {
            tripService.removeUserFromTrip(loggedUser, trip);
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("User is not part of this trip", "invalid-user")).build();
    }

    @GET
    @Path("/{id}/image")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getTripImage(@PathParam("id") final long tripId) {
        final Optional<TripPicture> tripPictureOptional = tripPicturesService.findByTripId(tripId);
        if (!tripPictureOptional.isPresent()) {
            LOGGER.warn("Cannot render trip picture, trip picture for id {} not found", tripId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        byte[] resizedImage;
        try {
            resizedImage = ImageUtils.resizeToProfileSize(tripPictureOptional.get().getPicture(), TRIP_IMAGE_WIDTH, TRIP_IMAGE_HEIGHT);

        } catch (IOException e) {
            return Response.serverError().build();
        }
        return Response.ok(resizedImage).build();
    }

    @GET
    @Path("/{id}/image/card")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getTripCardImage(@PathParam("id") final long tripId) {
        final Optional<TripPicture> tripPictureOptional = tripPicturesService.findByTripId(tripId);
        if (!tripPictureOptional.isPresent()) {
            LOGGER.warn("Cannot render trip picture, trip picture for id {} not found", tripId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        byte[] resizedImage;
        try {
            resizedImage = ImageUtils.resizeToProfileSize(tripPictureOptional.get().getPicture(), TRIP_CARD_IMAGE_WIDTH, TRIP_CARD_IMAGE_HEIGHT);

        } catch (IOException e) {
            return Response.serverError().build();
        }
        return Response.ok(resizedImage).build();
    }


    @GET
    @Path("/{id}/comments")
    public Response getTripComments(@PathParam("id") final long tripId) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        Trip t = tripOptional.get();
        if (!tripService.isMember(t, loggedUser)) return Response.status(Response.Status.FORBIDDEN).build();
        List<TripCommentDTO> comments = tripService.getTripComments(tripId).stream().map(TripCommentDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TripCommentDTO>>(comments) {
        }).build();
    }


    @POST
    @Path("/{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCommentToTripChat(@PathParam("id") final long tripId, @Valid TripCommentForm tripCommentForm) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        Trip trip = tripOptional.get();
        if (trip.getStatus().equals(TripStatus.COMPLETED))
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        Set<ConstraintViolation<TripCommentForm>> violations = validator.validate(tripCommentForm);
        if (!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<Set<ConstraintViolation<TripCommentForm>>>(violations) {
            }).build();
        }
        if (tripService.isMember(trip, loggedUser)) {
            TripComment tripComment = tripCommentsService.create(loggedUser, trip, tripCommentForm.getComment());
            return Response.ok(new TripCommentDTO(tripComment)).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Path("/{id}/activities")
    public Response getTripActivities(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (tripOptional.isPresent()) {
            List<ActivityDTO> activities = activityService.getTripActivities(tripId).stream().map(ActivityDTO::new).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<ActivityDTO>>(activities) {
            }).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @POST
    @Path("/{id}/activities")
    public Response createTripActivity(@PathParam("id") final long tripId, @Valid ActivityCreateForm activityCreateForm) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        User loggedUser = securityUserService.getLoggedUser();
        if (!tripOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Trip trip = tripOptional.get();
        if (!tripService.isAdmin(trip, loggedUser)) return Response.status(Response.Status.FORBIDDEN).build();
        if (trip.getStatus().equals(TripStatus.COMPLETED))
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        activityCreateForm.setTrip(trip);
        Set<ConstraintViolation<ActivityCreateForm>> violations = validator.validate(activityCreateForm);
        if (!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<Set<ConstraintViolation<ActivityCreateForm>>>(violations) {
            }).build();
        }
        Activity activity = activityService.create(activityCreateForm.getName(), activityCreateForm.getCategory(), activityCreateForm.getLatitude(),
                activityCreateForm.getLongitude(), trip, DateManipulation.stringToLocalDate(activityCreateForm.getStartDate()),
                DateManipulation.stringToLocalDate(activityCreateForm.getEndDate()), activityCreateForm.getDescription(), activityCreateForm.getPlaceInput());
        return Response.ok(new ActivityDTO(activity)).build();
    }

    @DELETE
    @Path("/{id}/activities/{activityId}")
    public Response deleteTripActivity(@PathParam("id") final long tripId, @PathParam("activityId") final long activityId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        User loggedUser = securityUserService.getLoggedUser();
        if (tripOptional.isPresent()) {
            Trip trip = tripOptional.get();
            if (trip.getStatus().equals(TripStatus.COMPLETED))
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
            if (tripService.isAdmin(trip, loggedUser)) {
                tripService.deleteTripActivity(activityId, tripId);
                return Response.ok().build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{id}/request-invite")
    public Response requestJoinTrip(@PathParam("id") final long tripId) {
        Optional<Trip> tripOpt = tripService.findById(tripId);
        User user = securityUserService.getLoggedUser();
        if (!tripOpt.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        Trip t = tripOpt.get();
        if (tripService.isMember(t, user)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("User is already part of the trip", "error")).build();
        }
        if (t.getStatus().equals(TripStatus.COMPLETED)) return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        List<TripPendingConfirmation> pendingConfirmations = tripService.getTripJoinRequests(t.getId())
                .stream()
                .filter(pc -> pc.getRequestingUser().getId() == user.getId())
                .collect(Collectors.toList());
        if (!pendingConfirmations.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("Cannot send multiple join requests", "repeated")).build();
        String token = RandomStringUtils.random(64, true, true);
        if (tripService.createJoinRequest(t, user, token)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDTO("Error creating request, please try again...", "error")).build();
    }

    @POST
    @Path("/{id}/invitation")
    public Response acceptOrDenyTripPendingConfirmation(@PathParam("id") final long tripId, @QueryParam("accepted") boolean accepted, @QueryParam("token") String token) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<Trip> tripOptional = tripService.findById(tripId);
        Optional<TripPendingConfirmation> pendingConfirmationOptional = tripService.findJoinRequestByToken(token);
        if (!pendingConfirmationOptional.isPresent() || !tripOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (tripOptional.get().getStatus().equals(TripStatus.COMPLETED))
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        if (!tripService.isAdmin(tripOptional.get(), loggedUser)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (pendingConfirmationOptional.get().isEdited())
            return Response.status(Response.Status.CONFLICT).entity(new ErrorDTO("Request already responded by another trip admin", "edited")).build();
        if (tripService.updateJoinRequest(tripOptional.get(), loggedUser, token, accepted, pendingConfirmationOptional.get().getRequestingUser())) {
            return Response.ok().build();
        }
        return Response.serverError().build();
    }

    @GET
    @Path("/{id}/pendingConfirmations/user")
    public Response isWaitingTripConfirmation(@PathParam("id") final long tripId, @QueryParam("user") long userId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        Optional<User> userOptional = userService.findById(userId);
        if (!tripOptional.isPresent() || !userOptional.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).build();
        if (tripService.isWaitingJoinTripConfirmation(tripOptional.get(), userOptional.get())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}/pendingConfirmations")
    public Response getTripPendingConfirmations(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (!tripOptional.isPresent()) return Response.status(Response.Status.BAD_REQUEST).build();
        if (!tripService.isAdmin(tripOptional.get(), securityUserService.getLoggedUser()))
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorDTO("Only trip admins can get pending confirmations", "permission-denied"))
                    .build();
        List<TripPendingConfirmationDTO> pendingConfirmations = tripService.getTripJoinRequests(tripId).stream().map(TripPendingConfirmationDTO::new).collect(Collectors.toList());
        if (pendingConfirmations.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(new GenericEntity<List<TripPendingConfirmationDTO>>(pendingConfirmations) {
        }).build();
    }

    @POST
    @Path("/{id}/invite/{userId}")
    public Response inviteUserToTrip(@PathParam("id") final long tripId, @PathParam("userId") final long userId) {
        User loggedUser = securityUserService.getLoggedUser();
        Optional<User> userOptional = userService.findById(userId);
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (!tripOptional.isPresent() || !userOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        User invitedUser = userOptional.get();
        Trip trip = tripOptional.get();
        if (!tripService.isAdmin(trip, loggedUser)) return Response.status(Response.Status.FORBIDDEN)
                .entity(new ErrorDTO("You need to be an admin to invite users", "permission-denied")).build();
        if (trip.getStatus().equals(TripStatus.COMPLETED))
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        if (tripService.isMember(trip, invitedUser)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("User is already part of the trip", "isMember")).build();
        }
        Optional<TripInvitation> tripInvitation = tripService.findTripInvitationByUser(tripOptional.get(), userOptional.get());
        if (tripInvitation.isPresent()) return Response.status(Response.Status.CONFLICT).build();
        tripService.inviteUserToTrip(trip, invitedUser, loggedUser);
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/invite-request/response")
    public Response acceptOrDenyTripInvitation(@PathParam("id") final long tripId, @QueryParam("token") String token, @QueryParam("accepted") boolean accepted) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        User loggedUser = securityUserService.getLoggedUser();
        Optional<TripInvitation> tripInvitation = tripService.findTripInvitationByToken(token);
        if (!tripInvitation.isPresent() || !tripOptional.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();
        if (tripOptional.get().getStatus().equals(TripStatus.COMPLETED))
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        if (tripInvitation.get().getInvitee().getId() != loggedUser.getId())
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorDTO("Only the invitee can accept or reject this invitation", "permission-denied"))
                    .build();
        tripService.acceptOrRejectTripInvitation(token, accepted, loggedUser, tripOptional.get());
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/make-admin/{userId}")
    public Response grantAdminRole(@PathParam("id") final long tripId, @PathParam("userId") final long userId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        Optional<User> userOptional = userService.findById(userId);
        if (!tripOptional.isPresent() || !userOptional.isPresent())
            return Response.status(Response.Status.BAD_REQUEST).build();
        Trip trip = tripOptional.get();
        User invitedUser = userOptional.get();
        User loggedUser = securityUserService.getLoggedUser();
        if (tripOptional.get().getStatus().equals(TripStatus.COMPLETED))
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        if (!tripService.isAdmin(trip, loggedUser)) return Response.status(Response.Status.FORBIDDEN).build();
        if (!tripService.isMember(trip, invitedUser)) return Response.status(Response.Status.BAD_REQUEST).build();
        if (tripService.isAdmin(trip, invitedUser))
            return Response.status(Response.Status.CONFLICT).entity(new ErrorDTO("Selected user is already a trip administrator", "duplicate")).build();
        tripService.grantAdminRole(trip.getId(), invitedUser.getId());
        return Response.ok().build();
    }


    @POST
    @Path("/{id}/finish")
    public Response markTripAsCompleted(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        User loggedUser = securityUserService.getLoggedUser();
        if (!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        if (tripService.isCreator(tripOptional.get(), loggedUser)) {
            if (!tripOptional.get().getStatus().equals(TripStatus.COMPLETED)) {
                tripService.markTripAsCompleted(tripId);
            }
            return Response.ok().build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }

}
