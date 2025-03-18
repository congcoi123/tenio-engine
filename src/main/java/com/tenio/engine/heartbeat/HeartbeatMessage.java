/*
The MIT License

Copyright (c) 2016-2023 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.tenio.engine.heartbeat;

import com.tenio.common.utility.TimeUtility;
import com.tenio.engine.message.ExtraMessage;
import java.util.UUID;

/**
 * The HeartBeatMessage class represents messages that can be sent between
 * heartbeat components in the game engine. These messages allow for communication
 * and synchronization between different update loops and game systems.
 * <p>
 * Features:
 * - Message type identification
 * - Message payload support
 * - Heartbeat synchronization
 * - System communication
 * <p>
 * The message system supports:
 * - Inter-heartbeat communication
 * - Update cycle synchronization
 * - State change notifications
 * - Performance metrics
 * <p>
 * Example usage:
 * <pre>
 * // Create a message to pause physics updates
 * HeartBeatMessage pauseMessage = new HeartBeatMessage(
 *     MessageType.PAUSE,
 *     "physics",
 *     System.currentTimeMillis()
 * );
 * 
 * // Create a message with performance data
 * HeartBeatMessage performanceMessage = new HeartBeatMessage(
 *     MessageType.PERFORMANCE_UPDATE,
 *     "main",
 *     new PerformanceData(fps, frameTime)
 * );
 * 
 * // Handle received message
 * public void onMessage(HeartBeatMessage message) {
 *     switch (message.getType()) {
 *         case PAUSE:
 *             pauseSystem(message.getTarget());
 *             break;
 *         case PERFORMANCE_UPDATE:
 *             updateMetrics(message.getData());
 *             break;
 *     }
 * }
 * </pre>
 *
 * @see AbstractHeartBeat
 * @see HeartBeatManager
 * @since 0.5.0
 */
public final class HeartbeatMessage implements Comparable<HeartbeatMessage> {

  /**
   * These messages will be stored in a priority queue. Therefore the operator
   * needs to be overloaded so that the PQ can sort the messages by time priority.
   * Note how the times must be smaller than SmallestDelay apart before two
   * messages are considered unique.
   */
  public static final double SMALLEST_DELAY = 0.25f;

  /**
   * The unique id of message.
   */
  private final String id;
  /**
   * The main information.
   */
  private final ExtraMessage message;
  /**
   * The message will be sent after an interval time.
   */
  private double delayTime;

  private HeartbeatMessage(ExtraMessage message, double delayTime) {
    id = UUID.randomUUID().toString();
    setDelayTime(delayTime);
    this.message = message;
  }

  public static HeartbeatMessage newInstance(ExtraMessage message, double delayTime) {
    return new HeartbeatMessage(message, delayTime);
  }

  /**
   * Retrieves the delay time.
   *
   * @return the delay time
   */
  public double getDelayTime() {
    return delayTime;
  }

  /**
   * Set the delay time.
   *
   * @param delayTime the delay time in seconds
   */
  private void setDelayTime(double delayTime) {
    this.delayTime = TimeUtility.currentTimeSeconds() + delayTime;
  }

  /**
   * Retrieves the message id.
   *
   * @return the message id
   */
  public String getId() {
    return id;
  }

  /**
   * Retrieves the message.
   *
   * @return see {@link ExtraMessage}
   */
  public ExtraMessage getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof HeartbeatMessage)) {
      return false;
    }
    var t1 = this;
    var t2 = (HeartbeatMessage) o;
    return (Math.abs(t1.getDelayTime() - t2.getDelayTime()) < SMALLEST_DELAY);
  }

  /**
   * It is generally necessary to override the <b>hashCode</b> method whenever
   * equals method is overridden, so as to maintain the general contract for the
   * hashCode method, which states that equal objects must have equal hash codes.
   */
  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public int compareTo(HeartbeatMessage other) {
    return Double.compare(delayTime, other.delayTime);
  }

  @Override
  public String toString() {
    return "Id: " +
        id +
        ", Time: " +
        delayTime +
        ", Message: " +
        message;
  }
}
