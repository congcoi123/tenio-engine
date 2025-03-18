package com.tenio.engine.physic2d.utility;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.physic2d.common.BaseGameEntity;
import com.tenio.engine.physic2d.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EntitiesRelationshipTest {

    @Mock
    private BaseGameEntity baseGameEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
  }

  @Test
    void testIsOverlapped() {
        List<BaseGameEntity> entities = new ArrayList<>();
    when(baseGameEntity.getPosition()).thenReturn(Vector2.newInstance());
        when(baseGameEntity.getBoundingRadius()).thenReturn(1.0f);
        assertFalse(EntitiesRelationship.isOverlapped(baseGameEntity, entities));
  }
}

