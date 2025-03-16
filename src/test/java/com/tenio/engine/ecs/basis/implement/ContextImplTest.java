package com.tenio.engine.ecs.basis.implement;

import static org.junit.jupiter.api.Assertions.*;

import com.tenio.engine.ecs.basis.Component;
import com.tenio.engine.ecs.basis.TestComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContextImplTest {

    private ContextImpl<EntityImpl> context;
    private static final String CONTEXT_NAME = "test-context";
    private static final int NUM_COMPONENTS = 3;
    private static final String TEST_COMPONENT_NAME = "test-component";
    private static final int TEST_COMPONENT_VALUE = 42;
    private ContextInfo contextInfo;

    @BeforeEach
    void setUp() {
        String[] componentNames = new String[]{"comp1", "comp2", "comp3"};
        Class<?>[] componentTypes = new Class<?>[]{Component.class, Component.class, Component.class};
        contextInfo = new ContextInfo(CONTEXT_NAME, componentNames, componentTypes, NUM_COMPONENTS);
        context = new ContextImpl<>(contextInfo, EntityImpl.class);
    }

    @Test
    void whenCreated_shouldHaveCorrectContextInfo() {
        assertEquals(contextInfo, context.getContextInfo());
        assertEquals(0, context.getEntitiesCount());
    }

    @Test
    void whenCreatingEntity_shouldAddToContext() {
        EntityImpl entity = context.createEntity();
        assertNotNull(entity);
        assertTrue(context.hasEntity(entity));
        assertEquals(1, context.getEntitiesCount());
        assertEquals(entity, context.getEntity(entity.getId()));
    }

    @Test
    void whenDestroyingEntity_shouldRemoveFromContext() {
        EntityImpl entity = context.createEntity();
        String entityId = entity.getId();
        
        context.destroyEntity(entity);
        
        assertFalse(context.hasEntity(entity));
        assertNull(context.getEntity(entityId));
        assertEquals(0, context.getEntitiesCount());
    }

    @Test
    void whenDestroyingAllEntities_shouldClearContext() {
        context.createEntity();
        context.createEntity();
        context.createEntity();
        
        assertEquals(3, context.getEntitiesCount());
        context.destroyAllEntities();
        assertEquals(0, context.getEntitiesCount());
        assertTrue(context.getEntities().isEmpty());
    }

    @Test
    void whenResetting_shouldClearContextAndPools() {
        EntityImpl entity = context.createEntity();
        entity.setComponent(0, new TestComponent(TEST_COMPONENT_NAME, TEST_COMPONENT_VALUE));
        
        context.reset();
        
        assertEquals(0, context.getEntitiesCount());
        assertTrue(context.getEntities().isEmpty());
    }

    @Test
    void whenGettingEntities_shouldReturnAllEntities() {
        EntityImpl entity1 = context.createEntity();
        EntityImpl entity2 = context.createEntity();
        
        var entities = context.getEntities();
        
        assertEquals(2, entities.size());
        assertTrue(entities.containsKey(entity1.getId()));
        assertTrue(entities.containsKey(entity2.getId()));
    }
} 