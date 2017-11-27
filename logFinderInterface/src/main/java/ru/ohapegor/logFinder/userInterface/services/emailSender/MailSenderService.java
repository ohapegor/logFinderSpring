package ru.ohapegor.logFinder.userInterface.services.emailSender;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.ohapegor.logFinder.userInterface.services.jmsSender.JmsMessage;

@Service
public class MailSenderService {

    private static final Logger logger = LogManager.getLogger();

    private JavaMailSender javaMailSender;

    @Autowired
    public MailSenderService(JavaMailSender javaMailSender) {
        logger.info("Constructing MailSender with JavaMailSender = " + javaMailSender);
        this.javaMailSender = javaMailSender;
    }

    @JmsListener(containerFactory = "myContainerFactory",
                destination = "jms/TestJMSQueue")
    public void processMessage(JmsMessage jmsMessage) {
        logger.info("MailSender received message = " + jmsMessage.getMessage()+" to "+jmsMessage.getAddress());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ohapegor2704@gmail.com");
        message.setTo(jmsMessage.getAddress());
        message.setSubject("Message from logFinder's SuperAdmin");
        message.setText(jmsMessage.getMessage());
        javaMailSender.send(message);
    }
}
