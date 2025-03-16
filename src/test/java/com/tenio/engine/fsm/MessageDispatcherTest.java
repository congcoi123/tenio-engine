package com.tenio.engine.fsm;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.fsm.entity.AbstractEntity;
import com.tenio.engine.fsm.entity.Telegram;
import com.tenio.engine.message.ExtraMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageDispatcherTest {

    private MessageDispatcher dispatcher;
    
    @Mock
    private EntityManager mockEntityManager;
    
    @Mock
    private AbstractEntity mockReceiver;
    
    @Mock
    private ExtraMessage mockInfo;
    
    @Mock
    private MessageListener mockListener;

    private static final String SENDER_ID = "sender";
    private static final String RECEIVER_ID = "receiver";
    private static final int MSG_TYPE = 1;

    @BeforeEach
    void setUp() {
        dispatcher = new MessageDispatcher(mockEntityManager);
    }

    @Test
    void whenDispatchingImmediateMessage_shouldDeliverImmediately() {
        // Given
        when(mockEntityManager.get(RECEIVER_ID)).thenReturn(mockReceiver);
        when(mockReceiver.handleMessage(any(Telegram.class))).thenReturn(true);
        dispatcher.listen(mockListener);

        // When
        dispatcher.dispatchMessage(0, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // Then
        verify(mockReceiver).handleMessage(any(Telegram.class));
        verify(mockListener).onListen(any(Telegram.class), eq(true));
    }

    @Test
    void whenDispatchingDelayedMessage_shouldQueueMessage() {
        // Given
        when(mockEntityManager.get(RECEIVER_ID)).thenReturn(mockReceiver);
        double delay = 1.0;

        // When
        dispatcher.dispatchMessage(delay, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // Then
        verify(mockReceiver, never()).handleMessage(any(Telegram.class));
    }

    @Test
    void whenReceiverNotFound_shouldNotDispatchMessage() {
        // Given
        when(mockEntityManager.get(RECEIVER_ID)).thenReturn(null);

        // When
        dispatcher.dispatchMessage(0, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // Then
        verify(mockReceiver, never()).handleMessage(any(Telegram.class));
    }

    @Test
    void whenMessageNotHandled_shouldNotifyListeners() {
        // Given
        when(mockEntityManager.get(RECEIVER_ID)).thenReturn(mockReceiver);
        when(mockReceiver.handleMessage(any(Telegram.class))).thenReturn(false);
        dispatcher.listen(mockListener);

        // When
        dispatcher.dispatchMessage(0, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // Then
        verify(mockListener).onListen(any(Telegram.class), eq(false));
    }

    @Test
    void whenUpdating_shouldProcessQueuedMessages() {
        // Given
        when(mockEntityManager.get(RECEIVER_ID)).thenReturn(mockReceiver);
        when(mockReceiver.handleMessage(any(Telegram.class))).thenReturn(true);
        dispatcher.listen(mockListener);
        
        // Queue a message with a small delay
        dispatcher.dispatchMessage(0.1, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // When
        dispatcher.update(0.2f);

        // Then
        verify(mockReceiver).handleMessage(any(Telegram.class));
        verify(mockListener).onListen(any(Telegram.class), eq(true));
    }

    @Test
    void whenClearing_shouldRemoveAllMessages() {
        // Given
        dispatcher.listen(mockListener);
        dispatcher.dispatchMessage(1.0, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // When
        dispatcher.clear();

        // Then
        dispatcher.update(2.0f);
        verify(mockReceiver, never()).handleMessage(any(Telegram.class));
        verify(mockListener, never()).onListen(any(Telegram.class), anyBoolean());
    }
}

