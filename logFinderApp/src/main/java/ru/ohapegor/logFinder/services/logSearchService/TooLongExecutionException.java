package ru.ohapegor.logFinder.services.logSearchService;

public class TooLongExecutionException extends RuntimeException {

    public TooLongExecutionException(String message) {
        super(message);
    }
}
