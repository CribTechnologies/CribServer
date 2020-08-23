package com.crib.server.services.helpers;

import com.crib.server.EnvVariables;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailHelper {

    private static EmailHelper singleInstance;
    private EnvVariables envVariables;
    private Session session;

    private EmailHelper() {
        envVariables = EnvVariables.getInstance();

        Properties props = System.getProperties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(envVariables.EMAIL, envVariables.PASSWORD);
            }
        });
        session.setDebug(false);
    }

    public static EmailHelper getInstance() {
        if (singleInstance == null)
            singleInstance = new EmailHelper();
        return singleInstance;
    }

    public boolean sendEmail(String toEmail, String subject, String message) {
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(envVariables.EMAIL));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
            return true;
        }
        catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
