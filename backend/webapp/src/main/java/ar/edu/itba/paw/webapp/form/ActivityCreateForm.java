package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.form.annotation.ValidDates;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ValidDates
public class ActivityCreateForm {

    @Size(min = 3, max = 40)
    private String name;

    @Pattern(regexp = "[a-zA-Z]+")
    @Size(min = 3, max = 40)
    private String category;

    @Size(min = 3, max = 100)
    private String placeInput;

    @Size(min = 10, max = 300)
    @NotNull
    private String description;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @Size(min = 8, max = 10)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String startDate;

    @Size(min = 8, max = 10)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String endDate;

    public ActivityCreateForm() {
        // Empty constructor needed by JAX-RS
    }

    private Trip trip;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlaceInput() {
        return placeInput;
    }

    public void setPlaceInput(String placeInput) {
        this.placeInput = placeInput;
    }

    @Override
    public String toString() {
        return "ActivityCreateForm{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", placeInput='" + placeInput + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", trip=" + trip +
                '}';
    }
}
