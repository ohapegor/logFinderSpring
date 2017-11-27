package ru.ohapegor.logFinder.userInterface.services.jmsSender;


import java.io.Serializable;

public class JmsMessage implements Serializable {

    private String message;

    private String address;

    public JmsMessage(String message, String address) {
        this.message = message;
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public String getAddress() {
        return address;
    }
}
