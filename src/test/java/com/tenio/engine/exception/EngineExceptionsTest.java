package com.tenio.engine.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EngineExceptionsTest {

    @Test
    void whenCreatingHeartbeatNotFoundException_shouldHaveCorrectMessage() {
        var exception = new HeartbeatNotFoundException();
        assertEquals("Heartbeat is not found", exception.getMessage());
    }

} 