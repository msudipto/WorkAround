package coms309.exception;

public class AlreadyClockedInException extends RuntimeException {
    public AlreadyClockedInException(String message) {
        super(message);
    }
}
