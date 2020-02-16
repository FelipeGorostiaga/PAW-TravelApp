package ar.edu.itba.paw.webapp.exceptions;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class DTOValidationException extends Exception{

    private final Set<? extends ConstraintViolation<?>> constraintViolations;

    public DTOValidationException(final String message, final Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
    }

    public Set<? extends ConstraintViolation<?>> getConstraintViolations() {
        return constraintViolations;
    }

}
