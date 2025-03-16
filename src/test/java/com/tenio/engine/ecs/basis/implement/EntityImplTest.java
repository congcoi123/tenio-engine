package com.tenio.engine.ecs.basis.implement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.common.pool.ElementPool;
import com.tenio.engine.ecs.basis.Component;
import com.tenio.engine.exception.ComponentIsNotExistedException;
import com.tenio.engine.exception.DuplicatedComponentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EntityImplTest {

    private EntityImpl entity;
    private static final String ENTITY_ID = "test-entity";
    private static final String CONTEXT_NAME = "test-context";
    private static final int NUM_COMPONENTS = 3;
    private Component mockComponent1;
    private Component mockComponent2;
    private Component mockComponent3;
    private ContextInfo contextInfo;

    @Mock
    @SuppressWarnings("unchecked")
    private ElementPool<Component> mockComponentPool;

    @BeforeEach
    void setUp() {
        String[] componentNames = new String[]{"comp1", "comp2", "comp3"};
        Class<?>[] componentTypes = new Class<?>[]{Component.class, Component.class, Component.class};
        contextInfo = new ContextInfo(CONTEXT_NAME, componentNames, componentTypes, NUM_COMPONENTS);
        
        entity = new EntityImpl();
        entity.setId(ENTITY_ID);
        entity.setContextInfo(contextInfo);
        
        @SuppressWarnings("unchecked")
        ElementPool<Component>[] componentPools = new ElementPool[NUM_COMPONENTS];
        componentPools[0] = mockComponentPool;
        entity.setComponentPools(componentPools);
        
        mockComponent1 = mock(Component.class);
        mockComponent2 = mock(Component.class);
        mockComponent3 = mock(Component.class);
    }

    @Test
    void whenCreated_shouldHaveCorrectId() {
        assertEquals(ENTITY_ID, entity.getId());
    }

    @Test
    void whenSettingComponent_shouldStoreComponent() throws DuplicatedComponentException {
        entity.setComponent(0, mockComponent1);
        assertTrue(entity.hasComponent(0));
        assertEquals(mockComponent1, entity.getComponent(0));
    }

    @Test
    void whenSettingDuplicateComponent_shouldThrowException() throws DuplicatedComponentException {
        entity.setComponent(0, mockComponent1);
        assertThrows(DuplicatedComponentException.class, () -> entity.setComponent(0, mockComponent2));
    }

    @Test
    void whenRemovingComponent_shouldRemoveFromEntity() throws DuplicatedComponentException, ComponentIsNotExistedException {
        entity.setComponent(0, mockComponent1);
        entity.removeComponent(0);
        assertFalse(entity.hasComponent(0));
    }

    @Test
    void whenRemovingNonExistentComponent_shouldThrowException() {
        assertThrows(ComponentIsNotExistedException.class, () -> entity.removeComponent(0));
    }

    @Test
    void whenReplacingComponent_shouldUpdateComponent() throws DuplicatedComponentException {
        entity.setComponent(0, mockComponent1);
        entity.replaceComponent(0, mockComponent2);
        assertEquals(mockComponent2, entity.getComponent(0));
    }

    @Test
    void whenCheckingComponents_shouldReturnCorrectState() {
        // Given
        entity.setComponent(0, mockComponent1);
        entity.setComponent(1, mockComponent2);

        // Then
        assertTrue(entity.hasComponent(0));
        assertTrue(entity.hasComponent(1));
        assertFalse(entity.hasComponent(2));
    }

    @Test
    void whenCheckingMultipleComponents_shouldReturnCorrectState() {
        // Given
        entity.setComponent(0, mockComponent1);
        entity.setComponent(1, mockComponent2);

        // Then
        assertTrue(entity.hasComponents(0, 1));
        assertFalse(entity.hasComponents(0, 1, 2));
    }

    @Test
    void whenCheckingAnyComponent_shouldReturnCorrectState() {
        // Given
        entity.setComponent(1, mockComponent1);

        // Then
        assertTrue(entity.hasAnyComponent(0, 1, 2));
        assertFalse(entity.hasAnyComponent(0, 2));
    }

    @Test
    void whenRemovingAllComponents_shouldClearEntity() {
        // Given
        entity.setComponent(0, mockComponent1);
        entity.setComponent(1, mockComponent2);

        // When
        entity.removeAllComponents();

        // Then
        assertFalse(entity.hasComponent(0));
        assertFalse(entity.hasComponent(1));
    }

    @Test
    void whenResetting_shouldClearAllComponents() throws DuplicatedComponentException {
        entity.setComponent(0, mockComponent1);
        entity.setComponent(1, mockComponent2);
        entity.reset();
        assertFalse(entity.hasComponent(0));
        assertFalse(entity.hasComponent(1));
    }

    @Test
    void whenComparingEntities_shouldUseIdentity() {
        // Given
        EntityImpl entity2 = new EntityImpl();
        entity2.setId(ENTITY_ID);

        // Then
        assertNotEquals(entity, entity2);
        assertEquals(entity, entity);
    }

    @Test
    void whenGettingHashCode_shouldUseId() {
        assertEquals(ENTITY_ID.hashCode(), entity.hashCode());
    }
} 