package ar.edu.itba.paw.webapp.dto.constraint;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ConstraintViolationsDTO {

    private ConstraintViolationDTO[] errors;

    public ConstraintViolationsDTO() {
        // Empty constructor needed by JAX-RS
    }

    public <T> ConstraintViolationsDTO(Set<ConstraintViolation<T>> violations) {
        if(!violations.isEmpty()) {
            add(violations);
        }
    }

    public <T> void add(Set<ConstraintViolation<T>> violations) {
        List<ConstraintViolationDTO> errorsList = new ArrayList<>(violations.size());
        this.errors =  new ConstraintViolationDTO[violations.size()];
        for(ConstraintViolation<T> violation : violations) {
            errorsList.add(new ConstraintViolationDTO(violation.getMessage(), violation.getPropertyPath().toString()));
        }
        errorsList.toArray(errors);
    }

    public <T> void add(ConstraintViolationDTO violationDTO) {
        List<ConstraintViolationDTO> list = Arrays.asList(errors);
        list.add(violationDTO);
        list.toArray(errors);
    }

    public ConstraintViolationDTO[] getErrors() {
        return errors;
    }

    public void setErrors(ConstraintViolationDTO[] errors) {
        this.errors = errors;
    }

}
