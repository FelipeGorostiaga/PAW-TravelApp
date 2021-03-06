package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.webapp.utils.DateManipulation;
import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.dto.TripDTO;
import ar.edu.itba.paw.webapp.dto.TripListDTO;
import ar.edu.itba.paw.webapp.utils.PaginationLinkFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("search")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class SearchController {

    @Autowired
    private TripService tripService;

    @Autowired
    private PaginationLinkFactory paginationLinkFactory;

    @Context
    private UriInfo uriContext;

    private static final int ADV_SEARCH_PAGE_SIZE = 2;

    @GET
    @Path("/trips")
    public Response searchTripsWithFilters(@QueryParam("place") String place,
                                           @QueryParam("startDate") String startDate,
                                           @QueryParam("endDate") String endDate,
                                           @QueryParam("name") String name,
                                           @DefaultValue("1") @QueryParam("page") int page) {
        page = (page < 1) ? 1 : page;

        Map<String, Object> filterMap = new HashMap<>();
        if (place != null && place.length() > 0) {
            filterMap.put("place", place);
        }
        if (startDate != null && DateManipulation.stringToLocalDate(startDate) != null) {
            filterMap.put("startDate", DateManipulation.stringToLocalDate(startDate));
        }
        if (endDate != null && DateManipulation.stringToLocalDate(endDate) != null) {
            filterMap.put("endDate", DateManipulation.stringToLocalDate(endDate));
        }
        if (name != null && name.length() > 0) {
            filterMap.put("name", name);
        }

        PaginatedResult<Trip> paginatedResult = tripService.findWithFilters(filterMap, page);

        final int maxPage = (int) (Math.ceil((float) paginatedResult.getTotalTrips() / ADV_SEARCH_PAGE_SIZE));

        if (maxPage != 0 && page > maxPage) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        List<TripDTO> resultTrips = paginatedResult.getTrips()
                .stream()
                .map(trip -> new TripDTO(trip, uriContext.getBaseUri()))
                .collect(Collectors.toList());

        final Map<String, Link> links = paginationLinkFactory.createLinks(uriContext, page, maxPage);
        final Link[] linkArray = links.values().toArray(new Link[0]);

        return Response.ok(new TripListDTO(resultTrips, paginatedResult.getTotalTrips(), maxPage)).links(linkArray).build();
    }

}
