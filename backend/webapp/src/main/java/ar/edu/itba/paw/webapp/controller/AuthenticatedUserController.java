package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import ar.edu.itba.paw.webapp.exceptions.DTOValidationException;
import ar.edu.itba.paw.webapp.form.EditProfileForm;
import ar.edu.itba.paw.webapp.validators.ConstraintValidatorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("user")
@Controller
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticatedUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedUserController.class);

    @Context
    private UriInfo uriContext;

    @Autowired
    private ConstraintValidatorDTO DTOValidator;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserService userService;


    @PUT
    @Path("/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response editUserProfile(@BeanParam final EditProfileForm editProfileForm) throws DTOValidationException {
        LOGGER.debug("Accessed edit profile");
        DTOValidator.validate(editProfileForm, "Failed to validate picture");
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
        return Response.ok(new UserDTO(loggedUser, uriContext.getBaseUri())).build();
    }




}
