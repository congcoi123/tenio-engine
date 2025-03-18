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

package com.tenio.engine.ecs.pool;

import com.tenio.common.exception.NullElementPoolException;
import com.tenio.common.logger.SystemLogger;
import com.tenio.common.pool.ElementPool;
import com.tenio.engine.ecs.basis.Entity;
import com.tenio.engine.ecs.basis.implement.ContextInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.concurrent.GuardedBy;

/**
 * The EntityPool class manages a pool of reusable entities in the Entity Component
 * System (ECS). It provides efficient memory management by recycling entity
 * instances instead of creating new ones.
 *
 * <p>
 * Features:
 * - Entity instance pooling
 * - Automatic pool expansion
 * - Memory optimization
 * - Type-safe entity management
 * </p>
 *
 * <p>
 * The pool system supports:
 * - Dynamic pool sizing
 * - Entity recycling
 * - Memory efficiency
 * - Performance optimization
 * </p>
 *
 * <p>
 * Example usage:
 * {@code
 * // Create a pool for game entities
 * EntityPool<Entity> pool = new EntityPool<>(
 *     Entity.class,
 *     100  // Initial capacity
 * );
 * 
 * // Get an entity from the pool
 * Entity entity = pool.get();
 * entity.setId("entity1");
 * 
 * // Return entity to the pool when done
 * pool.repay(entity);
 * 
 * // Create multiple entities
 * List<Entity> entities = pool.getAll(10);
 * 
 * // Return multiple entities
 * pool.repayAll(entities);
 * }
 * </p>
 *
 * @param <T> the type of entity managed by this pool
 * @see com.tenio.engine.ecs.basis.Entity
 * @since 0.5.0
 */
public final class EntityPool<T extends Entity> extends SystemLogger implements ElementPool<T> {

  private final Class<T> entityClass;
  private final ContextInfo contextInfo;
  @GuardedBy("this")
  private List<T> availableEntities;
  @GuardedBy("this")
  private List<T> usedEntities;

  /**
   * Creates a new entity pool with the specified initial capacity.
   *
   * @param entityClass the class of entities to manage
   * @param initialSize the initial size of the pool
   */
  public EntityPool(Class<T> entityClass, int initialSize) {
    this.entityClass = entityClass;
    this.contextInfo = null;
    this.availableEntities = new ArrayList<>(initialSize);
    this.usedEntities = new ArrayList<>(initialSize);

    // Initialize the pool with entities
    for (int i = 0; i < initialSize; i++) {
      try {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        entity.setId(UUID.randomUUID().toString());
        entity.setContextInfo(contextInfo);
        availableEntities.add(entity);
      } catch (Exception e) {
        throw new RuntimeException("Failed to create entity instance", e);
      }
    }
  }

  /**
   * Gets an entity from the pool. If no entities are available,
   * the pool will automatically expand.
   *
   * @return an entity instance
   */
  @Override
  public synchronized T get() {
    if (availableEntities.isEmpty()) {
      expandPool();
    }
    T entity = availableEntities.remove(availableEntities.size() - 1);
    usedEntities.add(entity);
    return entity;
  }

  /**
   * Gets multiple entities from the pool.
   *
   * @param count the number of entities to get
   * @return a list of entity instances
   */
  public List<T> getAll(int count) {
    List<T> entities = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      entities.add(get());
    }
    return entities;
  }

  /**
   * Returns an entity to the pool.
   *
   * @param entity the entity to return
   */
  @Override
  public synchronized void repay(T entity) {
    if (entity == null) {
      throw new NullElementPoolException("Cannot repay null entity");
    }
    
    boolean flagFound = false;
    for (int i = 0; i < availableEntities.size(); i++) {
      if (availableEntities.get(i) == entity) {
        availableEntities.remove(i);
        entity.reset();
        entity.setId(UUID.randomUUID().toString()); // Reset ID to a new value
        flagFound = true;
        break;
      }
    }
    if (!flagFound) {
      throw new NullElementPoolException("Entity not found in pool");
    }
  }

  /**
   * Returns multiple entities to the pool.
   *
   * @param entities the entities to return
   */
  public void repayAll(List<T> entities) {
    for (T entity : entities) {
      repay(entity);
    }
  }

  /**
   * Gets the number of available entities in the pool.
   *
   * @return the number of available entities
   */
  @Override
  public synchronized int getPoolSize() {
    return availableEntities.size() + usedEntities.size();
  }

  /**
   * Gets the number of available entities in the pool.
   *
   * @return the number of available entities
   */
  public int getAvailableCount() {
    return availableEntities.size();
  }

  /**
   * Gets the number of entities currently in use.
   *
   * @return the number of used entities
   */
  public int getUsedCount() {
    return usedEntities.size();
  }

  /**
   * Expands the pool by creating new entity instances.
   */
  private void expandPool() {
    int currentSize = availableEntities.size() + usedEntities.size();
    int expansionSize = Math.max(10, currentSize / 2);

    for (int i = 0; i < expansionSize; i++) {
      try {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        entity.setId(UUID.randomUUID().toString());
        entity.setContextInfo(contextInfo);
        availableEntities.add(entity);
      } catch (Exception e) {
        throw new RuntimeException("Failed to create entity instance", e);
      }
    }

    if (isInfoEnabled()) {
      info("COMPONENT POOL", buildgen("Increase the number of elements by ",
          expansionSize, " to ", getPoolSize()));
    }
  }

  @Override
  public synchronized void cleanup() {
    // Reset to initial size
    availableEntities.clear();
    usedEntities.clear();

    // Reinitialize all entities
    for (int i = 0; i < getPoolSize(); i++) {
      try {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        entity.setId(UUID.randomUUID().toString());
        entity.setContextInfo(contextInfo);
        availableEntities.add(entity);
      } catch (Exception e) {
        if (isErrorEnabled()) {
          error(e);
        }
      }
    }
  }

  @Override
  public int getAvailableSlot() {
    return availableEntities.size();
  }
}
