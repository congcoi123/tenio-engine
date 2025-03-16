package com.tenio.engine.message;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestExtraMessageTest {

    private TestExtraMessage message;

    @BeforeEach
    void setUp() {
        message = new TestExtraMessage();
    }

    @Test
    void whenCreated_shouldHaveTimestamp() {
        assertTrue(message.getTimestamp() > 0);
        assertTrue(message.getTimestamp() <= System.currentTimeMillis());
    }

    @Test
    void whenPuttingContent_shouldStoreValue() {
        // Given
        String key = "testKey";
        String value = "testValue";

        // When
        message.putContent(key, value);

        // Then
        assertEquals(value, message.getContentByKey(key));
    }

    @Test
    void whenGettingContent_shouldReturnCopy() {
        // Given
        String key = "testKey";
        String value = "testValue";
        message.putContent(key, value);

        // When
        Map<String, Object> content = message.getContent();
        content.put("newKey", "newValue");

        // Then
        assertNull(message.getContentByKey("newKey"));
        assertEquals(value, message.getContentByKey(key));
    }

    @Test
    void whenGettingNonExistentKey_shouldReturnNull() {
        assertNull(message.getContentByKey("nonexistent"));
    }

    @Test
    void whenPuttingMultipleContent_shouldStoreAll() {
        // Given
        String key1 = "key1";
        String value1 = "value1";
        String key2 = "key2";
        Integer value2 = 42;

        // When
        message.putContent(key1, value1);
        message.putContent(key2, value2);

        // Then
        Map<String, Object> content = message.getContent();
        assertEquals(2, content.size());
        assertEquals(value1, content.get(key1));
        assertEquals(value2, content.get(key2));
    }

    @Test
    void whenUpdatingExistingKey_shouldOverwriteValue() {
        // Given
        String key = "testKey";
        String value1 = "value1";
        String value2 = "value2";

        // When
        message.putContent(key, value1);
        message.putContent(key, value2);

        // Then
        assertEquals(value2, message.getContentByKey(key));
        assertEquals(1, message.getContent().size());
    }
} 