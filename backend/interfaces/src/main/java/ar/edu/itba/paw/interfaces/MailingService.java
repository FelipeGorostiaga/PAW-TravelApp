package ar.edu.itba.paw.interfaces;

import java.util.Locale;

public interface MailingService {

    public void sendRegisterMail(String email, String name, String lastname, Locale locale);
    public void sendJoinTripMail(String emailA, String adminName, String tripName, String firstname, String lastname,
                                 Locale locale);
    public void sendExitTripMail(String emailA, String adminName, String tripName, String firstname, String lastname,
                                 Locale locale);
    public void sendDeleteTripMail(String email, String firstname, String lastname, String tripName, Locale locale);
}
