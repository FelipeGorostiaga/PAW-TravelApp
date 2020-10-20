package ar.edu.itba.paw.webapp.form.annotation;

import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.webapp.form.ActivityCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.time.LocalDate;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

public class ActivityDatesValidator implements ConstraintValidator<ValidDates, ActivityCreateForm> {

    @Autowired
    private ApplicationContext applicationContext;

    private String errorMessage;

    @Override
    public void initialize(ValidDates constraintAnnotation) {
        errorMessage = applicationContext.getMessage("activityDatesError", null, getLocale());
    }

    @Override
    public boolean isValid(ActivityCreateForm form, ConstraintValidatorContext context) {
        //disable existing violation message
        context.disableDefaultConstraintViolation();
        //build new violation message and add it
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        LocalDate sDate = DateManipulation.stringToLocalDate(form.getStartDate());
        LocalDate eDate = DateManipulation.stringToLocalDate(form.getEndDate());
        if(sDate == null || eDate == null) {
            System.out.println("Couldn't parse dates, returning false...");
            return false;
        }
        return (sDate.isBefore(eDate) || sDate.isEqual(eDate));
    }


}
