package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import java.net.URI;
import java.util.List;

public class ProfileDataDTO {

    private UserDTO user;
    private List<RateDTO> rates;
    private int dueTripsAmount;
    private int activeTripsAmount;
    private int completedTripsAmount;

    public ProfileDataDTO() {
        // Empty constructor needed by JAX-RS
    }

    public ProfileDataDTO(User user, List<RateDTO> rates, int dueTripsAmount, int activeTripsAmount, int completedTripsAmount, final URI baseUri) {
        this.user = new UserDTO(user, baseUri);
        this.rates = rates;
        this.dueTripsAmount = dueTripsAmount;
        this.activeTripsAmount = activeTripsAmount;
        this.completedTripsAmount = completedTripsAmount;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<RateDTO> getRates() {
        return rates;
    }

    public void setRates(List<RateDTO> rates) {
        this.rates = rates;
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
