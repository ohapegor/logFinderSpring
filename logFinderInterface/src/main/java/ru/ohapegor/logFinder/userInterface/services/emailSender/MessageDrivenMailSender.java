package ru.ohapegor.logFinder.userInterface.services.emailSender;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static org.apache.commons.lang.exception.ExceptionUtils.getStackTrace;

/*@MessageDriven(activationConfig = { //  Настройка
        @ActivationConfigProperty(propertyName = "destinationType", // очереди
                propertyValue = "javax.jms.Queue"), // сообщений
        @ActivationConfigProperty(propertyName = "destinationLookup",// в
                propertyValue = "jms/TestJMSQueue") // компоненте
})*/
public class MessageDrivenMailSender implements MessageListener {

    private static final Logger logger = LogManager.getLogger();

    private String textMessage = "No messages received yet";

    public String getTextMessage() {
        return textMessage;
    }

    @Override
    public void onMessage(Message message) {
        logger.info("Entering MessageDrivenMailSender.onMessage()");
        TextMessage tm = (TextMessage)message;
        try {
            textMessage = tm.getText();
        } catch (JMSException e) {
            logger.error("Exception in MessageDrivenMailSender.onMessage() : "+getStackTrace(e));
        }
        logger.info("Successfully received message : "+textMessage);
    }
}
