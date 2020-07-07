package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationsDTO;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Path("users")
@RestController
@Produces(value = {MediaType.APPLICATION_JSON})
public class UserControllerREST {

    private static final int DEFAULT_PAGE_SIZE = 9;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerREST.class);

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
        }
        catch (AuthenticationException e) {
            LOGGER.debug("Invalid username or password");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return Response.ok(new AuthenticationResponseDTO(jwt)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") final int id) {
        final Optional<User> userOptional = us.findById(id);
        LOGGER.debug("Accessed getUserById with id {}", id);
        if (userOptional.isPresent()) {
            return Response.ok(new UserDTO(userOptional.get())).build();
        } else {
            LOGGER.warn("Cannot render user profile, user with id {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserCreateForm userForm) {
        Set<ConstraintViolation<UserCreateForm>> violations = validator.validate(userForm);
        if(!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ConstraintViolationsDTO(violations)).build();
        }
        final User user = us.create(userForm.getFirstname(), userForm.getLastname(), userForm.getEmail(),
                userForm.getPassword(), DateManipulation.stringToLocalDate(userForm.getBirthday()), userForm.getNationality());
        return Response.ok(new UserDTO(user)).build();
    }

    //WORKS NOT FOUND --> TODO: CHECK IF IT WORKS WITH DB PICTURE
    @GET
    @Path("/{id}/picture")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getUserProfilePicture(@PathParam("id") final int id) {
        LOGGER.debug("In getUserProfilePicture() with id {}", id);
        final Optional<UserPicture> pictureOpt = ups.findByUserId(id);
        if (!pictureOpt.isPresent()) {
            LOGGER.warn("Cannot render user profile picture, user with id {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new ImageDTO(pictureOpt.get())).build();
    }

    //WORKS NOT CONTENT --> TODO: CHECK IF IT WORKS WITH EXISTING TRIPS
    @GET
    @Path("/{userId}/trips")
    public Response getUserTrips(@PathParam("userId") final int id, @DefaultValue("1") @QueryParam("page") int page) {
        final Optional<User> userOptional = us.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.debug("Failed to get trips of user with id {} , user not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User user = userOptional.get();
        List<TripDTO> trips = ts.getAllUserTrips(user).stream().map(TripDTO::new).collect(Collectors.toList());
        System.out.println(trips);
        if(trips.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(trips).build();
    }

}


