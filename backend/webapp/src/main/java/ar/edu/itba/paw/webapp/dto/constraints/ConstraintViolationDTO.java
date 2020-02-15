package ar.edu.itba.paw.webapp.dto.constraints;

public class ConstraintViolationDTO {

    private String message;
    private String field;

    public ConstraintViolationDTO() {
        // Empty constructor needed by JAX-RS
    }

    public ConstraintViolationDTO(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
