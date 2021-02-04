package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.UserRate;

public class RateDTO {

    private long id;

    private int rate;

    private UserDTO ratedBy;

    private UserDTO ratedUser;

    private String comment;

    private String createdOn;

    public RateDTO() {
        // Empty constructor needed by JAX-RS
    }

    public RateDTO(UserRate rate) {
        this.id = rate.getId();
        this.rate = rate.getRate();
        this.ratedBy = new UserDTO(rate.getRatedByUser());
        this.ratedUser = new UserDTO(rate.getRatedUser());
        this.createdOn = DateManipulation.changeDateTimeFormat(rate.getCreatedOn());
        this.comment = rate.getComment();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public UserDTO getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(UserDTO ratedBy) {
        this.ratedBy = ratedBy;
    }

    public UserDTO getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(UserDTO ratedUser) {
        this.ratedUser = ratedUser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}