package com.tenio.engine.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EngineExceptionsTest {

    @Test
    void whenCreatingHeartbeatNotFoundException_shouldHaveCorrectMessage() {
        // When
        var exception = new HeartbeatNotFoundException();

        // Then
        assertEquals("Heartbeat is not found", exception.getMessage());
    }

    @Test
    void whenCreatingDuplicatedComponentException_shouldHaveCorrectMessage() {
        // When
        var exception = new DuplicatedComponentException();

        // Then
        assertEquals("Component is duplicated", exception.getMessage());
    }

    @Test
    void whenCreatingDuplicatedEntityException_shouldHaveCorrectMessage() {
        // When
        var exception = new DuplicatedEntityException();

        // Then
        assertEquals("Entity is duplicated", exception.getMessage());
    }

    @Test
    void whenCreatingComponentIsNotExistedException_shouldHaveCorrectMessage() {
        // When
        var exception = new ComponentIsNotExistedException();

        // Then
        assertEquals("Component is not existed", exception.getMessage());
    }

    @Test
    void whenThrowingHeartbeatNotFoundException_shouldBeRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            throw new HeartbeatNotFoundException();
        });
    }

    @Test
    void whenThrowingDuplicatedComponentException_shouldBeRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            throw new DuplicatedComponentException();
        });
    }

    @Test
    void whenThrowingDuplicatedEntityException_shouldBeRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            throw new DuplicatedEntityException();
        });
    }

    @Test
    void whenThrowingComponentIsNotExistedException_shouldBeRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            throw new ComponentIsNotExistedException();
        });
    }
} 