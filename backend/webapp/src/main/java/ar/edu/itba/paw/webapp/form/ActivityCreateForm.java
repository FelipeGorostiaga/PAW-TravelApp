package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DateManipulation;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class ActivityCreateForm {


    @Size(min = 3, max = 40)
    private String name;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]+")
    @Size(min = 3, max = 40)
    private String category;

    @Size(min = 3, max = 100)
    private String placeInput;

    @NotNull
    @Size(min = 8, max = 10)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String startDate;

    @NotNull
    @Size(min = 8, max = 10)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String endDate;

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

    public boolean checkDates(LocalDate tripStart, LocalDate tripEnd) {
        LocalDate sDate = DateManipulation.stringToLocalDate(startDate);
        LocalDate eDate = DateManipulation.stringToLocalDate(endDate);
        LocalDate now = LocalDate.now();
        return  (sDate.isAfter(tripStart) || sDate.isEqual(tripStart)) && (eDate.isBefore(tripEnd) || eDate.isEqual(tripEnd))
        && now.isBefore(sDate) && sDate.isBefore(eDate);
    }

    public boolean checkTimeline(List<Activity> activities) {
        LocalDate sDate = DateManipulation.stringToLocalDate(startDate);
        LocalDate eDate = DateManipulation.stringToLocalDate(endDate);
        for(Activity activity : activities) {
            if(  (sDate.isBefore(activity.getStartDate()) && eDate.isAfter(activity.getStartDate()))
                    || (sDate.isBefore(activity.getEndDate()) && eDate.isAfter(activity.getEndDate()))
                    || (sDate.isBefore(activity.getStartDate()) && eDate.isAfter(activity.getEndDate()))
                    || (sDate.isAfter(activity.getStartDate()) && eDate.isBefore(activity.getEndDate())) ) {
                return false;
            }
        }
        return true;
    }
}
