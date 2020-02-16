package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripComment;

import java.time.LocalDateTime;

public class TripCommentDTO {

    private String comment;
    private UserDTO user;
    private LocalDateTime createdOn;

    public TripCommentDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripCommentDTO(TripComment tripComment) {
        comment = tripComment.getComment();
        user = new UserDTO(tripComment.getUser());
        createdOn = tripComment.getCreatedOn();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
