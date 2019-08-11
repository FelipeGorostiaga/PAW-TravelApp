package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DataPair;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityService {
    public Optional<Activity> findById(long id);
    public Optional<Activity> findByName(String name);
    public Activity create(String name, String category, Place place, Trip trip, LocalDate startDate, LocalDate endDate);
    public Optional<Activity> findByCategory(String category);
    public List<DataPair<Activity, Place>> getTripActivitiesDetails(Trip trip);
}
