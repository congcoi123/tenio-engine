package com.tenio.engine.fsm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.fsm.entity.AbstractEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EntityManagerTest {

    private EntityManager manager;
    
    @Mock
    private AbstractEntity mockEntity1;
    
    @Mock
    private AbstractEntity mockEntity2;

    private static final String ENTITY_ID_1 = "entity1";
    private static final String ENTITY_ID_2 = "entity2";

    @BeforeEach
    void setUp() {
        manager = new EntityManager();
        when(mockEntity1.getId()).thenReturn(ENTITY_ID_1);
        when(mockEntity2.getId()).thenReturn(ENTITY_ID_2);
    }

    @Test
    void whenRegisteringEntity_shouldAddToManager() {
        // When
        manager.register(mockEntity1);

        // Then
        assertTrue(manager.contain(ENTITY_ID_1));
        assertEquals(1, manager.count());
        assertEquals(mockEntity1, manager.get(ENTITY_ID_1));
    }

    @Test
    void whenRegisteringDuplicateEntity_shouldNotAdd() {
        // Given
        manager.register(mockEntity1);

        // When
        manager.register(mockEntity1);

        // Then
        assertEquals(1, manager.count());
    }

    @Test
    void whenGettingNonExistentEntity_shouldReturnNull() {
        assertNull(manager.get("non-existent"));
    }

    @Test
    void whenUpdating_shouldUpdateAllEntities() {
        // Given
        manager.register(mockEntity1);
        manager.register(mockEntity2);
        float deltaTime = 0.1f;

        // When
        manager.update(deltaTime);

        // Then
        verify(mockEntity1).update(deltaTime);
        verify(mockEntity2).update(deltaTime);
    }

    @Test
    void whenGettingAllEntities_shouldReturnCopyOfMap() {
        // Given
        manager.register(mockEntity1);
        manager.register(mockEntity2);

        // When
        var entities = manager.gets();

        // Then
        assertEquals(2, entities.size());
        assertTrue(entities.containsKey(ENTITY_ID_1));
        assertTrue(entities.containsKey(ENTITY_ID_2));
    }

    @Test
    void whenRemovingEntity_shouldRemoveFromManager() {
        // Given
        manager.register(mockEntity1);
        manager.register(mockEntity2);

        // When
        manager.remove(ENTITY_ID_1);

        // Then
        assertFalse(manager.contain(ENTITY_ID_1));
        assertTrue(manager.contain(ENTITY_ID_2));
        assertEquals(1, manager.count());
    }

    @Test
    void whenClearing_shouldRemoveAllEntities() {
        // Given
        manager.register(mockEntity1);
        manager.register(mockEntity2);

        // When
        manager.clear();

        // Then
        assertEquals(0, manager.count());
        assertFalse(manager.contain(ENTITY_ID_1));
        assertFalse(manager.contain(ENTITY_ID_2));
    }
}

