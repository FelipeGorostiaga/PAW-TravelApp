package ar.edu.itba.paw.service;


import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DataPair;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActivityDao ad;

    @Override
    public Optional<Activity> findById(long id) {
        return ad.findById(id);
    }

    @Override
    public Optional<Activity> findByName(String name) {
        return ad.findByName(name);
    }

    @Override
    public Activity create(String name, String category, Place place, Trip trip, LocalDate startDate, LocalDate endDate) {
        return ad.create(name, category, place, trip, startDate, endDate);
    }

    @Override
    public List<DataPair<Activity, Place>> getTripActivitiesDetails(Trip trip) {
        List<Activity> activities = ad.getTripActivities(trip.getId());
        Collections.sort(activities);
        List<DataPair<Activity, Place>> dataPairList = new ArrayList<>();
        for(Activity activity : activities) {
            Place place = activity.getPlace();
            dataPairList.add(new DataPair<>(activity, place));

        }
        return dataPairList;
    }

    @Override
    public Optional<Activity> findByCategory(String category) {
        return ad.findByCategory(category);
    }

}
