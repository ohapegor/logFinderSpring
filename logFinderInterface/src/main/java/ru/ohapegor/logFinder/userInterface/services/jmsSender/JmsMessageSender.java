package ru.ohapegor.logFinder.userInterface.services.jmsSender;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.jms.*;

@Stateless
public class JmsMessageSender {

    private static final Logger logger = LogManager.getLogger();

    @Inject
    @JMSConnectionFactory("jms/TestConnectionFactory")
    private JMSContext context;

    @Resource(name = "jms/TestJMSQueue")
    private Destination destination;

    public void sendJmsMessage(String message, String address) {
        logger.info("Entering JmsMessageSender.sendJmsMessage()");
        context.createProducer().send(destination, context.createObjectMessage(JmsMessage.of(message, address)));
        logger.info("Exiting JmsMessageSender.sendJmsMessage(), message = " + message + "; address = " + address);
    }
}
