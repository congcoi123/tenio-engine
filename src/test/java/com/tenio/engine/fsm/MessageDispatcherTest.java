package com.tenio.engine.fsm;

import static org.mockito.Mockito.*;

import com.tenio.engine.fsm.entity.AbstractEntity;
import com.tenio.engine.fsm.entity.Telegram;
import com.tenio.engine.message.ExtraMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageDispatcherTest {

    private MessageDispatcher dispatcher;
    private MessageListener messageListener;
    
    @Mock
    private EntityManager mockEntityManager;
    
    @Mock
    private AbstractEntity mockReceiver;
    
    @Mock
    private ExtraMessage mockInfo;
    
    @Mock
    private AbstractEntity mockSender;

    private static final String SENDER_ID = "sender";
    private static final String RECEIVER_ID = "receiver";
    private static final int MSG_TYPE = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dispatcher = new MessageDispatcher(mockEntityManager);
        messageListener = mock(MessageListener.class);
        dispatcher.listen(messageListener);
    }

    @Test
    void whenDispatchingImmediateMessage_shouldDeliverImmediately() {
        // Given
        when(mockEntityManager.get(RECEIVER_ID)).thenReturn(mockReceiver);
        when(mockReceiver.handleMessage(any(Telegram.class))).thenReturn(true);

        // When
        dispatcher.dispatchMessage(0, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // Then
        verify(mockReceiver).handleMessage(any(Telegram.class));
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

        // When
        dispatcher.dispatchMessage(0, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // Then
        verify(mockReceiver).handleMessage(any(Telegram.class));
    }

    @Test
    void whenMessageDispatched_shouldBeDelivered() {
        // Given
        when(mockEntityManager.get(RECEIVER_ID)).thenReturn(mockReceiver);
        when(mockReceiver.handleMessage(any(Telegram.class))).thenReturn(true);

        // When
        dispatcher.dispatchMessage(0, SENDER_ID, RECEIVER_ID, MSG_TYPE, mockInfo);

        // Then
        verify(mockReceiver).handleMessage(any(Telegram.class));
    }
}

