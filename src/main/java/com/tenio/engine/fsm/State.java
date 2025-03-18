package com.tenio.engine.fsm;

import com.tenio.engine.fsm.entity.AbstractEntity;
import com.tenio.engine.fsm.entity.Telegram;

/**
 * An interface that defines the structure of a state in a Finite State Machine (FSM).
 * Each state represents a specific behavior or condition that an entity can be in.
 *
 * <p>
 * Features:
 * - State entry and exit handling
 * - State update logic
 * - Message handling within states
 * </p>
 *
 * <p>
 * The state interface is designed to work with any entity type that extends
 * {@link com.tenio.engine.fsm.entity.AbstractEntity}. Each state implementation
 * should provide specific behavior for the entity when it enters, exits, or
 * updates in that state.
 * </p>
 *
 * <p>
 * Example usage:
 * {@code
 * public class IdleState implements State<AbstractEntity> {
 *     @Override
 *     public void enter(AbstractEntity entity) {
 *         // Setup when entering idle state
 *         entity.setVelocity(0, 0);
 *     }
 *
 *     @Override
 *     public void execute(AbstractEntity entity) {
 *         // Idle state behavior
 *         if (entity.isTargetNearby()) {
 *             entity.getFSM().changeState(new ChaseState());
 *         }
 *     }
 *
 *     @Override
 *     public void exit(AbstractEntity entity) {
 *         // Cleanup when leaving idle state
 *     }
 *
 *     @Override
 *     public boolean onMessage(AbstractEntity entity, Telegram telegram) {
 *         if (telegram.getType().equals("MOVE")) {
 *             // Handle move command
 *             return true;
 *         }
 *         return false;
 *     }
 * }
 * }
 * </p>
 *
 * @param <T> the type of entity this state can handle
 * @see com.tenio.engine.fsm.entity.AbstractEntity
 * @see com.tenio.engine.fsm.entity.Telegram
 */
public interface State<T extends AbstractEntity> {

  /**
   * Called when the state is entered.
   *
   * @param entity the entity entering this state
   */
  void enter(T entity);

  /**
   * Called each update step while in this state.
   *
   * @param entity the entity currently in this state
   */
  void execute(T entity);

  /**
   * Called when the state is exited.
   *
   * @param entity the entity exiting this state
   */
  void exit(T entity);

  /**
   * Called to handle messages while in this state.
   *
   * @param entity   the entity receiving the message
   * @param telegram the message received
   * @return true if the message was handled, false otherwise
   */
  boolean onMessage(T entity, Telegram telegram);

}
// ... existing code ...