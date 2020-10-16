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

    @Size(min = 5, max = 100)
    private String placeInput;

    @Size(min = 5, max = 50)
    private String name;

    @Size(min = 25, max = 100)
    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private String startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private String endDate;

    @NotNull
    private boolean isPrivate;

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
        return  now.isBefore(sDate) && sDate.isBefore(eDate);
    }

    @Override
    public String toString() {
        return "TripCreateForm{" +
                "placeInput='" + placeInput + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
