package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.dto.TripDTO;
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
public class SearchControllerREST {

    @Autowired
    TripService tripService;

    @Autowired
    UserService userService;

    @GET
    @Path("/users")
    public Response searchUsersByName(@QueryParam("name") String name) {
        List<UserDTO> resultUsers = userService.findByName(name).stream().map(UserDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<UserDTO>>(resultUsers) {
        }).build();
    }

    @GET
    @Path("/{tripId}/users/invite")
    public Response searchInvitableUsers(@PathParam("tripId") long tripId, @QueryParam("name") String name) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if (!tripOptional.isPresent()) return Response.status(Response.Status.NOT_FOUND).build();
        List<UserDTO> resultUsers = userService.findInvitableUsersByName(name, tripOptional.get()).stream().map(UserDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<UserDTO>>(resultUsers) {
        }).build();
    }

    @GET
    @Path("/trips/name")
    public Response searchByName(@QueryParam("nameInput") String name) {
        List<TripDTO> resultTrips = tripService.findByName(name).stream().map(TripDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TripDTO>>(resultTrips) {
        }).build();
    }

    @GET
    @Path("/advanced")
    public Response searchByMultipleParams(@QueryParam("placeName") String placeName,
                                           @QueryParam("startDate") String startDate,
                                           @QueryParam("endDate") String endDate,
                                           @QueryParam("category") String category) {
        Map<String, Object> filterMap = new HashMap<>();
        // TODO - validate inputs
        filterMap.put("category", category);
        filterMap.put("placeName", placeName);
        filterMap.put("startDate", DateManipulation.stringToLocalDate(startDate));
        filterMap.put("endDate", DateManipulation.stringToLocalDate(endDate));
        List<TripDTO> resultTrips = tripService.findWithFilters(filterMap).stream().map(TripDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TripDTO>>(resultTrips) {
        }).build();
    }

}
