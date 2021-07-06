package ar.edu.itba.paw.webapp.dto.constraint;

import javax.validation.ConstraintViolation;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ConstraintViolationsDTO {

    private final List<ConstraintViolationDTO> errors = new LinkedList<>();

    public ConstraintViolationsDTO() {
        // Empty constructor needed by JAX-RS
    }

    public <T> ConstraintViolationsDTO(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            add(violations);
        }
    }

    public <T> void add(Set<ConstraintViolation<T>> violations) {
        if (violations != null) {
            for (ConstraintViolation<T> violation : violations) {
                errors.add(new ConstraintViolationDTO(violation.getMessage(), violation.getPropertyPath().toString()));
            }
        }
    }

    public <T> void add(ConstraintViolationDTO violationDTO) {
        errors.add(violationDTO);
    }

    public List<ConstraintViolationDTO> getErrors() {
        return errors;
    }

}
