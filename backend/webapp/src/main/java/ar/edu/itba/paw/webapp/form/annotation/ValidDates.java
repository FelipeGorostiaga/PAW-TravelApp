package ar.edu.itba.paw.webapp.form.annotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ActivityDatesValidator.class})
public @interface ValidDates {

    String message() default "Invalid dates, start date must be before end date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};



}
