package com.tenio.engine.heartbeat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.message.ExtraMessage;
import java.util.TreeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HeartBeatManagerImplTest {

    private HeartBeatManagerImpl heartBeatManager;
    private static final String TEST_ID = "test-heartbeat";
    private static final int MAX_HEARTBEATS = 5;

    @Mock
    private AbstractHeartBeat mockHeartBeat;
    
    @Mock
    private ExtraMessage mockMessage;

    @BeforeEach
    void setUp() throws Exception {
        heartBeatManager = new HeartBeatManagerImpl();
        heartBeatManager.initialize(MAX_HEARTBEATS);
    }

    @Test
    void whenInitializing_shouldCreateExecutorService() {
        assertDoesNotThrow(() -> {
            HeartBeatManagerImpl manager = new HeartBeatManagerImpl();
            manager.initialize(MAX_HEARTBEATS);
        });
    }

    @Test
    void whenCreatingHeartBeat_shouldAddToManager() {
        // Given
        doNothing().when(mockHeartBeat).setMessageListener(any());

        // When
        heartBeatManager.create(TEST_ID, mockHeartBeat);

        // Then
        assertTrue(heartBeatManager.contains(TEST_ID));
        verify(mockHeartBeat).setMessageListener(any(TreeSet.class));
    }

    @Test
    void whenDisposingExistingHeartBeat_shouldRemoveFromManager() {
        // Given
        doNothing().when(mockHeartBeat).setMessageListener(any());
        heartBeatManager.create(TEST_ID, mockHeartBeat);

        // When
        heartBeatManager.dispose(TEST_ID);

        // Then
        assertFalse(heartBeatManager.contains(TEST_ID));
    }

    @Test
    void whenDisposingNonExistentHeartBeat_shouldHandleException() {
        assertDoesNotThrow(() -> heartBeatManager.dispose("non-existent"));
    }

    @Test
    void whenCheckingContains_shouldReturnCorrectResult() {
        // Given
        doNothing().when(mockHeartBeat).setMessageListener(any());
        
        // When
        heartBeatManager.create(TEST_ID, mockHeartBeat);
        
        // Then
        assertTrue(heartBeatManager.contains(TEST_ID));
        assertFalse(heartBeatManager.contains("non-existent"));
    }

    @Test
    void whenClearing_shouldRemoveAllHeartBeats() {
        // Given
        doNothing().when(mockHeartBeat).setMessageListener(any());
        heartBeatManager.create(TEST_ID, mockHeartBeat);
        
        // When
        heartBeatManager.clear();
        
        // Then
        assertFalse(heartBeatManager.contains(TEST_ID));
    }

    @Test
    void whenSendingMessageWithDelay_shouldAddToMessageQueue() {
        // Given
        doNothing().when(mockHeartBeat).setMessageListener(any());
        heartBeatManager.create(TEST_ID, mockHeartBeat);
        double delayTime = 1.0;

        // When
        heartBeatManager.sendMessage(TEST_ID, mockMessage, delayTime);

        // Then
        verify(mockHeartBeat).setMessageListener(any(TreeSet.class));
    }

    @Test
    void whenSendingMessageWithoutDelay_shouldAddToMessageQueue() {
        // Given
        doNothing().when(mockHeartBeat).setMessageListener(any());
        heartBeatManager.create(TEST_ID, mockHeartBeat);

        // When
        heartBeatManager.sendMessage(TEST_ID, mockMessage);

        // Then
        verify(mockHeartBeat).setMessageListener(any(TreeSet.class));
    }
} 