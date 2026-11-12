package dev.ismeet.resilience;

public class CircuitBreakerException extends RuntimeException {
    public CircuitBreakerException(String message) {
        super(message);
    }
}
