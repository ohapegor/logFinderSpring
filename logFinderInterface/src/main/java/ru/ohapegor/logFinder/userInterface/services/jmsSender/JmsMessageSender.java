package ru.ohapegor.logFinder.userInterface.services.jmsSender;


public interface JmsMessageSender {

    void sendJmsMessage(JmsMessage customJmsMessage);
}
