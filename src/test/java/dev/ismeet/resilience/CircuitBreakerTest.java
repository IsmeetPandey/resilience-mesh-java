package dev.ismeet.resilience;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

class CircuitBreakerTest {

    @Test
    void testSuccessfulExecution() throws Exception {
        CircuitBreaker cb = new CircuitBreaker("payment-service", 2, Duration.ofSeconds(1));
        String result = cb.execute(() -> "OK");
        assertEquals("OK", result);
        assertEquals(State.CLOSED, cb.getState());
    }

    @Test
    void testTripToOpenOnThreshold() {
        CircuitBreaker cb = new CircuitBreaker("inventory-service", 2, Duration.ofSeconds(1));

        assertThrows(RuntimeException.class, () -> cb.execute(() -> {
            throw new RuntimeException("DB Connection Down");
        }));
        assertEquals(State.CLOSED, cb.getState());

        assertThrows(RuntimeException.class, () -> cb.execute(() -> {
            throw new RuntimeException("DB Connection Down");
        }));
        assertEquals(State.OPEN, cb.getState());

        // Subsequent call is blocked immediately
        assertThrows(CircuitBreakerException.class, () -> cb.execute(() -> "Never Runs"));
    }
}
