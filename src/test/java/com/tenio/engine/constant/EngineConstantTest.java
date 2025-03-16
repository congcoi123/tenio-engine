package com.tenio.engine.constant;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EngineConstantTest {

    @Test
    void whenAccessingSendMsgImmediately_shouldHaveCorrectValue() {
        assertEquals(0.0, EngineConstant.SEND_MSG_IMMEDIATELY, 0.0001);
    }

    @Test
    void whenCreatingInstance_shouldThrowException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var constructor = EngineConstant.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
} 