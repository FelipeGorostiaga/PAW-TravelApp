package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.webapp.dto.ImageDTO;
import ar.edu.itba.paw.webapp.dto.TripDTO;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationsDTO;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

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
@Controller
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

    @GET
    @Path("/hello")
    public Response testHello() {
        return Response.ok("Hello World!").build();
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
        return Response.ok(trips).build();
    }

}


