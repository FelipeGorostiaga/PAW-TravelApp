package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.exceptions.DTOValidationException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public class ConstraintValidatorDTO {


    @Autowired
    private Validator validator;

    public <T> void validate(T dto, String message, Class<?>... groups) throws DTOValidationException {
        final Set<ConstraintViolation<T>> constraintViolations = validator.validate(dto, groups);

        if (!constraintViolations.isEmpty())
            throw new DTOValidationException(message, constraintViolations);
    }


}
