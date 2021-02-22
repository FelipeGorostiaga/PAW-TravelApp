package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;

public interface MailingService {

    public void sendRegisterMail(User user);

    public void sendJoinTripMail(User tripMember, Trip trip);

    public void sendExitTripMail(User tripMember, Trip trip);

    public void sendDeleteTripMail(Trip trip);

    public void sendJoinRequestMail(Trip t, User user, String token);

    public void sendEditedJoinRequestMail(Trip trip, User requester, User loggedUser, boolean accepted);

    public void sendTripInviteMail(Trip trip, User invitedUser, User admin, String token);
}
