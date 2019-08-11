package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.DateManipulation;

import java.time.LocalDate;

public class AdvancedSearchForm {

    private String tripName;
    private String placeName;
    private String startDate;
    private String endDate;
    private String category;

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean validateDates() {
        LocalDate sDate = DateManipulation.stringToLocalDate(startDate);
        LocalDate eDate = DateManipulation.stringToLocalDate(endDate);
        return  sDate.isBefore(eDate);
    }
}
