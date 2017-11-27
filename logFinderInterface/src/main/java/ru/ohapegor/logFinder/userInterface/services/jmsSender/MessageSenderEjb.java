package ru.ohapegor.logFinder.userInterface.services.jmsSender;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.jms.*;

@ManagedBean(eager = true)
@SessionScoped
public class MessageSenderEjb {

    private static final Logger logger = LogManager.getLogger();

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Inject
    @JMSConnectionFactory("jms/TestConnectionFactory")
    private JMSContext context;

    @Resource(name = "jms/TestJMSQueue")
    private Destination destination;

    public void sendMessage() throws JMSException {
        logger.info("Entering MessageSender.sendMessage()");
        TextMessage tm = context.createTextMessage(message);
        //tm.setText(message);
        JMSProducer producer = context.createProducer();
        producer.send(destination,tm);
        logger.info("Exiting MessageSender.sendMessage(), send message = "+tm.getText());
    }
}