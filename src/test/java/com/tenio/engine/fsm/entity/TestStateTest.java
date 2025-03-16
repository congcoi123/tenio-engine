package com.tenio.engine.fsm.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestStateTest {

    private TestState state;
    private TestEntity entity;
    
    @Mock
    private Telegram mockTelegram;

    @BeforeEach
    void setUp() {
        state = new TestState();
        entity = new TestEntity("test-entity");
    }

    @Test
    void whenEntering_shouldCallEnterMethod() {
        // When
        state.enter(entity);

        // Then
        assertTrue(state.wasEnterCalled());
        assertEquals(entity, state.getLastEntity());
    }

    @Test
    void whenExecuting_shouldCallExecuteMethod() {
        // When
        state.execute(entity);

        // Then
        assertTrue(state.wasExecuteCalled());
        assertEquals(entity, state.getLastEntity());
    }

    @Test
    void whenExiting_shouldCallExitMethod() {
        // When
        state.exit(entity);

        // Then
        assertTrue(state.wasExitCalled());
        assertEquals(entity, state.getLastEntity());
    }

    @Test
    void whenHandlingMessage_shouldCallOnMessageMethod() {
        // When
        boolean result = state.onMessage(entity, mockTelegram);

        // Then
        assertTrue(result);
        assertTrue(state.wasOnMessageCalled());
        assertEquals(entity, state.getLastEntity());
        assertEquals(mockTelegram, state.getLastMessage());
    }

    @Test
    void whenResetting_shouldClearAllFlags() {
        // Given
        state.enter(entity);
        state.execute(entity);
        state.exit(entity);
        state.onMessage(entity, mockTelegram);

        // When
        state.reset();

        // Then
        assertFalse(state.wasEnterCalled());
        assertFalse(state.wasExecuteCalled());
        assertFalse(state.wasExitCalled());
        assertFalse(state.wasOnMessageCalled());
        assertNull(state.getLastEntity());
        assertNull(state.getLastMessage());
    }
} 