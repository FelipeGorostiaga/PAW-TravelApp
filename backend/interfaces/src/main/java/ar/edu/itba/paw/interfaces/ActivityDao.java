package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityDao {

    public Optional<Activity> findById(long id);
    public Optional<Activity> findByName(String name);
    public Activity create(String name, String category, Place place, Trip trip, LocalDate startDate, LocalDate endDate);
    public Optional<Activity> findByCategory(String category);
    public void deleteActivities(long tripId);
    public void deleteActivity(long activityId);
    public List<Activity> getTripActivities(long tripId);
}
