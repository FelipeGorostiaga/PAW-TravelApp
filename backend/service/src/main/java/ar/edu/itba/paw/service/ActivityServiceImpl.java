package ar.edu.itba.paw.service;


import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.interfaces.GoogleMapsService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.walkercrou.places.exception.GooglePlacesException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao ad;

    @Autowired
    private TripService tripService;

    @Autowired
    private GoogleMapsService googleMapsService;

    @Override
    public Optional<Activity> findById(final long id) {
        return ad.findById(id);
    }

    @Override
    public Optional<Activity> findByName(final String name) {
        return ad.findByName(name);
    }

    @Override
    public Activity create(String name, String category, double latitude, double longitude, Trip trip, LocalDate startDate, LocalDate endDate,
                           String description, String placeInput) throws GooglePlacesException {
        List<se.walkercrou.places.Place> googleMapsPlaces = googleMapsService.queryGoogleMapsPlaces(latitude, longitude);
        Place place = googleMapsService.createGooglePlaceReference(googleMapsPlaces, placeInput, latitude, longitude);
        Activity activity = ad.create(name, category, place, trip, startDate, endDate, description);
        tripService.addActivityToTrip(activity.getId(), trip.getId());
        return activity;
    }

    @Override
    public List<Activity> getTripActivities(final long tripId) {
        return ad.getTripActivities(tripId);
    }

}
