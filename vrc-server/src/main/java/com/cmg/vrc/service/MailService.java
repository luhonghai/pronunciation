package com.cmg.vrc.service;

import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.sphinx.SummaryReport;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 5/8/15.
 */
public class MailService {

    private static final Logger logger = Logger.getLogger(MailService.class.getName());

    private SummaryReport.MessageListener messageListener;

    private void log(String message) {
        log(Level.INFO, message, null);
    }

    private void log(Level level, String message) {
        log(level, message, null);
    }

    private void log(Level level, String message, Throwable ex) {
        if (messageListener != null) {
            if (ex != null || level != Level.INFO) {
                if (ex != null) {
                    String error = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(ex);
                    //String error = ex.getMessage();
                    error = error.replace("\\n", "<br/>");
                    messageListener.onError(message + ". Message: " + error);
                } else {
                    messageListener.onError(message );
                }

            } else {
                messageListener.onMessage(message);
            }
        }
        if (ex != null) {
            logger.log(level, message, ex);
        } else {
            logger.log(level, message);
        }
    }

    public void sendEmail(String[] recipients, String subject, String message) throws MessagingException {
        sendEmail(recipients, subject, message, null);
    }

    public void sendEmail(String[] recipients, String subject, String message, String[] attachFiles)
            throws javax.mail.MessagingException {
        if (Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER).length() == 0
                || Configuration.getValue(Configuration.CONTACT_US_EMAIL_PASSWORD).length() == 0
                || Configuration.getValue(Configuration.RECIPIENTS).length() == 0)
            return;
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "465";
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.user", Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER));
        properties.put("mail.password", Configuration.getValue(Configuration.CONTACT_US_EMAIL_PASSWORD));

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER),
                        Configuration.getValue(Configuration.CONTACT_US_EMAIL_PASSWORD));
            }
        };

        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(Configuration.getValue(Configuration.CONTACT_US_EMAIL_SENDER)));
        for (String recipient : recipients) {
            recipient = recipient.trim();
            if (recipient.length() > 0) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
                        recipient));
            }
        }
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        if (attachFiles != null && attachFiles.length > 0) {
            for (String attachFile : attachFiles) {
                // adds attachments
                MimeBodyPart attachPart = new MimeBodyPart();
                try {

                    attachPart.attachFile(attachFile);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                multipart.addBodyPart(attachPart);
            }
        }
        // sets the multi-part as e-mail's content
        msg.setContent(multipart);
        // sends the e-mail
        Transport.send(msg, msg.getAllRecipients());
        log(Level.ALL, "done");
    }

    public void setMessageListener(SummaryReport.MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void sendActivationEmail(String email, String activationCode) throws MessagingException {
        sendEmail(new String[] {
                email
        }
                ,"accenteasy - account activation"
                ,"Please select the link below to activate your account: <br> " +
                Configuration.getValue(Configuration.URL_ACTIVE_ACCOUNT) + "?acc=" + activationCode
                + "<br> Or enter the following activation code on your mobile device: <b>" + activationCode + "</b>"
                + "<br><br>Thank you and Best regards,<br>Admin accenteasy");
    }

    public static void main(String[] args) throws MessagingException {
        // Send mail with code
        MailService mailService = new MailService();
        mailService.sendEmail(new String[] {
              "luhonghai@gmail.com"
        }
                ,"accenteasy - account activation"
                ,"Please select the link below to activate your account: <br> " +
                Configuration.getValue(Configuration.URL_ACTIVE_ACCOUNT) + "?acc=" + "ABC1235BE135"
                + "<br> Or enter the following activation code on your mobile device: <b>" + "ABC1235BE135" + "</b>"
                + "<br><br>Thank you and Best regards,<br>Admin accenteasy");
    }
}
