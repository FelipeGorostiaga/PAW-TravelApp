package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.dto.TripDTO;
import ar.edu.itba.paw.webapp.dto.TripListDTO;
import ar.edu.itba.paw.webapp.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("search")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class SearchController {

    private static final int SEARCH_PAGE_SIZE = 3;

    @Autowired
    TripService tripService;

    @Autowired
    UserService userService;

    @GET
    @Path("/{tripId}/users")
    public Response searchInvitableUsers(@PathParam("tripId") long tripId, @QueryParam("name") String name) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        List<UserDTO> resultUsers = userService.findInvitableUsersByName(name, tripOptional.get()).stream().map(UserDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<UserDTO>>(resultUsers) {
        }).build();
    }

    @GET
    @Path("/trips")
    public Response searchTripByName(@QueryParam("nameInput") String name, @DefaultValue("1") @QueryParam("page") int page) {
        page = (page < 1) ? 1 : page;
        final int totalTrips = this.tripService.countByNameSearch(name);
        final int maxPage = (int) (Math.ceil((float) totalTrips / SEARCH_PAGE_SIZE));;
        List<TripDTO> resultTrips = tripService.findByName(name, page)
                .stream()
                .map(TripDTO::new)
                .collect(Collectors.toList());

        return Response.ok(new TripListDTO(resultTrips,totalTrips, maxPage)).build();
    }

    @GET
    @Path("/advanced")
    public Response searchByMultipleParams(@QueryParam("place") String place,
                                           @QueryParam("startDate") String startDate,
                                           @QueryParam("endDate") String endDate,
                                           @QueryParam("name") String name) {

        Map<String, Object> filterMap = new HashMap<>();
        if (place != null && place.length() > 0)
            filterMap.put("place", place);
        if (startDate != null && DateManipulation.stringToLocalDate(startDate) != null)
            filterMap.put("startDate", DateManipulation.stringToLocalDate(startDate));
        if (endDate != null && DateManipulation.stringToLocalDate(endDate) != null)
            filterMap.put("endDate", DateManipulation.stringToLocalDate(endDate));
        if (name != null && name.length() > 0)
            filterMap.put("name", name);

        if (filterMap.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<TripDTO> resultTrips = tripService.findWithFilters(filterMap).stream().map(TripDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TripDTO>>(resultTrips) {
        }).build();
    }

}
