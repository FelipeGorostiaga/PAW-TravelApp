package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;


@Path("users")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class UserControllerREST {

    public static final int DEFAULT_PAGE_SIZE = 9;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerREST.class);

    @Autowired
    private UserPicturesService ups;

    @Autowired
    private UserService us;

    @Autowired
    private TripService ts;

    @Context
    private UriInfo uriContext;

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") final int id) {
        final Optional<User> userOptional = us.findById(id);

        LOGGER.debug("Accessed getUserById with id {}", id);

        if (userOptional.isPresent()) {
            return Response.ok(new UserDTO(userOptional.get(), uriContext.getBaseUri())).build();
        } else {
            LOGGER.warn("Cannot render user profile, user with id {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserCreateForm userForm) {
        Optional<User> userDuplicate = us.findByUsername(userForm.getEmail());
        if(userDuplicate.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        final User user = us.create(userForm.getFirstname(), userForm.getLastname(), userForm.getEmail(),
                userForm.getPassword(), userForm.getBirthday(), userForm.getNationality());
        final URI uri = uriContext.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        return Response.created(uri).build();
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

        return Response.ok(pictureOpt.get().getPicture()).build();
    }

    @GET
    @Path("/{userId}/trips")
    public Response getUserTrips(@PathParam("userId") final int id,
                                 @DefaultValue("1") @QueryParam("page") int page,
                                 @DefaultValue("" + DEFAULT_PAGE_SIZE) @QueryParam("per_page") int pageSize)  {

        final Optional<User> userOptional = us.findById(id);
        if (!userOptional.isPresent()) {
            LOGGER.debug("Failed to get user with ID: {} trips, user not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User user = userOptional.get();
        final List<Trip> trips = ts.getAllUserTrips(user);
        return Response.ok(trips).build();
    }

}


