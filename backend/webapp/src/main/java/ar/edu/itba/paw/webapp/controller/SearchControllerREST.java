package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.dto.TripDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("search")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class SearchControllerREST {

    @Autowired
    TripService tripService;

    @GET
    @Path("/name")
    public Response searchByName(@QueryParam("nameInput") String name) {
        List<TripDTO> resultTrips = tripService.findByName(name).stream().map(TripDTO::new).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<TripDTO>>(resultTrips){}).build();
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
        return Response.ok(new GenericEntity<List<TripDTO>>(resultTrips){}).build();
    }

}
