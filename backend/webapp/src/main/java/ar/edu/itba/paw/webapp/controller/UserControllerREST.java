package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.EditUserBiographyForm;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import ar.edu.itba.paw.webapp.utils.ImageUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
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
public class UserControllerREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerREST.class);

    private static final int JWT_ACCESS_EXPIRATION = 3600 * 4 * 1000; //4 hours
    private static final int JWT_REFRESH_EXPIRATION = 900000 * 1000; //10+ days

    private static final int PROFILE_WIDTH = 220;
    private static final int PROFILE_HEIGHT = 200;


    @Autowired
    Validator validator;

    @Autowired
    private UserPicturesService userPicturesService;

    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MailingService mailService;

    @Autowired
    private TravelUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    SecurityUserService securityUserService;


    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Valid AuthenticationRequestDTO authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(new ErrorDTO("Invalid username or password", "credentials")).build();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
        if (!userOptional.isPresent() || !userOptional.get().isVerified()) {
            return Response.status(Response.Status.FORBIDDEN).entity(new ErrorDTO("Please verify your email before login in", "verification")).build();
        }
        final UserDTO user = new UserDTO(userOptional.get());
        final String accessToken = jwtUtil.generateToken(userDetails, JWT_ACCESS_EXPIRATION);
        final String refreshToken = jwtUtil.generateToken(userDetails, JWT_REFRESH_EXPIRATION);
        return Response.ok(new AuthenticationResponseDTO(accessToken, refreshToken, user)).build();
    }

    @GET
    @Path("/refresh")
    public Response refreshJwtToken(@HeaderParam("x-refresh-token") String refreshToken) {
        if (refreshToken != null) {
            try {
                String username = jwtUtil.extractUsername(refreshToken);
                if (username != null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (jwtUtil.validateToken(refreshToken, userDetails)) {
                        final String newAccessToken = jwtUtil.generateToken(userDetails, JWT_ACCESS_EXPIRATION);
                        return Response.ok(new RefreshResponseDTO(newAccessToken)).build();
                    }
                }
            } catch (Exception ignored) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/verify")
    public Response verifyUserRegistration(@QueryParam("code") final String verificationCode) {
        Optional<User> userOptional = userService.findByVerificationCode(verificationCode);
        if (!userOptional.isPresent() || userOptional.get().isVerified()) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorDTO("Invalid user verification code or user is already verified", "verification")).build();
        }
        userService.verify(userOptional.get());
        return Response.ok().build();
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

    @Path("/create")
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
            String verificationToken = RandomStringUtils.random(64, true, true);
            user = userService.create(userForm.getFirstname(), userForm.getLastname(), userForm.getEmail(),
                    userForm.getPassword(), DateManipulation.stringToLocalDate(userForm.getBirthday()),
                    userForm.getNationality(), userForm.getSex(), verificationToken);
            mailService.sendRegisterMail(user, userForm.getVerificationURL());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("Email already in use", "email")).build();
        }
        return Response.ok(new UserDTO(user)).build();
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
    @Path("/{userId}/trips")
    public Response getUserTrips(@PathParam("userId") final int id) {
        final Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User user = userOptional.get();
        List<TripDTO> trips = tripService.getAllUserTrips(user).stream().map(TripDTO::new).collect(Collectors.toList());
        if (trips.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new GenericEntity<List<TripDTO>>(trips) {
        }).build();
    }

    @POST
    @Path("/{userId}/edit/biography")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editUserBiography(@Valid EditUserBiographyForm editBiographyForm, @PathParam("userId") final int id) {
        User loggedUser = securityUserService.getLoggedUser();
        if (id != loggedUser.getId()) return Response.status(Response.Status.FORBIDDEN).build();
        System.out.println(editBiographyForm.getBiography());
        Set<ConstraintViolation<EditUserBiographyForm>> violations = validator.validate(editBiographyForm);
        if (!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("Biography size can't exceed 500 characters long", "size")).build();
        }
        userService.editBiography(loggedUser, editBiographyForm.getBiography());
        return Response.ok().build();
    }

    @POST
    @Path("/{userId}/edit/picture")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response editUserProfilePicture(@FormDataParam("image") File imageFile,
                                           @FormDataParam("image") FormDataContentDisposition fileMetaData,
                                           @PathParam("userId") final int id) {

        User loggedUser = securityUserService.getLoggedUser();
        if (id != loggedUser.getId()) return Response.status(Response.Status.FORBIDDEN).build();
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
        return Response.ok().build();
    }

/*
    @POST
    @Path("/test-formdata")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response testFormData(@FormDataParam("editForm") )
*/

}


