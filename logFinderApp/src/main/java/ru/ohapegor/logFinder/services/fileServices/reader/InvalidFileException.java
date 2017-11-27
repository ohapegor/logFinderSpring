package ru.ohapegor.logFinder.services.fileServices.reader;


public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {
        super(message);
    }
}
