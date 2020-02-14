package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.TripCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Path("trips")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class TripControllerREST {

    private static final Logger LOGGER = LoggerFactory.getLogger(TripControllerREST.class);

    @Autowired
    TripService tripService;

    @Autowired
    GooglePlaces googleClient;

    @Autowired
    PlaceService placeService;

    @Autowired
    UserService userService;

    @Autowired
    MailingService mailingService;

    @GET
    @Path("/all")
    public Response getAllTrips(@DefaultValue("1") @QueryParam("page") int pageNum) {
        List<Trip> trips = tripService.getAllTrips(pageNum);
        return Response.ok(trips).build();
    }


    @POST
    @Path("/{id}/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTrip(@Valid TripCreateForm tripCreateForm, @PathParam("id") final long userId) {

        //TODO - validate constraints
        Optional<User> userOpt = userService.findById(userId);

        //TODO - send proper error messages
        if(!userOpt.isPresent()) {
            LOGGER.debug("Invalid userId");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        List<Place> places;
        try {
            places = googleClient.getPlacesByQuery(tripCreateForm.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
        }
        catch(GooglePlacesException gpe) {
            LOGGER.debug("Invalid google maps query location");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        Place place = places.get(0);
        LOGGER.debug("Google Place name is {}", place.getName());
        Optional<ar.edu.itba.paw.model.Place> maybePlace = placeService.findByGoogleId(place.getPlaceId());

        ar.edu.itba.paw.model.Place dbPlace = maybePlace.orElseGet(() -> placeService
                .create(place.getPlaceId(), place.getName(), place.getLatitude(), place.getLongitude(), place.getAddress()));
        Trip trip = tripService.create(userOpt.get().getId(), dbPlace.getId(), tripCreateForm.getName(),
                tripCreateForm.getDescription(), DateManipulation.stringToLocalDate(tripCreateForm.getStartDate()),
                DateManipulation.stringToLocalDate(tripCreateForm.getEndDate()));

        // TODO - add uri for new created trip
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response getTrip(@PathParam("id") final long tripId) {
        Optional<Trip> trip = tripService.findById(tripId);
        if(trip.isPresent()) {
            return Response.ok(trip.get()).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}/places")
    public Response getTripPlaces(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            List<ar.edu.itba.paw.model.Place> places = tripService.findTripPlaces(tripOptional.get());
            return Response.ok(places).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("/{id}/delete")
    public Response deleteTrip(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            tripService.deleteTrip(tripId);
            return Response.ok().build();
        }
        // TODO - add error message
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}/rates")
    public Response getTripRates(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            return Response.ok(tripService.getTripRates(tripId)).build();
        }
        // TODO - add error message
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{id}/comments")
    public Response getTripComments(@PathParam("id") final long tripId) {
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(tripOptional.isPresent()) {
            return Response.ok(tripService.getTripComments(tripId)).build();
        }
        // TODO - add error message
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}/add/{userId}")
    public Response addUserToTrip(@PathParam("id") final long tripId, @PathParam("userId") final long userId) {
        Optional<User> userOptional = userService.findById(userId);
        Optional<Trip> tripOptional = tripService.findById(tripId);
        if(userOptional.isPresent() && tripOptional.isPresent()) {
            User u = userOptional.get();
            Trip t = tripOptional.get();
            Optional<User> tripAdmin = userService.findById(t.getAdminId());
            if(tripAdmin.isPresent()) {
                User admin = tripAdmin.get();
                tripService.addUserToTrip(userId, tripId);
                mailingService.sendJoinTripMail(u.getEmail(), u.getFirstname(), t.getName(), admin.getFirstname(),
                        admin.getLastname(), getLocale());
                return Response.ok().build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    // get trip image
    //
}
