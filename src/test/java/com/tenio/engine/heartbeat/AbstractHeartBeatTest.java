package com.tenio.engine.heartbeat;

import static org.junit.jupiter.api.Assertions.*;

import com.tenio.engine.message.ExtraMessage;
import com.tenio.engine.physic2d.graphic.Paint;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractHeartBeatTest {

    private TestHeartBeat heartBeat;
    
    @Mock
    private ExtraMessage mockMessage;
    
    @BeforeEach
    void setUp() {
        heartBeat = new TestHeartBeat();
    }

    @Test
    void whenCreated_shouldInitializeCorrectly() {
        assertNotNull(heartBeat);
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

    // Test implementation of AbstractHeartBeat
    private static class TestHeartBeat extends AbstractHeartBeat {
        private TreeSet<HeartbeatMessage> listener;
        
        TestHeartBeat() {
            super(800, 600);
        }
        
        TreeSet<HeartbeatMessage> getListener() {
            return listener;
        }
        
        @Override
        public void setMessageListener(TreeSet<HeartbeatMessage> listener) {
            this.listener = listener;
            super.setMessageListener(listener);
        }

        @Override
        protected void onCreate() {
        }

        @Override
        protected void onInitialization() {
        }

        @Override
        protected void onMessage(ExtraMessage message) {
        }

        @Override
        protected void onUpdate(float deltaTime) {
        }

        @Override
        protected void onRender(Paint paint) {
        }

        @Override
        protected void onPause() {
        }

        @Override
        protected void onResume() {
        }

        @Override
        protected void onDispose() {
        }

        @Override
        protected void onAction1() {
        }

        @Override
        protected void onAction2() {
        }

        @Override
        protected void onAction3() {
        }
    }
} 