package ar.edu.itba.paw.webapp.form.annotation;

import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.webapp.form.UserCreateForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthdayValidator implements ConstraintValidator<ValidBirthday, UserCreateForm> {


    @Override
    public void initialize(ValidBirthday constraintAnnotation) {

    }

    @Override
    public boolean isValid(UserCreateForm value, ConstraintValidatorContext context) {
        LocalDate birthday = DateManipulation.stringToLocalDate(value.getBirthday());
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addNode("birthday").addConstraintViolation().disableDefaultConstraintViolation();
        if(birthday == null) return false;
        return birthday.isBefore(LocalDate.now());
    }


}
