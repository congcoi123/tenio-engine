package com.tenio.engine.heartbeat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.message.ExtraMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HeartbeatMessageTest {

    @Mock
    private ExtraMessage mockMessage;

    private HeartbeatMessage heartbeatMessage;
    private final double delayTime = 1.0;

    @BeforeEach
    void setUp() {
        heartbeatMessage = HeartbeatMessage.newInstance(mockMessage, delayTime);
    }

    @Test
    void whenCreatingNewInstance_shouldGenerateUniqueId() {
        HeartbeatMessage another = HeartbeatMessage.newInstance(mockMessage, delayTime);
        assertNotNull(heartbeatMessage.getId());
        assertNotNull(another.getId());
        assertNotEquals(heartbeatMessage.getId(), another.getId());
    }

    @Test
    void whenGettingMessage_shouldReturnOriginalMessage() {
        assertEquals(mockMessage, heartbeatMessage.getMessage());
    }

    @Test
    void whenComparingEqualDelayTimes_shouldBeEqual() {
        HeartbeatMessage message1 = HeartbeatMessage.newInstance(mockMessage, delayTime);
        HeartbeatMessage message2 = HeartbeatMessage.newInstance(mockMessage, delayTime);
        
        assertTrue(Math.abs(message1.getDelayTime() - message2.getDelayTime()) < HeartbeatMessage.SMALLEST_DELAY);
        assertEquals(message1, message2);
    }

    @Test
    void whenComparingDifferentDelayTimes_shouldNotBeEqual() {
        HeartbeatMessage message1 = HeartbeatMessage.newInstance(mockMessage, delayTime);
        HeartbeatMessage message2 = HeartbeatMessage.newInstance(mockMessage, delayTime + 1.0);
        
        assertNotEquals(message1, message2);
    }

    @Test
    void whenComparingWithNull_shouldReturnFalse() {
        assertFalse(heartbeatMessage.equals(null));
    }

    @Test
    void whenComparingWithDifferentType_shouldReturnFalse() {
        assertFalse(heartbeatMessage.equals("not a heartbeat message"));
    }

    @Test
    void whenComparingMessages_shouldOrderByDelayTime() {
        HeartbeatMessage earlier = HeartbeatMessage.newInstance(mockMessage, 1.0);
        HeartbeatMessage later = HeartbeatMessage.newInstance(mockMessage, 2.0);
        
        assertTrue(earlier.compareTo(later) == 1);
        assertTrue(later.compareTo(earlier) == -1);
    }

    @Test
    void whenComparingSameMessage_shouldReturnZero() {
        assertEquals(0, heartbeatMessage.compareTo(heartbeatMessage));
    }

    @Test
    void whenGettingString_shouldIncludeAllFields() {
        String result = heartbeatMessage.toString();
        assertTrue(result.contains(heartbeatMessage.getId()));
        assertTrue(result.contains(String.valueOf(heartbeatMessage.getDelayTime())));
        assertTrue(result.contains(mockMessage.toString()));
    }

    @Test
    void whenGeneratingHashCode_shouldUseId() {
        assertEquals(heartbeatMessage.getId().hashCode(), heartbeatMessage.hashCode());
    }
} 