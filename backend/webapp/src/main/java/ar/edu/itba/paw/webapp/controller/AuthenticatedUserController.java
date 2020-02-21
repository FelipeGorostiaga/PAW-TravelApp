package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import ar.edu.itba.paw.webapp.dto.constraints.ConstraintViolationsDTO;
import ar.edu.itba.paw.webapp.form.EditProfileForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Set;

@Path("user")
@Controller
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticatedUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedUserController.class);

    @Context
    private UriInfo uriContext;

    @Autowired
    private Validator validator;

    @Autowired
    private SecurityUserService securityUserService;

    @PUT
    @Path("/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response editUserProfile(@BeanParam final EditProfileForm editProfileForm) {
        LOGGER.debug("Accessed edit profile");
        Set<ConstraintViolation<EditProfileForm>> violations = validator.validate(editProfileForm);
        if(!violations.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ConstraintViolationsDTO(violations)).build();
        }
        final User loggedUser = securityUserService.getLoggedUser();
        // TODO - edit user profile methods
        // userService.editProfile();
        // edit Bio
        // edit Profile Picture
        return Response.noContent().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoggedInUser() {
        final User loggedUser = securityUserService.getLoggedUser();
        if(loggedUser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new UserDTO(loggedUser)).build();
    }
}
