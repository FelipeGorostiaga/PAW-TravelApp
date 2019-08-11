package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditTripForm {

    @NotNull
    private MultipartFile imageUpload;

    public MultipartFile getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(MultipartFile imageUpload) {
        this.imageUpload = imageUpload;
    }
}
