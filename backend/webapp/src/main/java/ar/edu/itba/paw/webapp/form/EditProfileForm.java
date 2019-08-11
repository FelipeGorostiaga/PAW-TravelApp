package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;

public class EditProfileForm {

    @Size(max = 500)
    private String biography;

    private MultipartFile imageUpload;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public MultipartFile getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(MultipartFile imageUpload) {
        this.imageUpload = imageUpload;
    }
}
