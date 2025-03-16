package com.tenio.engine.ecs.pool;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.common.constant.CommonConstant;
import com.tenio.common.exception.NullElementPoolException;
import com.tenio.engine.ecs.basis.Component;
import com.tenio.engine.ecs.basis.Entity;
import com.tenio.engine.ecs.basis.implement.ContextInfo;
import com.tenio.engine.ecs.basis.implement.EntityImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityPoolTest {

    private EntityPool pool;
    private ContextInfo contextInfo;
    private static final String CONTEXT_NAME = "test-context";
    private static final int NUM_COMPONENTS = 3;

    @BeforeEach
    void setUp() {
        String[] componentNames = new String[]{"comp1", "comp2", "comp3"};
        Class<?>[] componentTypes = new Class<?>[]{Component.class, Component.class, Component.class};
        contextInfo = new ContextInfo(CONTEXT_NAME, componentNames, componentTypes, NUM_COMPONENTS);
        pool = new EntityPool(EntityImpl.class, contextInfo);
    }

    @Test
    void whenCreated_shouldHaveDefaultSize() {
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getPoolSize());
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getAvailableSlot());
    }

    @Test
    void whenGettingEntity_shouldDecreaseAvailableSlots() {
        Entity entity = pool.get();
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(contextInfo, entity.getContextInfo());
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL - 1, pool.getAvailableSlot());
    }

    @Test
    void whenRepayingEntity_shouldIncreaseAvailableSlots() {
        Entity entity = pool.get();
        pool.repay(entity);
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getAvailableSlot());
    }

    @Test
    void whenRepayingUnknownEntity_shouldThrowException() {
        EntityImpl unknownEntity = new EntityImpl();
        assertThrows(NullElementPoolException.class, () -> pool.repay(unknownEntity));
    }

    @Test
    void whenPoolExhausted_shouldIncreaseSize() {
        // Get all entities from the initial pool
        for (int i = 0; i < CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL; i++) {
            assertNotNull(pool.get());
        }

        // Get one more entity, which should trigger pool expansion
        Entity entity = pool.get();
        assertNotNull(entity);
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL + CommonConstant.ADDITIONAL_NUMBER_ELEMENTS_POOL,
                pool.getPoolSize());
    }

    @Test
    void whenCleaned_shouldResetPool() {
        Entity entity = pool.get();
        assertNotNull(entity);

        pool.cleanup();
        assertEquals(-1, pool.getPoolSize());
        assertEquals(0, pool.getAvailableSlot());
    }

    @Test
    void whenGettingMultipleEntities_shouldTrackAvailableSlots() {
        int numEntities = 5;
        Entity[] entities = new Entity[numEntities];

        // Get entities
        for (int i = 0; i < numEntities; i++) {
            entities[i] = pool.get();
            assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL - (i + 1), pool.getAvailableSlot());
        }

        // Repay entities
        for (int i = 0; i < numEntities; i++) {
            pool.repay(entities[i]);
            assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL - numEntities + (i + 1),
                    pool.getAvailableSlot());
        }
    }

    @Test
    void whenRepayingEntity_shouldResetEntity() {
        Entity entity = pool.get();
        entity.setId("test-id");
        
        pool.repay(entity);
        
        // The entity should be reset when repaid
        assertNotEquals("test-id", entity.getId());
    }

    @Test
    void whenGettingEntity_shouldHaveUniqueId() {
        Entity entity1 = pool.get();
        Entity entity2 = pool.get();
        
        assertNotNull(entity1.getId());
        assertNotNull(entity2.getId());
        assertNotEquals(entity1.getId(), entity2.getId());
    }
} 