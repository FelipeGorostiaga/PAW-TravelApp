package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Path("users")
@RestController
@Produces(value = {MediaType.APPLICATION_JSON})
public class UserControllerREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerREST.class);

    private static final int JWT_ACCESS_EXPIRATION = 3600 * 2 * 1000; //2 hours
    private static final int JWT_REFRESH_EXPIRATION = 900000 * 1000; //10+ days

    @Autowired
    Validator validator;

    @Autowired
    private UserPicturesService ups;

    @Autowired
    private UserService us;

    @Autowired
    private TripService ts;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MailingService mailService;

    @Autowired
    private TravelUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAuthenticationToken(@Valid AuthenticationRequestDTO authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String accessToken = jwtUtil.generateToken(userDetails, JWT_ACCESS_EXPIRATION);
        final String refreshToken = jwtUtil.generateToken(userDetails, JWT_REFRESH_EXPIRATION);
        final UserDTO user = new UserDTO(Objects.requireNonNull(us.findByUsername(userDetails.getUsername()).orElse(null)));
        return Response.ok(new AuthenticationResponseDTO(accessToken, refreshToken, user)).build();
    }

    @GET
    @Path("/refresh")
    public Response refreshJwtToken(@HeaderParam("x-refresh-token") String refreshToken) {
        if(refreshToken != null) {
            try {
                String username = jwtUtil.extractUsername(refreshToken);
                if(username != null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if(jwtUtil.validateToken(refreshToken, userDetails)) {
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
        System.out.println(verificationCode);
        Optional<User> userOptional = us.findByVerificationCode(verificationCode);
        if(!userOptional.isPresent() || userOptional.get().isVerified()) {
            System.out.println("User optional is present: " + userOptional.isPresent());
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorDTO("Invalid user verification code or user is already verified")).build();
        }
        us.verify(userOptional.get());
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") final int id) {
        final Optional<User> userOptional = us.findById(id);
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
            List<ErrorDTO> errors = violations.stream().map(violation -> new ErrorDTO(violation.getMessage())).collect(Collectors.toList());
            return Response.status(Response.Status.BAD_REQUEST).entity(new GenericEntity<List<ErrorDTO>>(errors) {}).build();
        }
        User user;
        try {
            String verificationToken = RandomStringUtils.random(64,true,true);
            user = us.create(userForm.getFirstname(), userForm.getLastname(), userForm.getEmail(),
                    userForm.getPassword(), DateManipulation.stringToLocalDate(userForm.getBirthday()),
                    userForm.getNationality(), userForm.getSex(), verificationToken);
            mailService.sendRegisterMail(user, LocaleContextHolder.getLocale(), userForm.getVerificationURL());
        }
        catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDTO("Username already in use")).build();
        }
        return Response.ok(new UserDTO(user)).build();
    }

    @GET
    @Path("/{id}/picture")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getUserProfilePicture(@PathParam("id") final int id) {
        final Optional<UserPicture> pictureOpt = ups.findByUserId(id);
        if (!pictureOpt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new ImageDTO(pictureOpt.get())).build();
    }

    @GET
    @Path("/{userId}/trips")
    public Response getUserTrips(@PathParam("userId") final int id) {
        final Optional<User> userOptional = us.findById(id);
        if (!userOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User user = userOptional.get();
        List<TripDTO> trips = ts.getAllUserTrips(user).stream().map(TripDTO::new).collect(Collectors.toList());
        if (trips.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new GenericEntity<List<TripDTO>>(trips) {}).build();
    }

}


