package com.crib.server.services.helpers;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class EmailHelper {

    private Mailer mailer;

    public EmailHelper() {
        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.host.com", 587, "user@host.com", "password")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withProxy("socksproxy.host.com", 1080, "proxy user", "proxy password")
                .withSessionTimeout(10 * 1000)
                .clearEmailAddressCriteria()
                .withProperty("mail.smtp.sendpartial", true)
                .withDebugLogging(true)
                .async()
                .buildMailer();
    }

    public void sendPlainTextEmail(String fromName, String fromEmail, String toName, String toEmail, String subject, String message) {
        Email email = EmailBuilder
                .startingBlank()
                .from(fromName, fromEmail)
                .to(toName, toEmail)
                .withSubject(subject)
                .withPlainText(message)
                .buildEmail();

        mailer.sendMail(email);
    }
}
