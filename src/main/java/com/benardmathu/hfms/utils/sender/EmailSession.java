package com.benardmathu.hfms.utils.sender;

import com.benardmathu.hfms.config.EmailEnv;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @author bernard
 */
public class EmailSession {
    private final int port = 587;
    private final String host = "smtp.gmail.com";
    private String from = "";
    private String username = "";
    private String password = "";
    private boolean auth = true;
    private Protocol protocol = Protocol.SMTPS;
    private boolean debug = true;

    public EmailSession() {
        EmailEnv env = new EmailEnv();
        Properties properties = env.getProperties();
        this.from = properties.getProperty("email");
        this.username = properties.getProperty("email");
        this.password = properties.getProperty("password");
    }

    public void sendEmail(String to , String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", true);

        Authenticator authenticator = null;
        if (auth) {
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(username, password);
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }

        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);

        Multipart multipart = new MimeMultipart("alternative");

        MimeBodyPart part = new MimeBodyPart();
        part.setContent(body, "text/html");
        multipart.addBodyPart(part);

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        InternetAddress[] address = {new InternetAddress(to)};
        message.setRecipients(Message.RecipientType.TO, address);
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setContent(multipart);
        Transport.send(message);
    }
}
