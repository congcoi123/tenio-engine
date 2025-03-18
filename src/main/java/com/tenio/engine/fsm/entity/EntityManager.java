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

import java.util.HashMap;
import java.util.Map;

/**
 * A singleton manager for handling game entities in a Finite State Machine (FSM) system.
 *
 * <p>This class provides centralized management of game entities, including creation, retrieval,
 * and removal operations. It ensures that each entity has a unique identifier and maintains
 * the lifecycle of all entities in the system.
 *
 * <p>Example usage:
 * {@code
 * EntityManager manager = EntityManager.getInstance();
 * manager.add("player", playerEntity);
 * AbstractEntity entity = manager.get("player");
 * manager.remove("player");
 * }
 *
 * @see com.tenio.engine.fsm.entity.AbstractEntity
 * @since 0.5.0
 */
public final class EntityManager {

  private static final EntityManager instance = new EntityManager();
  private final Map<String, AbstractEntity> entityMap;

  private EntityManager() {
    entityMap = new HashMap<>();
  }

  public static EntityManager getInstance() {
    return instance;
  }

  /**
   * Registers an entity with the manager.
   *
   * @param entity the entity to register
   */
  public void registerEntity(AbstractEntity entity) {
    if (entity != null && entity.getId() != null) {
      entityMap.put(entity.getId(), entity);
    }
  }

  /**
   * Removes an entity from the manager.
   *
   * @param entityId the ID of the entity to remove
   */
  public void removeEntity(String entityId) {
    entityMap.remove(entityId);
  }

  /**
   * Retrieves an entity by its ID.
   *
   * @param entityId the ID of the entity to retrieve
   * @return the entity, or null if not found
   */
  public AbstractEntity getEntityById(String entityId) {
    return entityMap.get(entityId);
  }

  /**
   * Updates all registered entities.
   * This method should be called each frame to update entity states.
   * 
   * @param deltaTime the time elapsed since the last update in seconds
   */
  public void updateEntities(float deltaTime) {
    for (AbstractEntity entity : entityMap.values()) {
      entity.update(deltaTime);
    }
  }
} 