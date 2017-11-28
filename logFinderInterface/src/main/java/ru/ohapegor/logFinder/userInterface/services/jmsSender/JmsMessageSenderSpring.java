package ru.ohapegor.logFinder.userInterface.services.jmsSender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsMessageSenderSpring implements JmsMessageSender {

    private static final Logger logger = LogManager.getLogger();

    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void sendJmsMessage(JmsMessage customJmsMessage) {
        logger.info("Entering MessageSender.sendMessage()");
        jmsTemplate.convertAndSend(customJmsMessage);
        logger.info("Exiting MessageSender.sendMessage()");
    }
}
