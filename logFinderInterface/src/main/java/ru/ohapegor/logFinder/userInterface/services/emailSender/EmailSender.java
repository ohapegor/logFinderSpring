package ru.ohapegor.logFinder.userInterface.services.emailSender;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ohapegor.logFinder.userInterface.services.jmsSender.JmsMessage;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.*;

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
            JmsMessage jmsMessage =  (JmsMessage) ((ObjectMessage)message).getObject();
            String messageToSend = jmsMessage.getMessage();
            String address = jmsMessage.getAddress();
            //todo
            //send email
            logger.info("Successfully received message : "+messageToSend+" to address "+ address);
        } catch (JMSException e) {
            logger.error("Exception in EmailSender.onMessage() : "+getStackTrace(e));
        }

    }
}
