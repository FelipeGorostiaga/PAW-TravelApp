package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class PendingConfirmationForm {


    public PendingConfirmationForm() {
        // Empty constructor needed by JAX-RS
    }

    @NotNull
    private long tripId;

    @NotNull
    private long userId;

    @NotNull
    private long adminId;

    @NotNull
    private boolean accepted;

    @NotNull
    @Length(min = 64, max = 65)
    private String token;

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

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
