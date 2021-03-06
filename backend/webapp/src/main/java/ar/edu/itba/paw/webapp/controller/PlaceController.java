package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.webapp.dto.PlaceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;


@Path("places")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class PlaceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripController.class);

    @Autowired
    private PlaceService placeService;

    @GET
    @Path("/{id}")
    public Response getPlace(@PathParam("id") final long placeId) {
        Optional<Place> placeOptional = this.placeService.findById(placeId);
        LOGGER.debug("Get place with id {}", placeId);
        if (!placeOptional.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new PlaceDTO(placeOptional.get())).build();
    }
}
