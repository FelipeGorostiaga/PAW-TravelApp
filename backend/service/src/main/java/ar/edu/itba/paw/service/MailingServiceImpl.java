
package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import org.simplejavamail.MailException;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.email.Recipient;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;


@Service
@Transactional
public class MailingServiceImpl implements MailingService {


    private static final Integer PORT = 587;
    private static final String EMAIL_SERVER = "smtp.gmail.com";
    private static final String EMAIL_NAME = "meet.travel.paw@gmail.com";
    private static final String EMAIL_PASS = "power123321";
    private static final Locale locale = getLocale();
    private static final String frontEndURL = "http://localhost:4200/";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    private static final String REGISTER_TEMPLATE = "templates/registerMail.html";
    private static final String JOIN_TRIP_TEMPLATE = "templates/joinTripMail.html";
    private static final String EXIT_TRIP_TEMPLATE = "templates/exitTripMail.html";
    private static final String DELETE_TRIP_TEMPLATE = "templates/deleteTripMail.html";
    private static final String JOIN_REQUEST_TEMPLATE = "templates/newJoinRequest.html";
    private static final String JOIN_REQUEST_ACCEPTED_TEMPLATE = "templates/joinRequestAccepted.html";
    private static final String JOIN_REQUEST_DENIED_TEMPLATE = "templates/joinRequestDenied.html";

    @Async
    @Override
    public void sendRegisterMail(User user, String contextURL) {
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(user.getFirstname() + " " + user.getLastname(), user.getEmail(), null));
        String subject = applicationContext.getMessage("mailRegisterSubject", null, locale);
        String verifyURL = contextURL + "/" + user.getVerificationCode();
        Context ctx = new Context(locale);
        ctx.setVariable("email", user.getEmail());
        ctx.setVariable("name", user.getFirstname());
        ctx.setVariable("lastname", user.getLastname());
        ctx.setVariable("verificationURL", verifyURL);
        String html = htmlTemplateEngine.process(REGISTER_TEMPLATE, ctx);
        sendMail(recipients, html, subject);
    }

    @Async
    @Override
    public void sendJoinTripMail(String emailA, String adminName, String tripName, String firstname, String lastname) {
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(adminName, emailA, null));
        String subject = applicationContext.getMessage("mailJoinSubject", null, locale);
        Context ctx = new Context(locale);
        ctx.setVariable("email", emailA);
        ctx.setVariable("adminname", adminName);
        ctx.setVariable("firstname", firstname);
        ctx.setVariable("lastname", lastname);
        ctx.setVariable("tripname", tripName);
        String html = htmlTemplateEngine.process(JOIN_TRIP_TEMPLATE, ctx);
        sendMail(recipients, html, subject);
    }

    @Async
    @Override
    public void sendExitTripMail(String emailA, String adminName, String tripName, String firstname, String lastname) {
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(adminName, emailA, null));
        String subject = applicationContext.getMessage("mailExitSubject", null, locale);
        Context ctx = new Context(locale);
        ctx.setVariable("email", emailA);
        ctx.setVariable("adminname", adminName);
        ctx.setVariable("firstname", firstname);
        ctx.setVariable("lastname", lastname);
        ctx.setVariable("tripname", tripName);
        String html = htmlTemplateEngine.process(EXIT_TRIP_TEMPLATE, ctx);
        sendMail(recipients, html, subject);
    }

    @Async
    @Override
    public void sendDeleteTripMail(String email, String firstname, String lastname, String tripName) {
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(firstname + " " + lastname, email, null));
        String subject = applicationContext.getMessage("mailDeleteSubject", null, locale);
        Context ctx = new Context(locale);
        ctx.setVariable("email", email);
        ctx.setVariable("firstname", firstname);
        ctx.setVariable("lastname", lastname);
        ctx.setVariable("tripname", tripName);
        String html = htmlTemplateEngine.process(DELETE_TRIP_TEMPLATE, ctx);
        sendMail(recipients, html, subject);
    }

    @Async
    @Override
    public void sendJoinRequestMail(Trip t, User user, String token) {
        String subject = applicationContext.getMessage("mailNewJoinRequestSubject", null, locale);
        String baseURL = frontEndURL + "/trip/" + t.getId() + "/joinRequest/?token=" + token;
        String acceptURL = baseURL + "&accepted=true";
        String denyURL = baseURL + "&accepted=false";
        Context ctx = new Context(locale);
        ctx.setVariable("username", user.getFirstname() + " " + user.getLastname());
        ctx.setVariable("userId", user.getId());
        ctx.setVariable("tripname", t.getName());
        ctx.setVariable("tripId", t.getId());
        ctx.setVariable("acceptedURL", acceptURL);
        ctx.setVariable("deniedURL", denyURL);
        String html = htmlTemplateEngine.process(JOIN_REQUEST_TEMPLATE, ctx);
        List<Recipient> recipients = t.getAdmins().stream()
                .map(u -> new Recipient(u.getFirstname() + u.getLastname(), u.getEmail(), null))
                .collect(Collectors.toList());
        sendMail(recipients, html, subject);
    }

    @Async
    @Override
    public void sendEditedJoinRequestMail(Trip trip, User requester, User loggedUser, boolean accepted) {
        String requesterName = requester.getFirstname() + " " + requester.getLastname();
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(new Recipient(requesterName, requester.getEmail(), null));
        Context ctx = new Context(locale);
        ctx.setVariable("requester", requesterName);
        ctx.setVariable("admin", loggedUser.getFirstname() + " " + loggedUser.getLastname());
        ctx.setVariable("tripname", trip.getName());
        String subject;
        String html;
        if (accepted) {
            subject = applicationContext.getMessage("mailAcceptedJoinRequestSubject", null, locale);
            html = htmlTemplateEngine.process(JOIN_REQUEST_ACCEPTED_TEMPLATE, ctx);
        }
        else {
            subject = applicationContext.getMessage("mailDeniedJoinRequestSubject", null, locale);
            html = htmlTemplateEngine.process(JOIN_REQUEST_DENIED_TEMPLATE, ctx);
        }
        sendMail(recipients, html, subject);
    }


    private void sendMail(List<Recipient> recipients, String html, String subject) {
        try {
            Email email = EmailBuilder.startingBlank()
                    .to(recipients).to()
                    .from("Meet and Travel", "meet.travel.paw@gmail.com")
                    .withSubject(subject)
                    .withHTMLText(html)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer(EMAIL_SERVER, PORT, EMAIL_NAME, EMAIL_PASS)
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria()
                    .withProperty("mail.smtp.sendpartial", "true")
                    .buildMailer();

            mailer.sendMail(email, true);
        } catch (MailException ignored) {
            // ignored
        }
    }
}

