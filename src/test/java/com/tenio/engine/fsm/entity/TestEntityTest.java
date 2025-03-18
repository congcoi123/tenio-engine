package com.tenio.engine.fsm.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestEntityTest {

    private TestEntity entity;
    private static final String TEST_ID = "test-entity";
    
    @Mock
    private Telegram mockTelegram;

    @BeforeEach
    void setUp() {
        entity = new TestEntity(TEST_ID);
    }

    @Test
    void whenCreated_shouldHaveCorrectId() {
        assertEquals(TEST_ID, entity.getId());
    }

    @Test
    void whenHandlingMessage_shouldUpdateState() {
        // When
        boolean result = entity.handleMessage(mockTelegram);

        // Then
        assertTrue(result);
        assertTrue(entity.wasHandleMessageCalled());
        assertEquals(mockTelegram, entity.getLastMessage());
    }

    @Test
    void whenUpdating_shouldUpdateState() {
        // Given
        float deltaTime = 0.1f;

        // When
        entity.update(deltaTime);

        // Then
        assertTrue(entity.wasUpdateCalled());
        assertEquals(deltaTime, entity.getLastDeltaTime(), 0.001f);
    }

    @Test
    void whenResetting_shouldClearAllFlags() {
        // Given
        entity.handleMessage(mockTelegram);
        entity.update(0.1f);

        // When
        entity.reset();

        // Then
        assertFalse(entity.wasHandleMessageCalled());
        assertFalse(entity.wasUpdateCalled());
        assertNull(entity.getLastMessage());
        assertEquals(0f, entity.getLastDeltaTime(), 0.001f);
    }
} 