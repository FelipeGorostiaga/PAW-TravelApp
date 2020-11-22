package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;

import java.util.Locale;

public interface MailingService {

    void sendRegisterMail(User user, Locale locale, String contextURL);

    void sendJoinTripMail(String emailA, String adminName, String tripName, String firstname, String lastname, Locale locale);

    void sendExitTripMail(String emailA, String adminName, String tripName, String firstname, String lastname,
                          Locale locale);

    void sendDeleteTripMail(String email, String firstname, String lastname, String tripName, Locale locale);
}
