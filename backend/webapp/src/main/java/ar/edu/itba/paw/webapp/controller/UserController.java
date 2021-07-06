package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserRatesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import ar.edu.itba.paw.webapp.form.UserRateForm;
import ar.edu.itba.paw.webapp.utils.DateManipulation;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import ar.edu.itba.paw.webapp.utils.PaginationLinkFactory;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Path("users")
@RestController
@Produces(value = {MediaType.APPLICATION_JSON})
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final int PROFILE_WIDTH = 220;
    private static final int PROFILE_HEIGHT = 200;
    private static final int PAGE_SIZE = 6;

    @Autowired
    private Validator validator;

    @Autowired
    private UserPicturesService userPicturesService;

    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Autowired
    private UserRatesService userRatesService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private PaginationLinkFactory paginationLinkFactory;

    @Context
    private UriInfo uriContext;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserCreateForm userForm) {
        Set<ConstraintViolation<UserCreateForm>> violations = validator.validate(userForm);
        if (violations.size() > 0) {
            List<ErrorDTO> errors = violations.stream().map(violation -> new ErrorDTO(violation.getMessage(),
                    violation.getPropertyPath().toString())).collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<List<ErrorDTO>>(errors) {
            }).build();
        }
        User user;
        try {
            user = userService.create(userForm.getFirstname(), userForm.getLastname(), userForm.getEmail(),
                    userForm.getPassword(), DateManipulation.stringToLocalDate(userForm.getBirthday()),
                    userForm.getNationality(), userForm.getSex());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("Email already in use", "email")).build();
        }
        LOGGER.debug("Created user with id {}", user.getId());
        return Response.ok(new UserDTO(user, uriContext.getBaseUri())).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") final int id) {
        final Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            LOGGER.debug("Requesting user with id {}", id);
            return Response.ok(new UserDTO(userOptional.get(), uriContext.getBaseUri())).build();
        } else {
            LOGGER.debug("User with id {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/image")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getUserImage(@PathParam("id") final int id) {
        final Optional<UserPicture> pictureOpt = userPicturesService.findByUserId(id);
        if (!pictureOpt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOGGER.debug("Requesting image for user with id {}", id);
        CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(50000);
        cacheControl.setPrivate(false);
        cacheControl.setMustRevalidate(true);
        return Response.ok(pictureOpt.get().getPicture()).cacheControl(cacheControl).build();
    }

    @GET
    @Path("/{id}/trips")
    public Response getUserTrips(@PathParam("id") final int id, @DefaultValue("1") @QueryParam("page") int page) {
        page = (page < 1) ? 1 : page;
        final Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User user = userOptional.get();
        LOGGER.debug("Requesting user trips for user with id {}", id);
        PaginatedResult<Trip> paginatedResult = tripService.getUserTrips(user, page);
        final int maxPage = (int) (Math.ceil((float) paginatedResult.getTotalTrips() / PAGE_SIZE));
        if (maxPage != 0 && page > maxPage) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        final Map<String, Link> links = paginationLinkFactory.createLinks(uriContext, page, maxPage);
        final Link[] linkArray = links.values().toArray(new Link[0]);
        final List<TripDTO> tripsDto = paginatedResult.getTrips()
                .stream()
                .map(trip -> new TripDTO(trip, uriContext.getBaseUri()))
                .collect(Collectors.toList());
        return Response.ok(new TripListDTO(tripsDto, paginatedResult.getTotalTrips(), maxPage)).links(linkArray).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response editProfile(@FormDataParam("biography") FormDataBodyPart biography,
                                @FormDataParam("image") File imageFile,
                                @FormDataParam("image") FormDataContentDisposition fileMetaData,
                                @PathParam("id") final long id) {

        User loggedUser = securityUserService.getLoggedUser();

        if (id != loggedUser.getId()) {
            LOGGER.debug("Forbidden request: edit profile of another user");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (biography == null && imageFile == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (imageFile != null) {
            byte[] imageBytes;
            try {
                imageBytes = FileUtils.readFileToByteArray(imageFile);
            } catch (IOException e) {
                LOGGER.debug("Error reading image file bytes");
                return Response.serverError().build();
            }
            if (!ImageUtils.validateImage(fileMetaData, imageBytes.length)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorDTO("Invalid image extension or file size too big", "image"))
                        .build();
            }
            byte[] resizedImage;
            try {
                resizedImage = ImageUtils.resizeToProfileSize(imageBytes, PROFILE_WIDTH, PROFILE_HEIGHT);
            } catch (IOException e) {
                LOGGER.debug("Error resizing user image");
                return Response.serverError().build();
            }
            userService.changeProfilePicture(loggedUser, resizedImage);
            imageFile.delete();
        }
        if (biography != null) {
            LOGGER.debug("User profile with id {} was edited", id);
            userService.editBiography(loggedUser, biography.getValue());
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/rates")
    public Response getUserRates(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        LOGGER.debug("Requesting rates for user with id {}", userId);
        List<RateDTO> rateDTOs = userService.getUserRates(userId)
                .stream()
                .map(userRate -> new RateDTO(userRate, uriContext.getBaseUri()))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<RateDTO>>(rateDTOs) {
        }).build();
    }

    @POST
    @Path("/rates")
    public Response rateUser(@Valid UserRateForm form) {
        User loggedUser = securityUserService.getLoggedUser();
        Set<ConstraintViolation<UserRateForm>> violations = validator.validate(form);

        if (!violations.isEmpty()) {
            List<ErrorDTO> errors = violations.stream().map(violation -> new ErrorDTO(violation.getMessage(),
                    violation.getPropertyPath().toString())).collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<List<ErrorDTO>>(errors) {
            }).build();
        }
        Optional<UserRate> rateOptional = userRatesService.findById(form.getRateId());
        if (!rateOptional.isPresent()) {
            LOGGER.debug("Invalid rate id {}", form.getRateId());
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UserRate rate = rateOptional.get();

        if (!rate.getRatedByUser().equals(loggedUser)) {
            LOGGER.debug("Forbidden request: only logged user can rate");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (rate.getTrip().getStatus() != TripStatus.COMPLETED) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        if (userRatesService.rateUser(rate.getId(), form.getRate(), form.getComment())) {
            LOGGER.debug("Rate with id {} created", rate.getId());
            return Response.noContent().build();
        }
        return Response.serverError().build();
    }


    @GET
    @Path("/{id}/trips-data")
    public Response getUserTripsData(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            LOGGER.debug("Trips data: User with id {} not found", userId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        LOGGER.debug("Requesting trip data for user with id {}", userId);
        int dueTrips = this.tripService.countUserTripsWithStatus(userId, TripStatus.DUE);
        int activeTrips = this.tripService.countUserTripsWithStatus(userId, TripStatus.IN_PROGRESS);
        int completedTrips = this.tripService.countUserTripsWithStatus(userId, TripStatus.COMPLETED);
        return Response.ok(new ProfileDataDTO(dueTrips, activeTrips, completedTrips)).build();
    }

    @GET
    @Path("/{id}/invitations")
    public Response getUserInvitations(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);

        if (!userOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!userOptional.get().equals(securityUserService.getLoggedUser())) {
            LOGGER.debug("Forbidden request: requesting invitations of another user");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<TripInvitationDTO> tripInvitations = userService.getTripInvitations(userId).stream()
                .filter(tripInvitation -> !tripInvitation.isResponded())
                .map(tripInvitation -> new TripInvitationDTO(tripInvitation, uriContext.getBaseUri()))
                .collect(Collectors.toList());

        return Response.ok(new GenericEntity<List<TripInvitationDTO>>(tripInvitations) {
        }).build();
    }

    @GET
    @Path("/{id}/pending-rates")
    public Response getUserPendingRates(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        User loggedUser = securityUserService.getLoggedUser();

        if (!userOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!userOptional.get().equals(loggedUser)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        LOGGER.debug("Requesting pending rates for user with id {}", userId);
        List<RateDTO> rates = userService.getUserPendingRates(userId)
                .stream()
                .map(userRate -> new RateDTO(userRate, uriContext.getBaseUri()))
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<RateDTO>>(rates) {
        }).build();
    }

}

 
