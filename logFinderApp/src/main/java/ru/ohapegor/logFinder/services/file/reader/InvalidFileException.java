package ru.ohapegor.logFinder.services.file.reader;


public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {
        super(message);
    }
}
