package ru.ohapegor.logFinder.userInterface.services.jmsSender;


import java.io.Serializable;
import java.util.Objects;

public class JmsMessage implements Serializable {

    private String message;

    private String address;

    private JmsMessage(String message, String address) {
        this.message = message;
        this.address = address;
    }

    public static JmsMessage  of(String message, String address){
        Objects.requireNonNull(address,"empty address in message");
        return new JmsMessage(message,address);
    }

    public String getMessage() {
        return message;
    }

    public String getAddress() {
        return address;
    }
}
