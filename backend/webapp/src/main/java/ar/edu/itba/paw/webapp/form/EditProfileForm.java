package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class EditProfileForm {

    public EditProfileForm() {
        // Empty constructor needed by JAX-RS
    }

    @Size(max = 500)
    private String biography;

    private String imageBase64;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
