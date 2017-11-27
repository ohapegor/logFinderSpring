package ru.ohapegor.logFinder.userInterface.services.jmsSender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.*;

@Service
public class JmsMessageSenderSpring implements JmsMessageSender {

    private static final Logger logger = LogManager.getLogger();

    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage() throws JMSException {
        logger.info("Entering MessageSender.sendMessage(), jmsTemplate = "+jmsTemplate);
        /*jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });*/
        //jmsTemplate.convertAndSend(message);
        logger.info("Exiting MessageSender.sendMessage()");
    }

    @Override
    public void sendJmsMessage(JmsMessage customJmsMessage) {
        logger.info("Entering MessageSender.sendMessage(), jmsTemplate = "+jmsTemplate);
        jmsTemplate.convertAndSend(customJmsMessage);
        logger.info("Exiting MessageSender.sendMessage()");
    }
}
