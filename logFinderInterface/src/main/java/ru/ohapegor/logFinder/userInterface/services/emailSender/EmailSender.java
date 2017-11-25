package ru.ohapegor.logFinder.userInterface.services.emailSender;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.userInterface.services.jmsSender.JmsMessage;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.jms.Message;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

@MessageDriven(activationConfig = { //  Настройка
        @ActivationConfigProperty(propertyName = "destinationType", // очереди
                propertyValue = "javax.jms.Queue"), // сообщений
        @ActivationConfigProperty(propertyName = "destinationLookup",// в
                propertyValue = "jms/TestJMSQueue") // компоненте
})
public class EmailSender implements MessageListener {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void onMessage(Message message) {
        logger.info("Entering EmailSender.onMessage()");
        try {
            JmsMessage jmsMessage = (JmsMessage) ((ObjectMessage) message).getObject();
            String messageToSend = jmsMessage.getMessage();
            String address = jmsMessage.getAddress();

            String host = "smtp.gmail.com";
            String serviceUsername = "logfinderadm@gmail.com";
            String servicePassword = "superadmin1";

            Properties props = new Properties();
            props.put("mail.smtp.user", serviceUsername);
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.debug", "true");
            props.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(serviceUsername,servicePassword);
                }
            });

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(serviceUsername));
            msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(address));
            msg.setSubject("Message from logFinder admin");
            msg.setText(messageToSend);
            Transport.send(msg);
            logger.info("Successfully send message : " + messageToSend + " to address " + address);
        } catch (Exception e) {
           logger.error("Exception in EmailSender.onMessage() : " + getStackTrace(e));
        }

    }

}
