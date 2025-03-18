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

package com.tenio.engine.ecs.model;

import com.tenio.engine.ecs.basis.Component;
import com.tenio.engine.ecs.basis.Context;
import com.tenio.engine.ecs.basis.implement.ContextImpl;
import com.tenio.engine.ecs.basis.implement.ContextInfo;
import com.tenio.engine.ecs.pool.ComponentPool;
import java.util.Map;

public final class GameContext implements Context<GameEntity> {
  private final ContextImpl<GameEntity> context;

  public GameContext(ContextInfo contextInfo) {
    this.context = new ContextImpl<>(contextInfo.getNumberComponents());
  }

  @Override
  public GameEntity createEntity() {
    return context.createEntity();
  }

  @Override
  public GameEntity getEntity(String entityId) {
    return context.getEntity(entityId);
  }

  @Override
  public void destroyEntity(GameEntity entity) {
    context.destroyEntity(entity);
  }

  @Override
  public boolean hasEntity(GameEntity entity) {
    return context.hasEntity(entity);
  }

  @Override
  public Map<String, GameEntity> getEntities() {
    return context.getEntities();
  }

  @Override
  public ContextInfo getContextInfo() {
    return context.getContextInfo();
  }

  @Override
  public int getEntitiesCount() {
    return context.getEntitiesCount();
  }

  @Override
  public void destroyAllEntities() {
    context.destroyAllEntities();
  }

  @Override
  public void reset() {
    context.reset();
  }

  @Override
  public <C extends Component> ComponentPool<C> getComponentPool(Class<C> componentClass) {
    return context.getComponentPool(componentClass);
  }
}
