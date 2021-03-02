package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripPendingConfirmation;

import java.net.URI;

public class TripPendingConfirmationDTO {

    private TripDTO trip;

    private UserDTO user;

    private boolean accepted;

    private boolean edited;

    public TripPendingConfirmationDTO() {
        // Empty constructor needed by JAX-RS
    }

    public TripPendingConfirmationDTO(TripPendingConfirmation pendingConfirmation, final URI baseUri) {
        this.trip = new TripDTO(pendingConfirmation.getTrip(), baseUri);
        this.user = new UserDTO(pendingConfirmation.getRequestingUser(), baseUri);
        this.accepted = pendingConfirmation.isAccepted();
        this.edited = pendingConfirmation.isEdited();
    }

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

}
