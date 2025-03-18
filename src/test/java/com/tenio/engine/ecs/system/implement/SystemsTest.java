package com.tenio.engine.ecs.system.implement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.ecs.system.ExecuteSystem;
import com.tenio.engine.ecs.system.InitializeSystem;
import com.tenio.engine.ecs.system.RenderSystem;
import com.tenio.engine.ecs.system.TearDownSystem;
import com.tenio.engine.physic2d.graphic.Paint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SystemsTest {

    private Systems systems;

    @Mock
    private InitializeSystem mockInitSystem;
    @Mock
    private ExecuteSystem mockExecSystem;
    @Mock
    private RenderSystem mockRenderSystem;
    @Mock
    private TearDownSystem mockTearDownSystem;
    @Mock
    private Paint mockPaint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        systems = new Systems();
    }

    @Test
    void whenCreated_shouldBeRunning() {
        assertTrue(systems.isRunning());
    }

    @Test
    void whenAddingSystem_shouldAddToAppropriateList() {
        systems.add(mockInitSystem)
               .add(mockExecSystem)
               .add(mockRenderSystem)
               .add(mockTearDownSystem);

        systems.initialize();
        verify(mockInitSystem).initialize();

        systems.execute(1.0f);
        verify(mockExecSystem).execute(1.0f);

        systems.render(mockPaint);
        verify(mockRenderSystem).render(mockPaint);

        systems.tearDown();
        verify(mockTearDownSystem).tearDown();
    }

    @Test
    void whenPaused_shouldNotExecuteOrRender() {
        systems.add(mockExecSystem).add(mockRenderSystem);
        systems.paused(true);

        systems.execute(1.0f);
        systems.render(mockPaint);

        verify(mockExecSystem, never()).execute(anyFloat());
        verify(mockRenderSystem, never()).render(any(Paint.class));
    }

    @Test
    void whenResumed_shouldExecuteAndRender() {
        systems.add(mockExecSystem).add(mockRenderSystem);
        systems.paused(true);
        systems.paused(false);

        systems.execute(1.0f);
        systems.render(mockPaint);

        verify(mockExecSystem).execute(1.0f);
        verify(mockRenderSystem).render(mockPaint);
    }

    @Test
    void whenCleared_shouldRemoveAllSystems() {
        systems.add(mockInitSystem)
               .add(mockExecSystem)
               .add(mockRenderSystem)
               .add(mockTearDownSystem);

        systems.clearSystems();

        systems.initialize();
        systems.execute(1.0f);
        systems.render(mockPaint);
        systems.tearDown();

        verify(mockInitSystem, never()).initialize();
        verify(mockExecSystem, never()).execute(anyFloat());
        verify(mockRenderSystem, never()).render(any(Paint.class));
        verify(mockTearDownSystem, never()).tearDown();
    }

    @Test
    void whenAddingNull_shouldNotThrowException() {
        assertDoesNotThrow(() -> systems.add(null));
    }

    @Test
    void whenAddingMultipleSystemTypes_shouldHandleAllTypes() {
        TestMultiSystem multiSystem = mock(TestMultiSystem.class);
        systems.add(multiSystem);

        systems.initialize();
        systems.execute(1.0f);
        systems.render(mockPaint);
        systems.tearDown();

        verify(multiSystem).initialize();
        verify(multiSystem).execute(1.0f);
        verify(multiSystem).render(mockPaint);
        verify(multiSystem).tearDown();
    }

    private interface TestMultiSystem extends InitializeSystem, ExecuteSystem, RenderSystem, TearDownSystem {
    }
} 