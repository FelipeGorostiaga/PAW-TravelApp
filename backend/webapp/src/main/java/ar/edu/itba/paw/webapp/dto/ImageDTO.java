package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripPicture;
import ar.edu.itba.paw.model.UserPicture;

import java.awt.*;

public class ImageDTO {

    private long id;
    private byte[] image;

    public ImageDTO() {
        // Empty constructor needed by JAX-RS
    }

    public ImageDTO(TripPicture tripPicture) {
        id = tripPicture.getId();
        image = tripPicture.getPicture();
    }

    public ImageDTO(UserPicture userPicture) {
        id = userPicture.getId();
        image = userPicture.getPicture();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
