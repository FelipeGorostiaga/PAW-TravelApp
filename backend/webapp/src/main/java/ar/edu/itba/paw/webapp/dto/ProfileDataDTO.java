package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class ProfileDataDTO {

    private int dueTripsAmount;
    private int activeTripsAmount;
    private int completedTripsAmount;

    public ProfileDataDTO() {
        // Empty constructor needed by JAX-RS
    }

    public ProfileDataDTO(int dueTripsAmount, int activeTripsAmount, int completedTripsAmount) {
        this.dueTripsAmount = dueTripsAmount;
        this.activeTripsAmount = activeTripsAmount;
        this.completedTripsAmount = completedTripsAmount;
    }

    public int getDueTripsAmount() {
        return dueTripsAmount;
    }

    public void setDueTripsAmount(int dueTripsAmount) {
        this.dueTripsAmount = dueTripsAmount;
    }

    public int getActiveTripsAmount() {
        return activeTripsAmount;
    }

    public void setActiveTripsAmount(int activeTripsAmount) {
        this.activeTripsAmount = activeTripsAmount;
    }

    public int getCompletedTripsAmount() {
        return completedTripsAmount;
    }

    public void setCompletedTripsAmount(int completedTripsAmount) {
        this.completedTripsAmount = completedTripsAmount;
    }
}
