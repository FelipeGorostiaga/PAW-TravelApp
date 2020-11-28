package ar.edu.itba.paw.webapp.dto;

public class ErrorDTO {

    private String message;
    private String invalidField;

    public ErrorDTO() {

    }

    public ErrorDTO(final String message, final String field) {
        this.message = message;
        this.invalidField = field;
    }

    public String getInvalidField() {
        return invalidField;
    }

    public void setInvalidField(String invalidField) {
        this.invalidField = invalidField;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
