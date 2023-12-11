package com.idunno;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtility {

    public static void sendEmail(String toEmail, String subject, String body) throws IOException {

        Properties props = new Properties();
        /*
         * Use path to a .properties file with the following content:
         * mail.smtp.host= <smtp host server>
         * mail.smtp.port= 587
         * mail.username= <username of sender>
         * mail.password= <password of sender>
         */
        try (InputStream input = new FileInputStream(
                "C:\\Users\\We Are Legion\\Network-Programming\\mail.properties")) {
            props.load(input);
        }

        String username = props.getProperty("mail.username");
        String password = props.getProperty("mail.password");

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(username +"@kth.se");
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
