package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.EditTripForm;
import ar.edu.itba.paw.webapp.form.TripCommentForm;
import ar.edu.itba.paw.webapp.form.TripCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Controller
public class TripController extends MainController{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    private static final GooglePlaces client = new GooglePlaces("AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int MAX_TRIPS_PAGE = 4;
    private static final long MAX_UPLOAD_SIZE = 5242880;

    @Autowired
    private TripCommentsService tcs;

    @Autowired
    private MailingService ms;

    @Autowired
    private ActivityService as;

    @Autowired
    private TripService ts;

    @Autowired
    private UserService us;

    @Autowired
    private PlaceService ps;

    @Autowired
    private TripPicturesService tripPictureService;

    @Autowired
    private TripRateService tripRateService;

    @RequestMapping("/home/create-trip")
    public ModelAndView createTripGet(@ModelAttribute("createTripForm") final TripCreateForm form) {
        return new ModelAndView("createTrip");
    }

    @RequestMapping("/home/trips/{pageNum}")
    public ModelAndView getUserTrips(@ModelAttribute("user") User user,  @PathVariable(value = "pageNum") int pageNum) {
        ModelAndView mav = new ModelAndView("userTrips");
        User u = us.findById(user.getId()).get();
        List<Trip> userTrips =  ts.getAllUserTrips(u);
        int userTripsQty = userTrips.size();
        int requiredPages = (int) Math.ceil(userTripsQty/(double)MAX_TRIPS_PAGE);
        int offset = (pageNum - 1) * MAX_TRIPS_PAGE;
        if(pageNum > 1 && pageNum > requiredPages) {
            mav.setViewName("404");
            return mav;
        }
        int end = (offset + MAX_TRIPS_PAGE) > userTripsQty ? (userTripsQty) : (offset + MAX_TRIPS_PAGE);
        List<Trip> subList = userTrips.subList(offset, end);
        List<DataPair<Trip, DataPair<ar.edu.itba.paw.model.Place, Boolean>>> dataPairList = new LinkedList<>();
        for (Trip trip: subList) {
            long placeId = trip.getStartPlaceId();
            ar.edu.itba.paw.model.Place place = ps.findById(placeId).get();
            dataPairList.add(new DataPair<>(trip,
                    new DataPair<>(place,tripPictureService.findByTripId(trip.getId()).isPresent())));
        }
        mav.addObject("dateFormat", formatter);
        mav.addObject("pageQty", requiredPages);
        mav.addObject("isEmpty", dataPairList.isEmpty());
        mav.addObject("userTripsList", dataPairList);
        mav.addObject("formatter", formatter);
        return mav;
    }

    @RequestMapping(value = "/home/create-trip", method = {RequestMethod.POST})
    public ModelAndView createTripPost(@ModelAttribute("user") User user,
                                       @Valid @ModelAttribute("createTripForm") final TripCreateForm form,
                                       final BindingResult errors) {
        List<Place> places;
        ar.edu.itba.paw.model.Place modelPlace;
        ModelAndView mav = new ModelAndView("createTrip");
        if(errors.hasErrors()) {
            return mav;
        }
        if(!form.validateDates()) {
            mav.addObject("invalidDates", true);
            return mav;
        }
        LOGGER.debug("No errors in create editTripGet form");
        try {
            places = client.getPlacesByQuery(form.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
        }
        catch(GooglePlacesException gpe) {
            LOGGER.debug("Invalid google maps query location");
            mav.addObject("invalidPlace", true);
            return mav;
        }
        Place place = places.get(0);
        LOGGER.debug("Google Place name is {}", place.getName());

        Optional<ar.edu.itba.paw.model.Place> maybePlace = ps.findByGoogleId(place.getPlaceId());

        modelPlace = maybePlace.orElseGet(() -> ps.create(place.getPlaceId(), place.getName(), place.getLatitude(),
                place.getLongitude(), place.getAddress()));


        Trip trip = ts.create(user.getId(), modelPlace.getId(), form.getName(), form.getDescription(),
                DateManipulation.stringToLocalDate(form.getStartDate()),
                DateManipulation.stringToLocalDate(form.getEndDate()));

        String redirectFormat = String.format("redirect:/home/trip/%d", trip.getId());
        mav.setViewName(redirectFormat);
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}/edit", method = {RequestMethod.GET})
    public ModelAndView editTripGet(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId,
                                    @ModelAttribute("editTripForm") final EditTripForm form) {

        ModelAndView mav = new ModelAndView("editTrip");
        Optional<Trip> maybeTrip = ts.findById(tripId);
        if(!maybeTrip.isPresent()) {
            mav.setViewName("404");
            return mav;
        }
        if(maybeTrip.get().getAdminId() != user.getId()) {
            mav.setViewName("403");
            return mav;
        }
        mav.addObject("trip", maybeTrip.get());
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}/edit", method = {RequestMethod.POST})
    public ModelAndView editTrip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId,
                             @Valid @ModelAttribute("editTripForm") final EditTripForm form, BindingResult errors) {
        ModelAndView mav = new ModelAndView("editTrip");
        Optional<Trip> tripOptional = ts.findById(tripId);
        if(!tripOptional.isPresent()) {
            mav.setViewName("404");
            return mav;
        }
        Trip trip = tripOptional.get();
        mav.addObject("trip", trip);
        if(tripOptional.get().getAdminId() != user.getId()) {
            mav.setViewName("403");
            return mav;
        }
        if(errors.hasErrors()) {
            return mav;
        }
        MultipartFile tripPicture = form.getImageUpload();
        byte[] imageBytes;
        if(tripPicture != null && !tripPicture.isEmpty()) {
            String contentType = tripPicture.getContentType();
            if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                mav.addObject("invalidContentError", true);
                return mav;
            }
            else if(tripPicture.getSize() > MAX_UPLOAD_SIZE) {
                mav.addObject("fileSizeError", true);
                return mav;
            }
            try {
                imageBytes = tripPicture.getBytes();
            } catch (IOException e) {
                mav.addObject("generalError", true);
                return mav;
            }
        }
        else {
            mav.addObject("generalError", true);
            return mav;
        }
        if(tripPictureService.findByTripId(tripId).isPresent()) {
            if(!tripPictureService.deleteByTripId(tripId)) {
                mav.addObject("generalError", true);
                return mav;
            }
        }
        TripPicture tripPictureModel = tripPictureService.create(trip, imageBytes);
        trip.setProfilePicture(tripPictureModel);
        String redirectFormat = String.format("redirect:/home/trip/%d", tripId);
        mav.setViewName(redirectFormat);
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}/join", method = {RequestMethod.POST})
    public ModelAndView joinTrip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId) {
        ModelAndView mav = new ModelAndView("editTrip");
        ts.addUserToTrip(user.getId(), tripId);
        Optional<Trip> optionalTrip = ts.findById(tripId);
        if(optionalTrip.isPresent()) {
            Optional<User> u = us.findById(optionalTrip.get().getAdminId());
            if(u.isPresent()) {
                User us = u.get();
                ms.sendJoinTripMail(us.getEmail(), us.getFirstname(),  optionalTrip.get().getName(),
                        user.getFirstname(), user.getLastname(), getLocale());
            }

        }
        String redirect = String.format("redirect:/home/trip/%d", tripId);
        mav.setViewName(redirect);
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}/exit", method = {RequestMethod.POST})
    public ModelAndView exitTrip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId) {
        ts.removeUserFromTrip(user.getId(), tripId);
        Optional<Trip> optionalTrip = ts.findById(tripId);
        if(optionalTrip.isPresent()) {
            Optional<User> u = us.findById(optionalTrip.get().getAdminId());
            if(u.isPresent()) {
                User us = u.get();
                ms.sendExitTripMail(us.getEmail(), us.getFirstname(),  optionalTrip.get().getName(),
                        user.getFirstname(), user.getLastname(), getLocale());
            }

        }
        return new ModelAndView("redirect:/home/trips/1");
    }

    @RequestMapping(value = "/home/trip/{tripId}/delete", method = {RequestMethod.POST})
    public ModelAndView deleteTrip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId) {
        ModelAndView mav = new ModelAndView();
        Optional<Trip> optionalTrip = ts.findById(tripId);
        if(optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            if(user.getId() != trip.getAdminId()) {
                mav.setViewName("403");
                return mav;
            }
        }
        for(User u : optionalTrip.get().getUsers()) {
            ms.sendDeleteTripMail(u.getEmail(), u.getFirstname(), u.getLastname(), optionalTrip.get().getName(), getLocale());
        }
        ts.deleteTrip(tripId);
        mav.setViewName("redirect:/home/trips/1");
        return mav;
    }

    @RequestMapping("/home/search-trip/")
    public ModelAndView searchByName(@ModelAttribute("user") User user, @RequestParam(value = "nameInput") String nameInput) {
        ModelAndView mav = new ModelAndView("searchTrips");
        List<Trip> trips = ts.findByName(nameInput);
        mav.addObject("tripQty", trips.size());
        mav.addObject("tripsList", trips);
        return mav;
    }

    @RequestMapping(value = "/home/advanced-search")
    public ModelAndView advancedSearch() {
        ModelAndView mav = new ModelAndView("advancedSearch");
        return mav;
    }

    @RequestMapping(value = "/home/advanced-search/query")
    public ModelAndView advancedSearchPost(@ModelAttribute("user") User user,
                                       @RequestParam(value = "placeName", required = false) String placeName,
                                       @RequestParam(value = "startDate", required = false) String startDate,
                                       @RequestParam(value = "endDate", required = false) String endDate,
                                       @RequestParam(value = "category", required = false) String category) {
        ModelAndView mav = new ModelAndView("advancedSearch");
        if((placeName.length() < 1) && (startDate.length() < 1) && (endDate.length() < 1) && category.equals("NA")) {
            mav.addObject("noInput", true);
            return mav;
        }
        Map<String, Object> filterMap = new HashMap<>();
        if(startDate.length() > 0) {
            if(DateManipulation.validate(startDate)){
                filterMap.put("startDate", DateManipulation.stringToLocalDate(startDate));
            }
            else {
                mav.addObject("invalidStartDate", true);
                return mav;
            }

        }
        if(endDate.length() > 0) {
            if(DateManipulation.validate(endDate)){

                filterMap.put("endDate", DateManipulation.stringToLocalDate(endDate));
            }
            else {
                mav.addObject("invalidEndDate", true);
                return mav;
            }
        }
        if(!category.equals("NA")) {
            filterMap.put("category", category);
        }
        if(placeName.length() > 0) {
            filterMap.put("placeName", placeName);
        }
        List<Trip> searchResult = ts.findWithFilters(filterMap);
        mav.addObject("tripQty", searchResult.size());
        mav.addObject("tripsList", searchResult);
        mav.setViewName("searchTrips");
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}/image", method = {RequestMethod.GET})
    public void getProfileImage(@PathVariable(value = "tripId") long tripId, HttpServletResponse response) {
        Optional<TripPicture> tripPictureOptional = tripPictureService.findByTripId(tripId);
        try {
            if(tripPictureOptional.isPresent()) {
                TripPicture tp = tripPictureOptional.get();
                String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(tp.getPicture()));
                response.setContentType(mimeType);
                response.getOutputStream().write(tp.getPicture());
            }
        }
        catch (IOException ex) {
            //nothing to do here
        }
    }

    @RequestMapping(value = "/home/trip/{tripId}")
    public ModelAndView trip(@PathVariable(value = "tripId") long tripId,
                             @ModelAttribute("user") User user,
                             @ModelAttribute("tripCommentForm")TripCommentForm tripCommentForm){
        ModelAndView mav = new ModelAndView("trip");
        Optional<Trip> maybeTrip = ts.findById(tripId);
        if(!maybeTrip.isPresent()) {
            mav.setViewName("404");
            return mav;
        }
        Trip trip = maybeTrip.get();
        LOGGER.debug("trip {}", trip);
        Optional<User> adminUserOp = us.findById(trip.getAdminId());
        if(!adminUserOp.isPresent()) {
            mav.setViewName("404");
            return mav;
        }
        User adminUser = adminUserOp.get();

        List<DataPair<Activity, ar.edu.itba.paw.model.Place>> tripActAndPlace = as.getTripActivitiesDetails(trip);
        List <ar.edu.itba.paw.model.Place> tripPlaces = ts.findTripPlaces(trip);
        Optional<ar.edu.itba.paw.model.Place> sPlaceOpt = ps.findById(trip.getStartPlaceId());
        sPlaceOpt.ifPresent(tripPlaces::add);
        List<User> tripMembers = trip.getUsers();
        boolean isTravelling = false;
        if(trip.getUsers().contains(user) || trip.getAdminId() == user.getId()) {
            isTravelling = true;
        }
        mav.addObject("startPlace", sPlaceOpt.get());
        mav.addObject("admin", adminUser);
        mav.addObject("users", tripMembers);
        mav.addObject("passengerCount", tripMembers.size() + 1 );
        mav.addObject("hasTripPicture", tripPictureService.findByTripId(tripId).isPresent());
        mav.addObject("isEmpty", tripActAndPlace.size() == 0);
        mav.addObject("isTravelling", isTravelling);
        mav.addObject("places", tripPlaces);
        mav.addObject("actAndPlaces", tripActAndPlace);
        mav.addObject("trip", trip);
        mav.addObject("comments", ts.getTripComments(trip.getId()));
        mav.addObject("timeFormatter", formatterTime);
        mav.addObject("formatter", formatter);
        mav.addObject("startDate", trip.getStartDate());
        mav.addObject("endDate", trip.getEndDate());
        mav.addObject("rate", ts.getTripRates(trip.getId()).stream()
                                                                            .mapToDouble(TripRate::getRate)
                                                                            .average()
                                                                            .orElse(3));
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}", method = {RequestMethod.POST})
    public ModelAndView commentPost(@PathVariable(value = "tripId") long tripId,
                                @ModelAttribute("user") User user,
                                @Valid @ModelAttribute("tripCommentForm")TripCommentForm tripCommentForm,
                                final BindingResult errors){
        String redirectFormat = String.format("redirect:/home/trip/%d", tripId);
        ModelAndView  mav = new ModelAndView(redirectFormat);
        if(errors.hasErrors()){
            return mav;
        }
        Optional<Trip> maybeTrip = ts.findById(tripId);
        if(!maybeTrip.isPresent()){
            return new ModelAndView("404");
        }
        Trip trip = maybeTrip.get();
        if(!(trip.getAdminId() == user.getId() || trip.getUsers().contains(user))){
            return  new ModelAndView("403");
        }

        TripComment tripComment = tcs.create(user, trip, tripCommentForm.getComment());
        ts.addCommentToTrip(tripComment.getId(), tripId);
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}/rate/{rate}", method = {RequestMethod.POST})
    public @ResponseBody
    String ajaxTest(@ModelAttribute("user") User user,
                    @PathVariable("tripId") long tripId,
                    @PathVariable("rate") int rate){

        Optional<Trip> maybeTrip = ts.findById(tripId);
        if(!maybeTrip.isPresent()){
            return "trip doest exist";
        }

        Trip trip = maybeTrip.get();
        if(!(trip.getAdminId() == user.getId() || trip.getUsers().contains(user))){
            return  "Permission Denied";
        }

        Optional<TripRate> maybeTripRate = tripRateService.findByUserAndTrip(tripId, user.getId());
        if(maybeTripRate.isPresent()){
            TripRate tripRate = maybeTripRate.get();
            if(tripRate.getRate() != rate){
                tripRate.setRate(rate);
                tripRateService.update(tripRate);
                return "updated";
            }
            else {
                return "rate doest change";
            }
        } else {
            tripRateService.create(user, maybeTrip.get(), rate);
            return "rate created";
        }
    }
}