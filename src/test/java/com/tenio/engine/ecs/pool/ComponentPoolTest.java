package com.tenio.engine.ecs.pool;

import static org.junit.jupiter.api.Assertions.*;

import com.tenio.common.constant.CommonConstant;
import com.tenio.common.exception.NullElementPoolException;
import com.tenio.engine.ecs.basis.TestComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentPoolTest {

    private ComponentPool pool;

    @BeforeEach
    void setUp() {
        pool = new ComponentPool(TestComponent.class);
    }

    @Test
    void whenCreated_shouldHaveDefaultSize() {
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getPoolSize());
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getAvailableSlot());
    }

    @Test
    void whenGettingComponent_shouldDecreaseAvailableSlots() {
        TestComponent component = (TestComponent) pool.get();
        assertNotNull(component);
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL - 1, pool.getAvailableSlot());
    }

    @Test
    void whenRepayingComponent_shouldIncreaseAvailableSlots() {
        TestComponent component = (TestComponent) pool.get();
        pool.repay(component);
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getAvailableSlot());
    }

    @Test
    void whenRepayingUnknownComponent_shouldThrowException() {
        TestComponent unknownComponent = new TestComponent("test", 1);
        assertThrows(NullElementPoolException.class, () -> pool.repay(unknownComponent));
    }

    @Test
    void whenPoolExhausted_shouldIncreaseSize() {
        // Get all components from the initial pool
        for (int i = 0; i < CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL; i++) {
            assertNotNull(pool.get());
        }

        // Get one more component, which should trigger pool expansion
        TestComponent component = (TestComponent) pool.get();
        assertNotNull(component);
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL + CommonConstant.ADDITIONAL_NUMBER_ELEMENTS_POOL,
                pool.getPoolSize());
    }

    @Test
    void whenCleaned_shouldResetPool() {
        TestComponent component = (TestComponent) pool.get();
        assertNotNull(component);

        pool.cleanup();
        assertEquals(-1, pool.getPoolSize());
        assertEquals(0, pool.getAvailableSlot());
    }

    @Test
    void whenGettingMultipleComponents_shouldTrackAvailableSlots() {
        int numComponents = 5;
        TestComponent[] components = new TestComponent[numComponents];

        // Get components
        for (int i = 0; i < numComponents; i++) {
            components[i] = (TestComponent) pool.get();
            assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL - (i + 1), pool.getAvailableSlot());
        }

        // Repay components
        for (int i = 0; i < numComponents; i++) {
            pool.repay(components[i]);
            assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL - numComponents + (i + 1),
                    pool.getAvailableSlot());
        }
    }
} 