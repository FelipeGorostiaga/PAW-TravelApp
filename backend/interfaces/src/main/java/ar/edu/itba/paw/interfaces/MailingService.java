package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;

public interface MailingService {

    void sendRegisterMail(User user);

    void sendJoinTripMail(User tripMember, Trip trip);

    void sendExitTripMail(User tripMember, Trip trip);

    void sendDeleteTripMail(Trip trip);

    void sendJoinRequestMail(Trip t, User user, String token);

    void sendEditedJoinRequestMail(Trip trip, User requester, User loggedUser, boolean accepted);

    void sendTripInviteMail(Trip trip, User invitedUser, User admin, String token);
}
