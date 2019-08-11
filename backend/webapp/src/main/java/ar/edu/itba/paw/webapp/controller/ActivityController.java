package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.ActivityCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ActivityController extends MainController {

    private GooglePlaces client = new GooglePlaces("AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M");

    @Autowired
    ActivityService as;

    @Autowired
    PlaceService ps;

    @Autowired
    TripService ts;

    @RequestMapping(value = "/home/trip/{tripId}/{activityId}/delete", method = {RequestMethod.POST})
    public ModelAndView deleteTripActivity(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId,
                                          @PathVariable(value = "activityId") long activityId) {

        Optional<Trip> tripOptional = ts.findById(tripId);
        if(tripOptional.isPresent()) {
            if(tripOptional.get().getAdminId() != user.getId()) {
                return new ModelAndView("403");
            }
        }
        else {
            return new ModelAndView("404");
        }
        ts.deleteTripActivity(activityId, tripId);
        String redirectFormat = String.format("redirect:/home/trip/%d", tripId);
        return new ModelAndView(redirectFormat);
    }


    @RequestMapping(value = "/home/trip/{tripId}/create-activity", method = {RequestMethod.GET})
    public ModelAndView createActivityGet(@PathVariable(value = "tripId") long tripId,
                                          @ModelAttribute("activityForm") final ActivityCreateForm form) {
        ModelAndView mav = new ModelAndView("createActivity");
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}/create-activity", method = {RequestMethod.POST})
    public ModelAndView createActivityPost(@PathVariable(value = "tripId") long tripId,
                                          @Valid @ModelAttribute("activityForm") final ActivityCreateForm form,
                                          final BindingResult errors) {
        ModelAndView mav = new ModelAndView("createActivity");
        Optional<Trip> tripOptional = ts.findById(tripId);
        if(!tripOptional.isPresent()) return mav;
        Trip trip = tripOptional.get();
        if(errors.hasErrors() ) {
            return mav;
        }
        if(!form.checkDates(trip.getStartDate(), trip.getEndDate())) {
            mav.addObject("invalidDates", true);
            return mav;
        }
        if(!form.checkTimeline(trip.getActivities())) {
            mav.addObject("intervalError", true);
            return mav;
        }
        ar.edu.itba.paw.model.Place modelPlace;
        List<Place> googlePlaces;
        try {
            googlePlaces = client.getPlacesByQuery(form.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
        }
        catch(GooglePlacesException gpe) {
            mav.addObject("errorMap", true);
            return mav;
        }
        Place googlePlace = googlePlaces.get(0);
        Optional<ar.edu.itba.paw.model.Place> maybePlace = ps.findByGoogleId(googlePlace.getPlaceId());
        modelPlace = maybePlace.orElseGet(() -> ps.create(googlePlace.getPlaceId(), googlePlace.getName(), googlePlace.getLatitude(),
                googlePlace.getLongitude(), googlePlace.getAddress()));

        Activity activity = as.create(form.getName(), form.getCategory(), modelPlace, tripOptional.get(),
                DateManipulation.stringToLocalDate(form.getStartDate()), DateManipulation.stringToLocalDate(form.getEndDate()));
        ts.addActivityToTrip(activity.getId(), tripId);
        String redirectURL = String.format("redirect:/home/trip/%d", tripId);
        mav.setViewName(redirectURL);
        return mav;
    }
}
