package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class EditProfileImageForm {

    public EditProfileImageForm() {
        // Empty constructor needed by JAX-RS
    }

    @NotNull
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
