package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Activity;

import java.time.LocalDate;

public class ActivityDTO {

    private long id;
    private String name;
    private PlaceDTO place;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;

    public ActivityDTO() {
        // Empty constructor needed by JAX-RS
    }

    public ActivityDTO(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.place = new PlaceDTO(activity.getPlace());
        this.category = activity.getCategory();
        this.startDate = activity.getStartDate();
        this.endDate = activity.getEndDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaceDTO getPlace() {
        return place;
    }

    public void setPlace(PlaceDTO place) {
        this.place = place;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
