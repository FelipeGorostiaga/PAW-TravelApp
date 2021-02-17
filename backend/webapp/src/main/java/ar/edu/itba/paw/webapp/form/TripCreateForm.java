package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.DateManipulation;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class TripCreateForm {

    public TripCreateForm() {
        // Empty constructor needed by JAX-RS
    }

    @Size(min = 5, max = 15)
    private String name;

    @Size(min = 25, max = 400)
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String startDate;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String endDate;

    @NotNull
    private boolean isPrivate;

    @Size(max = 200)
    private String placeInput;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    @NotNull
    private String googlePlaceId;

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPlaceInput() {
        return placeInput;
    }

    public void setPlaceInput(String placeInput) {
        this.placeInput = placeInput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean validateDates() {
        if(!(DateManipulation.validate(startDate) || DateManipulation.validate(endDate))){
            return false;
        }
        LocalDate sDate = DateManipulation.stringToLocalDate(startDate);
        LocalDate eDate = DateManipulation.stringToLocalDate(endDate);
        LocalDate now = LocalDate.now();
        return  (now.isBefore(sDate) || now.isEqual(sDate)) && sDate.isBefore(eDate);
    }

    @Override
    public String toString() {
        return "TripCreateForm{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", isPrivate=" + isPrivate +
                ", placeInput='" + placeInput + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", googlePlaceId='" + googlePlaceId + '\'' +
                '}';
    }
}
