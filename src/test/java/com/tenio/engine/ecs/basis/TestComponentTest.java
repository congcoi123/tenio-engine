package com.tenio.engine.ecs.basis;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestComponentTest {

    private TestComponent component;
    private static final String TEST_NAME = "test-component";
    private static final int TEST_VALUE = 42;

    @BeforeEach
    void setUp() {
        component = new TestComponent(TEST_NAME, TEST_VALUE);
    }

    @Test
    void whenCreated_shouldHaveCorrectValues() {
        assertEquals(TEST_NAME, component.getName());
        assertEquals(TEST_VALUE, component.getValue());
    }

    @Test
    void whenSettingName_shouldUpdateName() {
        // Given
        String newName = "new-name";

        // When
        component.setName(newName);

        // Then
        assertEquals(newName, component.getName());
    }

    @Test
    void whenSettingValue_shouldUpdateValue() {
        // Given
        int newValue = 100;

        // When
        component.setValue(newValue);

        // Then
        assertEquals(newValue, component.getValue());
    }

    @Test
    void shouldImplementComponentInterface() {
        assertTrue(component instanceof Component);
    }
} 