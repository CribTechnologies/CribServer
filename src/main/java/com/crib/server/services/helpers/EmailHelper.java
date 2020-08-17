package com.crib.server.services.helpers;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailHelper {

    private static Mailer mailer;

    private static String senderEmail;
    private static String senderName;

    static {
        senderEmail = "crib@gmail.com";
        senderName = "Crib";

        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 25, senderEmail, "PASSWORD")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
    }

    public static void sendPlainTextEmail(String toName, String toEmail, String subject, String message) {
        Email email = EmailBuilder
                .startingBlank()
                .from(senderName, senderEmail)
                .to(toName, toEmail)
                .withSubject(subject)
                .withPlainText(message)
                .buildEmail();

        mailer.sendMail(email);
    }
}
