package com.tenio.engine.ecs.basis.implement;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContextInfoTest {

    private ContextInfo contextInfo;
    private static final String CONTEXT_NAME = "test-context";
    private static final int NUM_COMPONENTS = 3;
    private String[] componentNames;
    private Class<?>[] componentTypes;

    @BeforeEach
    void setUp() {
        componentNames = new String[]{"comp1", "comp2", "comp3"};
        componentTypes = new Class<?>[]{String.class, Integer.class, Boolean.class};
        contextInfo = new ContextInfo(CONTEXT_NAME, componentNames, componentTypes, NUM_COMPONENTS);
    }

    @Test
    void whenCreated_shouldHaveCorrectValues() {
        assertEquals(CONTEXT_NAME, contextInfo.getName());
        assertEquals(NUM_COMPONENTS, contextInfo.getNumberComponents());
        assertArrayEquals(componentNames, contextInfo.getComponentNames());
        assertArrayEquals(componentTypes, contextInfo.getComponentTypes());
    }

    @Test
    void whenGettingString_shouldIncludeAllFields() {
        String result = contextInfo.toString();
        assertTrue(result.contains(CONTEXT_NAME));
        assertTrue(result.contains(String.valueOf(NUM_COMPONENTS)));
        assertTrue(result.contains(componentNames[0]));
        assertTrue(result.contains(componentNames[1]));
        assertTrue(result.contains(componentNames[2]));
    }
} 