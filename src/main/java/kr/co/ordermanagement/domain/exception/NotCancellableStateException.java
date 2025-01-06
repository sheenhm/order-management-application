package kr.co.ordermanagement.domain.exception;

public class NotCancellableStateException extends RuntimeException {
    public NotCancellableStateException(String message) {
        super(message);
    }
}
