package ar.edu.itba.paw.webapp.form.annotation;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.UserCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Optional;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, UserCreateForm> {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationContext applicationContext;

    private String errorMessage;


    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        errorMessage = applicationContext.getMessage("uniqueUsernameError", null, getLocale());
    }

    @Override
    public boolean isValid(UserCreateForm form, ConstraintValidatorContext context) {
        //disable existing violation message
        context.disableDefaultConstraintViolation();
        //build new violation message and add it
        context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
        Optional<User> userDuplicate = userService.findByUsername(form.getEmail());
        return !userDuplicate.isPresent();

    }
}
