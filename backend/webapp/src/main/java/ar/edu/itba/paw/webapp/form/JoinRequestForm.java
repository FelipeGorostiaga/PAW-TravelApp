package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class JoinRequestForm {

    public JoinRequestForm() {
        // Empty constructor needed by JAX-RS
    }

    @NotNull
    private long tripId;

    @NotNull
    private long userId;

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
