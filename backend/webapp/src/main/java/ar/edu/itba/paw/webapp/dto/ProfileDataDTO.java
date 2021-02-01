package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import java.util.List;

public class ProfileDataDTO {

    private UserDTO user;
    private List<RateDTO> rates;
    private List<TripDTO> dueTrips;
    private List<TripDTO> activeTrips;
    private List<TripDTO> completedTrips;

    public ProfileDataDTO() {
        // Empty constructor needed by JAX-RS
    }

    public ProfileDataDTO(User user, List<RateDTO> rates, List<TripDTO> dueTrips, List<TripDTO> activeTrips, List<TripDTO> completedTrips) {
        this.user = new UserDTO(user);
        this.rates = rates;
        this.dueTrips = dueTrips;
        this.activeTrips = activeTrips;
        this.completedTrips = completedTrips;
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

    public List<TripDTO> getDueTrips() {
        return dueTrips;
    }

    public void setDueTrips(List<TripDTO> dueTrips) {
        this.dueTrips = dueTrips;
    }

    public List<TripDTO> getActiveTrips() {
        return activeTrips;
    }

    public void setActiveTrips(List<TripDTO> activeTrips) {
        this.activeTrips = activeTrips;
    }

    public List<TripDTO> getCompletedTrips() {
        return completedTrips;
    }

    public void setCompletedTrips(List<TripDTO> completedTrips) {
        this.completedTrips = completedTrips;
    }
}
