package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityListDTO {

    List<ActivityDTO> activities;

    public ActivityListDTO() {

    }

    public ActivityListDTO(List<Activity> list) {
        this.activities = new ArrayList<>();
        list.forEach(a -> this.activities.add(new ActivityDTO(a)));
    }

    public List<ActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityDTO> activities) {
        this.activities = activities;
    }
}
