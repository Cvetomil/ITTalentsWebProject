package ittalents.webappsports.util;

import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

public class EmailSender {

    private static final String emailSender = "sportproject747@gmail.com";
    private static final String password = "sportal123";


    public static void sendEmail(String emailReceiver, String subject, String text) throws  MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailSender, password);
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("noreply@sportal.bg", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailReceiver));
        msg.setSubject(subject);
        msg.setContent(text, "text/html");
        msg.setSentDate(new Date());

        Transport.send(msg);
    }

    }
