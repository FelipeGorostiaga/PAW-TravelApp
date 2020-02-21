package ar.edu.itba.paw.webapp.form.annotation;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.webapp.form.ActivityCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

public class TripItineraryValidator implements ConstraintValidator<ValidTripItinerary, ActivityCreateForm> {

    @Autowired
    private ApplicationContext applicationContext;

    private String errorMessage;

    @Override
    public void initialize(ValidTripItinerary constraintAnnotation) {
        errorMessage = applicationContext.getMessage("activityDatesItineraryError", null, getLocale());
    }

    @Override
    public boolean isValid(ActivityCreateForm form, ConstraintValidatorContext context) {
        //disable existing violation message
        context.disableDefaultConstraintViolation();
        //build new violation message and add it
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        LocalDate sDate = DateManipulation.stringToLocalDate(form.getStartDate());
        LocalDate eDate = DateManipulation.stringToLocalDate(form.getEndDate());
        for(Activity activity : form.getTrip().getActivities()) {
            if((sDate.isBefore(activity.getStartDate()) && eDate.isAfter(activity.getStartDate()))
                    || (sDate.isBefore(activity.getEndDate()) && eDate.isAfter(activity.getEndDate()))
                    || (sDate.isBefore(activity.getStartDate()) && eDate.isAfter(activity.getEndDate()))
                    || (sDate.isAfter(activity.getStartDate()) && eDate.isBefore(activity.getEndDate())) ) {
                return false;
            }
        }
        return true;
    }
}
