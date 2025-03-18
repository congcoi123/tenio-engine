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

import com.tenio.engine.ecs.basis.implement.EntityImpl;
import com.tenio.engine.ecs.model.component.Position;
import com.tenio.engine.ecs.model.component.Animation;
import com.tenio.engine.ecs.model.component.Motion;
import com.tenio.engine.ecs.model.component.View;
import com.tenio.engine.ecs.pool.ComponentPool;

public final class GameEntity extends EntityImpl {

  public boolean isAnimation() {
    return hasComponent(GameComponent.ANIMATION);
  }

  @SuppressWarnings("unchecked")
  public GameEntity setAnimation(boolean value) {
    if (value != hasComponent(GameComponent.ANIMATION)) {
      if (value) {
        setComponent(GameComponent.ANIMATION, ((ComponentPool<Animation>)getComponentPools().get(Animation.class)).get());
      } else {
        ((ComponentPool<Animation>)getComponentPools().get(Animation.class)).repay((Animation)getComponent(GameComponent.ANIMATION));
        removeComponent(GameComponent.ANIMATION);
      }
    }
    return this;
  }

  public boolean isMotion() {
    return hasComponent(GameComponent.MOTION);
  }

  @SuppressWarnings("unchecked")
  public GameEntity setMotion(boolean value) {
    if (value != hasComponent(GameComponent.MOTION)) {
      if (value) {
        setComponent(GameComponent.MOTION, ((ComponentPool<Motion>)getComponentPools().get(Motion.class)).get());
      } else {
        ((ComponentPool<Motion>)getComponentPools().get(Motion.class)).repay((Motion)getComponent(GameComponent.MOTION));
        removeComponent(GameComponent.MOTION);
      }
    }
    return this;
  }

  public boolean isView() {
    return hasComponent(GameComponent.VIEW);
  }

  @SuppressWarnings("unchecked")
  public GameEntity setView(boolean value) {
    if (value != hasComponent(GameComponent.VIEW)) {
      if (value) {
        setComponent(GameComponent.VIEW, ((ComponentPool<View>)getComponentPools().get(View.class)).get());
      } else {
        ((ComponentPool<View>)getComponentPools().get(View.class)).repay((View)getComponent(GameComponent.VIEW));
        removeComponent(GameComponent.VIEW);
      }
    }
    return this;
  }

  public boolean hasPosition() {
    return hasComponent(GameComponent.POSITION);
  }

  @SuppressWarnings("unchecked")
  public GameEntity setPosition(float x, float y) {
    var component = ((ComponentPool<Position>)getComponentPools().get(Position.class)).get();
    component.x = x;
    component.y = y;
    setComponent(GameComponent.POSITION, component);
    return this;
  }

  @SuppressWarnings("unchecked")
  public GameEntity replacePosition(float x, float y) {
    var component = ((ComponentPool<Position>)getComponentPools().get(Position.class)).get();
    component.x = x;
    component.y = y;
    replaceComponent(GameComponent.POSITION, component);
    return this;
  }

  @SuppressWarnings("unchecked")
  public GameEntity removePosition() {
    ((ComponentPool<Position>)getComponentPools().get(Position.class)).repay((Position)getComponent(GameComponent.POSITION));
    removeComponent(GameComponent.POSITION);
    return this;
  }

  @Override
  public String toString() {
    return "game-entity";
  }
}
