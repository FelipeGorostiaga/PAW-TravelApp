package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;

public interface MailingService {

    void sendRegisterMail(User user, String contextURL);

    void sendJoinTripMail(String emailA, String adminName, String tripName, String firstname, String lastname);

    void sendExitTripMail(String emailA, String adminName, String tripName, String firstname, String lastname);

    void sendDeleteTripMail(String email, String firstname, String lastname, String tripName);

    void sendJoinRequestMail(Trip t, User user, String token);

    void sendEditedJoinRequestMail(Trip trip, User requester, User loggedUser, boolean accepted);

}
