package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import ar.edu.itba.paw.webapp.form.UserRateForm;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import ar.edu.itba.paw.model.TripStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("users")
@RestController
@Produces(value = {MediaType.APPLICATION_JSON})
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final int PROFILE_WIDTH = 220;
    private static final int PROFILE_HEIGHT = 200;

    private static final int PAGE_SIZE = 4;

    @Autowired
    Validator validator;

    @Autowired
    private UserPicturesService userPicturesService;

    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Autowired
    private UserRatesService userRatesService;

    @Autowired
    SecurityUserService securityUserService;

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
        return Response.ok(new UserDTO(user)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") final int id) {
        final Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            return Response.ok(new UserDTO(userOptional.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/picture")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getUserProfilePicture(@PathParam("id") final int id) {
        final Optional<UserPicture> pictureOpt = userPicturesService.findByUserId(id);
        if (!pictureOpt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(pictureOpt.get().getPicture()).build();
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
        final int totalUserTrips = tripService.countUserTrips(user);
        final int maxPage = (int) (Math.ceil((float) totalUserTrips / PAGE_SIZE));
        if (page > maxPage) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<TripDTO> trips = tripService.getUserTrips(user, page).stream().map(TripDTO::new).collect(Collectors.toList());
        return Response.ok(new TripListDTO(trips, totalUserTrips, maxPage)).build();
    }

    @POST
    @Path("/{id}/editProfile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response editProfile(@FormDataParam("biography") FormDataBodyPart biography,
                                @FormDataParam("image") File imageFile,
                                @FormDataParam("image") FormDataContentDisposition fileMetaData,
                                @PathParam("id") final long id) {
        User loggedUser = securityUserService.getLoggedUser();
        if (id != loggedUser.getId()) return Response.status(Response.Status.FORBIDDEN).build();
        if (biography == null && imageFile == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
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
                resizedImage = ImageUtils.resizeToProfileSize(imageBytes, PROFILE_WIDTH, PROFILE_HEIGHT);
            } catch (IOException e) {
                return Response.serverError().build();
            }
            userService.changeProfilePicture(loggedUser, resizedImage);
            imageFile.delete();
        }
        if (biography != null) {
            userService.editBiography(loggedUser, biography.getValue());
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/rates")
    public Response userRates(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) return Response.status(Response.Status.BAD_REQUEST).build();
        List<RateDTO> rateDTOs = userService.getUserRates(userId).stream().map(RateDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<RateDTO>>(rateDTOs) {
        }).build();
    }

    @POST
    @Path("/rateUser")
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
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        UserRate rate = rateOptional.get();
        if (!rate.getRatedByUser().equals(loggedUser)) return Response.status(Response.Status.FORBIDDEN).build();
        if (rate.getTrip().getStatus() != TripStatus.COMPLETED)
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        if (userRatesService.rateUser(rate.getId(), form.getRate(), form.getComment())) {
            return Response.ok().build();
        }
        return Response.serverError().build();

    }
    
    @GET
    @Path("/{id}/profile")
    public Response getUserProfileData(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        List<RateDTO> rates = this.userService.getUserRates(userId).stream().map(RateDTO::new).collect(Collectors.toList());
        int dueTrips = this.tripService.countUserTripsWithStatus(userId, TripStatus.DUE);
        int activeTrips = this.tripService.countUserTripsWithStatus(userId, TripStatus.IN_PROGRESS);
        int completedTrips = this.tripService.countUserTripsWithStatus(userId, TripStatus.COMPLETED);
        return Response.ok(new ProfileDataDTO(userOptional.get(), rates, dueTrips, activeTrips, completedTrips)).build();
    }

    @GET
    @Path("/{id}/invitations")
    public Response getUserInvitations(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        if (!userOptional.get().equals(securityUserService.getLoggedUser()))
            return Response.status(Response.Status.FORBIDDEN).build();
        List<TripInvitationDTO> tripInvitations = userService.getTripInvitations(userId).stream()
                .filter(tripInvitation -> !tripInvitation.isResponded())
                .map(TripInvitationDTO::new)
                .collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TripInvitationDTO>>(tripInvitations) {
        }).build();
    }

    @GET
    @Path("/{id}/pending-rates")
    public Response getUserPendingRates(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        if (!userOptional.get().equals(securityUserService.getLoggedUser()))
            return Response.status(Response.Status.FORBIDDEN).build();
        List<RateDTO> rates = userService.getUserPendingRates(userId).stream().map(RateDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<RateDTO>>(rates) {
        }).build();
    }

    @GET
    @Path("/{id}/rating")
    @Produces(value = {MediaType.TEXT_PLAIN})
    public Response getUserRating(@PathParam("id") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        double rate = userService.calculateRate(userId);
        return Response.ok(rate).build();
    }
}

 
