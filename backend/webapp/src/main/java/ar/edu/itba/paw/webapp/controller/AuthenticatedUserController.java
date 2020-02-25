package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.webapp.auth.SecurityUserService;
import ar.edu.itba.paw.webapp.dto.ErrorDTO;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationDTO;
import ar.edu.itba.paw.webapp.dto.constraint.ConstraintViolationsDTO;
import ar.edu.itba.paw.webapp.form.EditProfileForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Path("user")
@Controller
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticatedUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedUserController.class);
    private static final long MAX_UPLOAD_SIZE = 5242880;

    @Context
    private UriInfo uriContext;

    @Autowired
    private Validator validator;

    @Autowired
    private UserPicturesService userPicturesService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUserService securityUserService;

    @PUT
    @Path("/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response editUserProfile(@Valid EditProfileForm editProfileForm) {

        //TODO - ADD INTERNACIONALIZATION

        Optional<User> loggedUserOptional = securityUserService.getLoggedUser();
        if(!loggedUserOptional.isPresent()) return Response.status(Response.Status.FORBIDDEN).build();
        User loggedUser = loggedUserOptional.get();
        LOGGER.debug("Accessed edit profile for user {}", loggedUser.getId());
        Set<ConstraintViolation<EditProfileForm>> violations = validator.validate(editProfileForm);
        ConstraintViolationsDTO constraintViolationsDTO = new ConstraintViolationsDTO(violations);
        MultipartFile tripPicture = editProfileForm.getImageUpload();
        byte[] imageBytes = null;
        if(tripPicture != null && !tripPicture.isEmpty()) {
            String contentType = tripPicture.getContentType();
            if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                constraintViolationsDTO.add(new ConstraintViolationDTO("Invalid image format", "imageInput"));
            }
            else if(tripPicture.getSize() > MAX_UPLOAD_SIZE) {
                constraintViolationsDTO.add(new ConstraintViolationDTO("Image size is too big", "imageInput"));
            }
            try {
                imageBytes = tripPicture.getBytes();
            } catch (IOException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorDTO("Server couldn´t get image bytes"))
                        .build();
            }
        }
        if(constraintViolationsDTO.getErrors().length > 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(constraintViolationsDTO).build();
        }

        if(userPicturesService.findByUserId(loggedUser.getId()).isPresent()) {
            userPicturesService.deleteByUserId(loggedUser.getId());
        }
        UserPicture userPicture = userPicturesService.create(loggedUser, imageBytes);
        loggedUser.setBiography(editProfileForm.getBiography());
        userService.update(loggedUser);
        //TODO - ADD SERVICE METHOD THAT DELETES OLD PICTURE AND PUTS NEW ONE INSTEAD OF DOING IT IN CONTROLLER
        return Response.ok(new UserDTO(loggedUser)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoggedInUser() {
        Optional<User> loggedUser = securityUserService.getLoggedUser();
        if(!loggedUser.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new UserDTO(loggedUser.get())).build();
    }
}
