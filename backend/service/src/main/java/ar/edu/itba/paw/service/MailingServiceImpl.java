package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import org.simplejavamail.MailException;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
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

import java.util.Locale;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;


@Service
@Transactional
public class MailingServiceImpl implements MailingService {

    private static final Integer PORT = 587;
    private static final String EMAIL_SERVER = "smtp.gmail.com";
    private static final String EMAIL_NAME = "meet.travel.paw@gmail.com";
    private static final String EMAIL_PASS = "power123321";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    private static final String REGISTER_TEMPLATE = "templates/registerMail.html";
    private static final String JOIN_TRIP_TEMPLATE = "templates/joinTripMail.html";
    private static final String EXIT_TRIP_TEMPLATE = "templates/exitTripMail.html";
    private static final String DELETE_TRIP_TEMPLATE = "templates/deleteTripMail.html";


    @Async
    @Override
    public void sendRegisterMail(String emailName, String name, String lastname, Locale locale) {
        String subject = applicationContext.getMessage("mailRegisterSubject", null, locale);
        final Context ctx = new Context(locale);
        ctx.setVariable("email", emailName);
        ctx.setVariable("name", name);
        ctx.setVariable("lastname", lastname);
        String html = htmlTemplateEngine.process(REGISTER_TEMPLATE, ctx);
        sendMail(name + " " + lastname, emailName, html, subject, ctx);
    }

    private void sendMail(String recieverName, String recieverEmail, String html, String subject, Context ctx) {
        try {
            Email email = EmailBuilder.startingBlank()
                    .to(recieverName, recieverEmail)
                    .from("Meet and Travel", "meet.travel.paw@gmail.com")
                    .withSubject(subject)
                    .withHTMLText(html)
                    .buildEmail();

            Mailer mailer = MailerBuilder
                    .withSMTPServer(EMAIL_SERVER, PORT, EMAIL_NAME , EMAIL_PASS)
                    .withTransportStrategy(TransportStrategy.SMTP_TLS)
                    .withSessionTimeout(10 * 1000)
                    .clearEmailAddressCriteria()
                    .withProperty("mail.smtp.sendpartial", "true")
                    .buildMailer();

            mailer.sendMail(email, true);
        } catch (MailException ignored) {

        }
    }

    @Async
    @Override
    public void sendJoinTripMail(String emailA, String adminName, String tripName, String firstname, String lastname,
                                 Locale locale) {
        String subject = applicationContext.getMessage("mailJoinSubject", null, locale);
        final Context ctx = new Context(locale);
        ctx.setVariable("email", emailA);
        ctx.setVariable("adminname", adminName);
        ctx.setVariable("firstname", firstname);
        ctx.setVariable("lastname", lastname);
        ctx.setVariable("tripname", tripName);
        String html = htmlTemplateEngine.process(JOIN_TRIP_TEMPLATE, ctx);
        sendMail(adminName, emailA, html, subject, ctx);
    }

    @Async
    @Override
    public void sendExitTripMail(String emailA, String adminName, String tripName, String firstname, String lastname,
                                 Locale locale) {

        String subject = applicationContext.getMessage("mailExitSubject", null, locale);
        final Context ctx = new Context(locale);
        ctx.setVariable("email", emailA);
        ctx.setVariable("adminname", adminName);
        ctx.setVariable("firstname", firstname);
        ctx.setVariable("lastname", lastname);
        ctx.setVariable("tripname", tripName);
        String html = htmlTemplateEngine.process(EXIT_TRIP_TEMPLATE, ctx);
        sendMail(adminName, emailA, html, subject, ctx);
    }

    @Async
    @Override
    public void sendDeleteTripMail(String email, String firstname, String lastname, String tripName, Locale locale) {

        String subject = applicationContext.getMessage("mailDeleteSubject", null, locale);
        final Context ctx = new Context(locale);
        ctx.setVariable("email", email);
        ctx.setVariable("firstname", firstname);
        ctx.setVariable("lastname", lastname);
        ctx.setVariable("tripname", tripName);
        String html = htmlTemplateEngine.process(DELETE_TRIP_TEMPLATE, ctx);
        sendMail(firstname + " " + lastname, email, html, subject, ctx);
    }


}