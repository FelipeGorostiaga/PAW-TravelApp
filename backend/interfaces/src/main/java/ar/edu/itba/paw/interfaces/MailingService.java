package ar.edu.itba.paw.interfaces;

import java.util.Locale;

public interface MailingService {

    void sendRegisterMail(String email, String name, String lastname, Locale locale);
    void sendJoinTripMail(String emailA, String adminName, String tripName, String firstname, String lastname,
                                 Locale locale);
    void sendExitTripMail(String emailA, String adminName, String tripName, String firstname, String lastname,
                                 Locale locale);
    void sendDeleteTripMail(String email, String firstname, String lastname, String tripName, Locale locale);
}
