package nl.winfinnity.housingapp.exceptions;

public class NoCustomersFoundException extends RuntimeException {
    public NoCustomersFoundException(String message) {
        super(message);
    }
}
