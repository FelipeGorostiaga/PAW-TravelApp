package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TripInvitation;

public class TripInvitationDTO {


    private TripDTO trip;

    private UserDTO inviter;

    private UserDTO invitee;

    private String token;

    private boolean responded;

    TripInvitationDTO() {

    }

    public TripInvitationDTO(TripInvitation invitation) {
        this.inviter = new UserDTO(invitation.getInviter());
        this.invitee = new UserDTO(invitation.getInvitee());
        this.token = invitation.getToken();
        this.responded = invitation.isResponded();
        this.trip = new TripDTO(invitation.getTrip());
    }

    public TripDTO getTrip() {
        return trip;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    public UserDTO getInviter() {
        return inviter;
    }

    public void setInviter(UserDTO inviter) {
        this.inviter = inviter;
    }

    public UserDTO getInvitee() {
        return invitee;
    }

    public void setInvitee(UserDTO invitee) {
        this.invitee = invitee;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isResponded() {
        return responded;
    }

    public void setResponded(boolean responded) {
        this.responded = responded;
    }
}
