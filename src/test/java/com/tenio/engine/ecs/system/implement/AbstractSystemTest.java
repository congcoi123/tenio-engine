package com.tenio.engine.ecs.system.implement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.ecs.basis.Context;
import com.tenio.engine.ecs.basis.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AbstractSystemTest {

    @Mock
    private Context<Entity> mockContext;

    private TestSystem testSystem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testSystem = new TestSystem(mockContext);
    }

    @Test
    void whenCreated_shouldHaveCorrectContext() {
        assertEquals(mockContext, testSystem.getContext());
    }

    private static class TestSystem extends AbstractSystem<Entity> {
        public TestSystem(Context<Entity> context) {
            super(context);
        }
    }
} 