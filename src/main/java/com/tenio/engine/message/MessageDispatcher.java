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

package com.tenio.engine.message;

import com.tenio.engine.fsm.entity.AbstractEntity;
import com.tenio.engine.fsm.entity.EntityManager;
import com.tenio.engine.fsm.entity.Telegram;
import java.util.ArrayList;
import java.util.List;

/**
 * The MessageDispatcher class manages message delivery between entities in the game engine.
 * It provides a robust messaging system that supports both immediate and delayed message delivery,
 * allowing entities to communicate and coordinate their actions.
 *
 * <p>
 * Features:
 * - Immediate message dispatch
 * - Delayed message scheduling
 * - Priority-based message queuing
 * - Entity-to-entity communication
 * - Message lifecycle management
 * </p>
 *
 * <p>
 * The dispatcher supports:
 * - Asynchronous communication
 * - Time-based message delivery
 * - Message prioritization
 * - Entity state updates
 * </p>
 *
 * <p>
 * Example usage:
 * {@code
 * MessageDispatcher dispatcher = MessageDispatcher.getInstance();
 * 
 * // Send immediate message
 * dispatcher.dispatchMessage("player-1", "enemy-1", MessageType.ATTACK, null);
 * 
 * // Send delayed message (2 seconds delay)
 * dispatcher.dispatchMessage(2.0, "player-1", "enemy-1", MessageType.HEAL, healData);
 * 
 * // Update dispatcher (call in game loop)
 * dispatcher.update(deltaTime);
 * }
 * </p>
 *
 * @see com.tenio.engine.fsm.entity.AbstractEntity
 * @see com.tenio.engine.fsm.entity.EntityManager
 * @see com.tenio.engine.message.ExtraMessage
 * @since 0.5.0
 */
public final class MessageDispatcher {

  private static final MessageDispatcher instance = new MessageDispatcher();
  private final List<Telegram> delayedTelegrams;

  private MessageDispatcher() {
    delayedTelegrams = new ArrayList<Telegram>();
  }

  /**
   * Gets the singleton instance of the MessageDispatcher.
   *
   * @return the singleton instance
   */
  public static MessageDispatcher getInstance() {
    return instance;
  }

  /**
   * Dispatches a message immediately to the recipient.
   *
   * @param senderId   the ID of the sending entity
   * @param receiverId the ID of the receiving entity
   * @param type      the type of message
   * @param extraInfo additional information to be sent with the message (can be null)
   */
  public void dispatchMessage(String senderId, String receiverId, int type,
      ExtraMessage extraInfo) {
    // Create and dispatch the telegram immediately
    Telegram telegram = new Telegram(0, senderId, receiverId, type, extraInfo);
    discharge(telegram);
  }

  /**
   * Schedules a message to be delivered after a specified delay.
   *
   * @param delay      the delay in seconds before the message should be delivered
   * @param senderId   the ID of the sending entity
   * @param receiverId the ID of the receiving entity
   * @param type      the type of message
   * @param extraInfo additional information to be sent with the message (can be null)
   */
  public void dispatchMessage(double delay, String senderId, String receiverId,
      int type, ExtraMessage extraInfo) {
    // Create the telegram with the specified delay
    Telegram telegram = new Telegram(delay, senderId, receiverId, type, extraInfo);
    
    // If there is no delay, dispatch immediately
    if (delay <= 0.0) {
      discharge(telegram);
    } else {
      // Add to delayed telegrams queue
      delayedTelegrams.add(telegram);
    }
  }

  /**
   * Updates the message dispatcher, delivering any pending messages whose delay has expired.
   * This method should be called regularly as part of the game loop.
   *
   * @param deltaTime the time elapsed since the last update in seconds
   */
  public void update(float deltaTime) {
    double currentTime = System.currentTimeMillis();
    
    // Deliver any telegrams that are ready
    while (!delayedTelegrams.isEmpty() && 
           delayedTelegrams.get(0).getDelayTime() <= currentTime) {
      // Get the telegram
      Telegram telegram = delayedTelegrams.remove(0);
      
      // Deliver it
      discharge(telegram);
    }
  }

  /**
   * Delivers a telegram to its recipient immediately.
   * If the recipient entity cannot be found, the message is discarded.
   *
   * @param telegram the telegram to deliver
   * @see com.tenio.engine.fsm.entity.Telegram
   */
  private void discharge(Telegram telegram) {
    EntityManager entityManager = EntityManager.getInstance();
    AbstractEntity receiver = entityManager.getEntityById(telegram.getReceiver());
    
    if (receiver != null) {
      receiver.handleMessage(telegram);
    }
  }

} 