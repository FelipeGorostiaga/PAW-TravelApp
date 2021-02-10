package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripComment;

import java.time.LocalDateTime;

public class TripCommentDTO {

    private String comment;
    private UserDTO user;
    private long tripId;
    private LocalDateTime createdOn;

    public TripCommentDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripCommentDTO(TripComment tripComment) {
        this.comment = tripComment.getComment();
        this.user = new UserDTO(tripComment.getUser());
        this.tripId = tripComment.getTrip().getId();
        this.createdOn = tripComment.getCreatedOn();
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

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
