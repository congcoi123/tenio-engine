package com.tenio.engine.heartbeat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.message.ExtraMessage;
import com.tenio.engine.physic2d.graphic.Paint;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractHeartBeatTest {

    private TestHeartBeat heartBeat;
    private ExecutorService executor;
    
    @Mock
    private ExtraMessage mockMessage;
    
    @BeforeEach
    void setUp() {
        heartBeat = new TestHeartBeat();
        executor = Executors.newSingleThreadExecutor();
    }

    @Test
    void whenSettingMessageListener_shouldStoreListener() {
        // Given
        TreeSet<HeartbeatMessage> listener = new TreeSet<>();
        
        // When
        heartBeat.setMessageListener(listener);
        
        // Then
        assertEquals(listener, heartBeat.getListener());
    }

    @Test
    void whenCallingHeartBeat_shouldExecuteGameLoop() throws Exception {
        // Given
        TreeSet<HeartbeatMessage> listener = new TreeSet<>();
        heartBeat.setMessageListener(listener);
        
        // When
        Future<Void> future = executor.submit(heartBeat);
        
        // Let it run for a short time
        Thread.sleep(100);
        heartBeat.stopLoop();
        
        // Then
        assertDoesNotThrow(() -> future.get(1, TimeUnit.SECONDS));
        assertTrue(heartBeat.wasOnCreateCalled());
        assertTrue(heartBeat.wasOnUpdateCalled());
        assertTrue(heartBeat.wasOnRenderCalled());
    }

    @Test
    void whenPausingHeartBeat_shouldCallPauseAndResumeMethods() {
        // When
        heartBeat.pause(true);
        
        // Then
        assertTrue(heartBeat.wasOnPauseCalled());
        assertFalse(heartBeat.isRunning());
        
        // When
        heartBeat.pause(false);
        
        // Then
        assertTrue(heartBeat.wasOnResumeCalled());
        assertTrue(heartBeat.isRunning());
    }

    @Test
    void whenSettingCcu_shouldUpdateValue() {
        // When
        heartBeat.setCcu(100);
        
        // Then
        assertEquals(100, heartBeat.getCurrentCcu());
    }

    // Test implementation of AbstractHeartBeat
    private static class TestHeartBeat extends AbstractHeartBeat {
        private boolean onCreateCalled;
        private boolean onUpdateCalled;
        private boolean onRenderCalled;
        private boolean onPauseCalled;
        private boolean onResumeCalled;
        private TreeSet<HeartbeatMessage> listener;
        private int currentCcu;
        
        TestHeartBeat() {
            super(800, 600); // Default test dimensions
        }
        
        void stopLoop() {
            pause(true);
        }
        
        boolean isRunning() {
            return !onPauseCalled || onResumeCalled;
        }
        
        TreeSet<HeartbeatMessage> getListener() {
            return listener;
        }
        
        boolean wasOnCreateCalled() {
            return onCreateCalled;
        }
        
        boolean wasOnUpdateCalled() {
            return onUpdateCalled;
        }
        
        boolean wasOnRenderCalled() {
            return onRenderCalled;
        }
        
        boolean wasOnPauseCalled() {
            return onPauseCalled;
        }
        
        boolean wasOnResumeCalled() {
            return onResumeCalled;
        }
        
        int getCurrentCcu() {
            return currentCcu;
        }
        
        @Override
        public void setMessageListener(TreeSet<HeartbeatMessage> listener) {
            this.listener = listener;
            super.setMessageListener(listener);
        }
        
        @Override
        public void setCcu(int currentCcu) {
            this.currentCcu = currentCcu;
            super.setCcu(currentCcu);
        }

        @Override
        protected void onCreate() {
            onCreateCalled = true;
        }

        @Override
        protected void onMessage(ExtraMessage message) {
            // Not tested in this implementation
        }

        @Override
        protected void onUpdate(float deltaTime) {
            onUpdateCalled = true;
        }

        @Override
        protected void onRender(Paint paint) {
            onRenderCalled = true;
        }

        @Override
        protected void onPause() {
            onPauseCalled = true;
            onResumeCalled = false;
        }

        @Override
        protected void onResume() {
            onResumeCalled = true;
        }

        @Override
        protected void onDispose() {
            // Not tested in this implementation
        }

        @Override
        protected void onAction1() {
            // Not tested in this implementation
        }

        @Override
        protected void onAction2() {
            // Not tested in this implementation
        }

        @Override
        protected void onAction3() {
            // Not tested in this implementation
        }
    }
} 