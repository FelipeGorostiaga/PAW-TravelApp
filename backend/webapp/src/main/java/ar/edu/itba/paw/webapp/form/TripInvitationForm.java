package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class TripInvitationForm {

    @NotNull
    private long userId;

    @NotNull
    private long tripId;

    public TripInvitationForm() {

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }
}
