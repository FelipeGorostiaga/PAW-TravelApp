package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class UserRateForm {

    @NotNull
    private long tripId;

    @NotNull
    private long ratedUserId;

    @NotNull
    private long ratedById;

    @NotNull
    private String comment;

    @NotNull
    private int rate;

    public UserRateForm() {

    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getRatedUserId() {
        return ratedUserId;
    }

    public void setRatedUserId(long ratedUserId) {
        this.ratedUserId = ratedUserId;
    }

    public long getRatedById() {
        return ratedById;
    }

    public void setRatedById(long ratedById) {
        this.ratedById = ratedById;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
