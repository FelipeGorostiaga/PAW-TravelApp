package ar.edu.itba.paw.webapp.dto.constraints;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConstraintViolationsDTO {

    private ConstraintViolationDTO[] errors;

    public ConstraintViolationsDTO() {
        // Empty constructor needed by JAX-RS
    }

    public <T> ConstraintViolationsDTO(Set<ConstraintViolation<T>> violations) {
        List<ConstraintViolationDTO> errorsList = new ArrayList<>(violations.size());
        this.errors =  new ConstraintViolationDTO[violations.size()];
        for(ConstraintViolation<T> violation : violations) {
            errorsList.add(new ConstraintViolationDTO(violation.getMessage(), violation.getPropertyPath().toString()));
        }
        errorsList.toArray(errors);
    }

    public ConstraintViolationDTO[] getErrors() {
        return errors;
    }

    public void setErrors(ConstraintViolationDTO[] errors) {
        this.errors = errors;
    }


}
