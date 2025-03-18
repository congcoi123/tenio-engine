package com.tenio.engine.fsm;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityManagerTest {

    private EntityManager manager;

    @BeforeEach
    void setUp() {
        manager = new EntityManager();
    }

    @Test
    void whenCreated_shouldBeEmpty() {
        assertEquals(0, manager.count());
    }
}

