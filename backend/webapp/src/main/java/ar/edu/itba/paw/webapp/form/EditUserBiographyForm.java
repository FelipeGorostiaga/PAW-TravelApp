package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class EditUserBiographyForm {

    public EditUserBiographyForm() {
        // Empty constructor needed by JAX-RS
    }

    @Size(max = 500)
    private String biography;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

}
