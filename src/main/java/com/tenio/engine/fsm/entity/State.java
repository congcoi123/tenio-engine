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


/**
 * Defines the structure of a state in a Finite State Machine (FSM).
 *
 * <p>This interface provides the core functionality needed to implement states in an FSM. Each state can
 * handle entry and exit actions, update logic, and message handling.
 *
 * <p>Example implementation:
 * {@code
 * public class IdleState implements State<AbstractEntity> {
 *     @Override
 *     public void enter(AbstractEntity entity) {
 *         // Perform entry actions
 *     }
 *
 *     @Override
 *     public void execute(AbstractEntity entity) {
 *         // Update state logic
 *     }
 *
 *     @Override
 *     public void exit(AbstractEntity entity) {
 *         // Perform exit actions
 *     }
 *
 *     @Override
 *     public boolean onMessage(AbstractEntity entity, Telegram telegram) {
 *         // Handle messages
 *         return true;
 *     }
 * }
 * }
 *
 * @param <T> The type of entity this state can handle
 * @see com.tenio.engine.fsm.entity.AbstractEntity
 * @see com.tenio.engine.fsm.entity.Telegram
 * @since 0.5.0
 */
public interface State<T extends AbstractEntity> {

  /**
   * Called when the state is entered.
   *
   * @param entity the entity entering this state
   */
  void enter(T entity);

  /**
   * Called by the FSM's update function.
   *
   * @param entity the entity in this state
   */
  void execute(T entity);

  /**
   * Called when the state is exited.
   *
   * @param entity the entity exiting this state
   */
  void exit(T entity);

  /**
   * This executes if the entity receives a message from the message dispatcher.
   *
   * @param entity   the entity receiving the message
   * @param telegram the message
   * @return true if the message was handled
   */
  boolean onMessage(T entity, Telegram telegram);
}
