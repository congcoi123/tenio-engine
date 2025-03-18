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

package com.tenio.engine.fsm.entity;

import com.tenio.common.utility.TimeUtility;
import com.tenio.engine.message.ExtraMessage;

/**
 * A message class used for communication between entities in the Finite State Machine (FSM).
 * Telegrams carry information about the sender, receiver, message type, and any additional
 * data needed for the communication.
 *
 * <p>
 * Features:
 * - Message delivery timing control
 * - Entity-to-entity communication
 * - Message type identification
 * - Additional data payload support
 * - Message prioritization
 * </p>
 *
 * <p>
 * The telegram system supports both immediate and delayed message delivery,
 * allowing for scheduled communications between entities. Messages can be
 * prioritized and queued for delivery at specific times.
 * </p>
 *
 * <p>
 * Example usage:
 * {@code
 * Telegram telegram = new Telegram(
 *     System.currentTimeMillis(), // Dispatch time
 *     senderEntity,              // Sender
 *     receiverEntity,            // Receiver
 *     MessageType.ATTACK,        // Message type
 *     attackData                 // Extra data
 * );
 * }
 * </p>
 *
 * @see com.tenio.engine.fsm.entity.AbstractEntity
 * @see com.tenio.engine.message.MessageDispatcher
 * @since 0.5.0
 */
public final class Telegram implements Comparable<Telegram> {

  /**
   * These telegrams will be stored in a priority queue. Therefore, the operator
   * needs to be overloaded so that the PQ can sort the telegrams by time
   * priority. Note how the times must be smaller than SmallestDelay apart before
   * two Telegrams are considered unique.
   */
  public static final double SMALLEST_DELAY = 0.25f;

  /**
   * The id of the sender.
   */
  private final String sender;

  /**
   * The id of the receiver.
   */
  private final String receiver;

  /**
   * The type of this message.
   */
  private final int type;

  /**
   * The creation time.
   */
  private double createdTime;

  /**
   * The message will be sent after an interval time.
   */
  private double delayTime;

  /**
   * The extra information.
   */
  private ExtraMessage info;

  /**
   * Default constructor.
   */
  public Telegram() {
    createdTime = TimeUtility.currentTimeSeconds();
    delayTime = -1;
    sender = null;
    receiver = null;
    type = -1;
  }

  public Telegram(double delayTime, String sender, String receiver, int type) {
    this(delayTime, sender, receiver, type, null);
  }

  /**
   * Initialization with parameters.
   *
   * <p>
   * Creates a new telegram with the specified parameters for message delivery.
   * </p>
   *
   * @param createdTime the time when the telegram is created
   * @param sender      the sender's identifier
   * @param receiver    the receiver's identifier
   * @param type       the type of message
   * @param info       additional information attached to the message
   */
  public Telegram(double createdTime, String sender, String receiver, int type, ExtraMessage info) {
    this.createdTime = createdTime;
    this.sender = sender;
    this.receiver = receiver;
    this.type = type;
    this.info = info;
  }

  /**
   * Retrieves the sender's identifier.
   *
   * @return the sender's identifier
   */
  public String getSender() {
    return sender;
  }

  /**
   * Retrieves the receiver's identifier.
   *
   * @return the receiver's identifier
   */
  public String getReceiver() {
    return receiver;
  }

  /**
   * Retrieves the message type.
   *
   * @return the message type
   */
  public int getType() {
    return type;
  }

  /**
   * Retrieves the creation time of the telegram.
   *
   * @return the creation time
   */
  public double getCreatedTime() {
    return createdTime;
  }

  /**
   * Sets the creation time of the telegram.
   *
   * @param createdTime the new creation time
   */
  public void setCreatedTime(double createdTime) {
    this.createdTime = createdTime;
  }

  /**
   * Retrieves the delay time before message delivery.
   *
   * @return the delay time
   */
  public double getDelayTime() {
    return delayTime;
  }

  /**
   * Sets the delay time before message delivery.
   *
   * @param delayTime the new delay time
   */
  public void setDelayTime(double delayTime) {
    this.delayTime = delayTime;
  }

  /**
   * Retrieves the additional information attached to the message.
   *
   * @return the additional information
   */
  public ExtraMessage getInfo() {
    return info;
  }

  /**
   * Sets the additional information for the message.
   *
   * @param info the new additional information
   */
  public void setInfo(ExtraMessage info) {
    this.info = info;
  }

  /**
   * Compares this telegram with another for priority queue ordering.
   * <p>
   * Telegrams are ordered based on their creation time, with a minimum difference
   * threshold of {@link #SMALLEST_DELAY} to be considered unique.
   * </p>
   *
   * @param other the telegram to compare with
   * @return negative if this telegram should be processed before the other,
   *         positive if after, and zero if they are considered equal
   */
  @Override
  public int compareTo(Telegram other) {
    if (Math.abs(this.createdTime - other.createdTime) < SMALLEST_DELAY) {
      return 0;
    }
    return (this.createdTime - other.createdTime) > 0 ? 1 : -1;
  }

  /**
   * Returns a string representation of the telegram.
   *
   * @return a string containing the telegram's details
   */
  @Override
  public String toString() {
    return String.format("Time: %.2f, Sender: %s, Receiver: %s, Msg: %d", createdTime, sender, receiver, type);
  }
}
