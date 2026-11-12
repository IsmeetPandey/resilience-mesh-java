package dev.ismeet.resilience;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CircuitBreaker {
    private final String name;
    private final int failureThreshold;
    private final Duration resetTimeout;
    private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicReference<Instant> lastFailureTime = new AtomicReference<>(Instant.EPOCH);

    public CircuitBreaker(String name, int failureThreshold, Duration resetTimeout) {
        this.name = name;
        this.failureThreshold = failureThreshold;
        this.resetTimeout = resetTimeout;
    }

    public <T> T execute(Callable<T> task) throws Exception {
        State current = state.get();

        if (current == State.OPEN) {
            if (Instant.now().isAfter(lastFailureTime.get().plus(resetTimeout))) {
                state.compareAndSet(State.OPEN, State.HALF_OPEN);
            } else {
                throw new CircuitBreakerException("Circuit breaker [" + name + "] is OPEN");
            }
        }

        try {
            T result = task.call();
            onSuccess();
            return result;
        } catch (Exception ex) {
            onFailure();
            throw ex;
        }
    }

    private void onSuccess() {
        failureCount.set(0);
        state.set(State.CLOSED);
    }

    private void onFailure() {
        lastFailureTime.set(Instant.now());
        int failures = failureCount.incrementAndGet();
        if (failures >= failureThreshold) {
            state.set(State.OPEN);
        }
    }

    public State getState() {
        return state.get();
    }

    public String getName() {
        return name;
    }
}
