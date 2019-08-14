package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

@Path("users")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class UserControllerREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerREST.class);

    @Autowired
    private UserPicturesService ups;

    @Autowired
    private UserService us;

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


    //TODO : validate user content and maybe user @FormDataParam
    @Path("/")
    @POST
    public Response createUser(final UserDTO userDto) {
        final User user = us.create(userDto.getFirstname(), userDto.getLastname(), userDto.getEmail(),
                userDto.getPassword(), userDto.getBirthday(), userDto.getNationality());
        final URI uri = uriContext.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("/{id}/picture")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getUserProfilePicture(@PathParam("id") final int id) {
        LOGGER.debug("Accessed getUserProfilePicture with id {}", id);

        final Optional<UserPicture> pictureOpt = ups.findByUserId(id);

        if (!pictureOpt.isPresent()) {
            LOGGER.warn("Cannot render user profile picture, user with id {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(pictureOpt.get().getPicture()).build();
    }



}


