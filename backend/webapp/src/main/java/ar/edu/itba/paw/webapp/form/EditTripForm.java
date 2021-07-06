package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class EditTripForm {

    public EditTripForm() {
        // Empty constructor needed by JAX-RS
    }

    @NotNull
    private MultipartFile imageUpload;

    public MultipartFile getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(MultipartFile imageUpload) {
        this.imageUpload = imageUpload;
    }
}
