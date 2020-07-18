package ar.edu.itba.paw.webapp.dto;

public class IntegerDTO {


    private int value;

    public IntegerDTO() {
        // Empty constructor needed by JAX-RS
    }

    public IntegerDTO(int num) {
        this.value = num;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
