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

package com.tenio.engine.ecs.basis.implement;

import com.tenio.engine.ecs.basis.Component;
import com.tenio.engine.ecs.basis.Context;
import com.tenio.engine.ecs.basis.Entity;
import com.tenio.engine.ecs.pool.ComponentPool;
import com.tenio.engine.ecs.pool.EntityPool;
import java.util.HashMap;
import java.util.Map;

/**
 * A context manages the life-cycle of entities and groups. You can create and
 * destroy entities and get groups of entities.
 *
 * @see Context
 */
public final class ContextImpl<T extends Entity> implements Context<T> {

  private static final int DEFAULT_NUMBER_ELEMENTS_POOL = 100;

  private final ContextInfo contextInfo;
  private final Map<Class<? extends Component>, ComponentPool<? extends Component>> componentPools;
  private final EntityPool<T> entityPool;
  private final Map<String, T> entities;

  /**
   * Creates a new context implementation with the specified initial size.
   *
   * @param initialSize the initial size for entity and component pools
   */
  @SuppressWarnings("unchecked")
  public ContextImpl(int initialSize) {
    this.contextInfo = new ContextInfo(initialSize);
    this.componentPools = new HashMap<>();
    this.entityPool = new EntityPool<>((Class<T>) EntityImpl.class, initialSize);
    this.entities = new HashMap<>();
  }

  @Override
  public T createEntity() {
    var entity = (T) entityPool.get();
    entity.setComponentPools(componentPools);
    entity.setContextInfo(contextInfo);
    entities.put(entity.getId(), entity);
    return entity;
  }

  @Override
  public T getEntity(String entityId) {
    return entities.get(entityId);
  }

  @Override
  public void destroyEntity(T entity) {
    entity.reset();
    entities.remove(entity.getId());
    entityPool.repay(entity);
  }

  @Override
  public boolean hasEntity(T entity) {
    return entities.containsKey(entity.getId());
  }

  @Override
  public Map<String, T> getEntities() {
    return entities;
  }

  @Override
  public ContextInfo getContextInfo() {
    return contextInfo;
  }

  @Override
  public int getEntitiesCount() {
    return entities.size();
  }

  @Override
  public void destroyAllEntities() {
    for (T entity : entities.values()) {
      entity.reset();
    }
    entities.clear();
  }

  @Override
  public void reset() {
    for (T entity : entities.values()) {
      entity.reset();
    }
    componentPools.clear();
    entities.clear();
  }

  @Override
  public <C extends Component> ComponentPool<C> getComponentPool(Class<C> componentClass) {
    if (!componentPools.containsKey(componentClass)) {
      componentPools.put(componentClass, new ComponentPool<>(componentClass, DEFAULT_NUMBER_ELEMENTS_POOL));
    }
    @SuppressWarnings("unchecked")
    ComponentPool<C> pool = (ComponentPool<C>) componentPools.get(componentClass);
    return pool;
  }
}
