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

import com.tenio.common.constant.CommonConstant;
import com.tenio.common.logger.SystemLogger;
import com.tenio.common.pool.ElementPool;
import com.tenio.engine.ecs.basis.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.concurrent.GuardedBy;

/**
 * The ComponentPool class manages a pool of reusable components in the Entity
 * Component System (ECS). It provides efficient memory management by recycling
 * component instances instead of creating new ones.
 *
 * <p>
 * Features:
 * - Component instance pooling
 * - Automatic pool expansion
 * - Memory optimization
 * - Type-safe component management
 * </p>
 *
 * <p>
 * The pool system supports:
 * - Dynamic pool sizing
 * - Component recycling
 * - Memory efficiency
 * - Performance optimization
 * </p>
 *
 * <p>
 * Example usage:
 * {@code
 * // Create a pool for components
 * ComponentPool<Component> pool = new ComponentPool<>(
 *     Component.class,
 *     100  // Initial capacity
 * );
 * 
 * // Get a component from the pool
 * Component component = pool.get();
 * 
 * // Return component to the pool when done
 * pool.repay(component);
 * 
 * // Create multiple components
 * List<Component> components = pool.getAll(10);
 * 
 * // Return multiple components
 * pool.repayAll(components);
 * }
 * </p>
 *
 * @param <T> the type of component managed by this pool
 * @see com.tenio.engine.ecs.basis.Component
 * @since 0.5.0
 */
public final class ComponentPool<T extends Component> extends SystemLogger implements ElementPool<T> {

  private final Class<T> componentClass;
  @GuardedBy("this")
  private List<T> availableComponents;
  @GuardedBy("this")
  private List<T> usedComponents;

  /**
   * Creates a new component pool with the specified initial capacity.
   *
   * @param componentClass the class of components to manage
   * @param initialSize   the initial size of the pool
   */
  public ComponentPool(Class<T> componentClass, int initialSize) {
    this.componentClass = componentClass;
    this.availableComponents = new ArrayList<>(initialSize);
    this.usedComponents = new ArrayList<>(initialSize);

    // Initialize the pool with components
    for (int i = 0; i < initialSize; i++) {
      try {
        availableComponents.add(componentClass.getDeclaredConstructor().newInstance());
      } catch (Exception e) {
        throw new RuntimeException("Failed to create component instance", e);
      }
    }
  }

  /**
   * Gets a component from the pool. If no components are available,
   * the pool will automatically expand.
   *
   * @return a component instance
   */
  @Override
  public synchronized T get() {
    if (availableComponents.isEmpty()) {
      expandPool();
    }
    T component = availableComponents.remove(availableComponents.size() - 1);
    usedComponents.add(component);
    return component;
  }

  /**
   * Gets multiple components from the pool.
   *
   * @param count the number of components to get
   * @return a list of component instances
   */
  public List<T> getAll(int count) {
    List<T> components = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      components.add(get());
    }
    return components;
  }

  /**
   * Returns a component to the pool.
   *
   * @param component the component to return
   */
  @Override
  public synchronized void repay(T component) {
    if (usedComponents.remove(component)) {
      availableComponents.add(component);
    }
  }

  /**
   * Returns multiple components to the pool.
   *
   * @param components the components to return
   */
  public void repayAll(List<T> components) {
    for (T component : components) {
      repay(component);
    }
  }

  /**
   * Gets the number of available components in the pool.
   *
   * @return the number of available components
   */
  public int getAvailableCount() {
    return availableComponents.size();
  }

  /**
   * Gets the number of components currently in use.
   *
   * @return the number of used components
   */
  public int getUsedCount() {
    return usedComponents.size();
  }

  /**
   * Expands the pool by creating new component instances.
   */
  private void expandPool() {
    int currentSize = availableComponents.size() + usedComponents.size();
    int expansionSize = Math.max(10, currentSize / 2);

    for (int i = 0; i < expansionSize; i++) {
      try {
        availableComponents.add(componentClass.getDeclaredConstructor().newInstance());
      } catch (Exception e) {
        throw new RuntimeException("Failed to create component instance", e);
      }
    }
  }

  @Override
  public synchronized void cleanup() {
    // Reset to initial size
    availableComponents.clear();
    usedComponents.clear();

    // Reinitialize all components
    for (int i = 0; i < CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL; i++) {
      try {
        availableComponents.add((T) componentClass.getDeclaredConstructor().newInstance());
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
        if (isErrorEnabled()) {
          error(exception);
        }
      }
    }
  }

  @Override
  public synchronized int getPoolSize() {
    return (availableComponents.size() + usedComponents.size() == CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL) ?
        CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL : -1;
  }

  @Override
  public int getAvailableSlot() {
    int slot = 0;
    for (T component : availableComponents) {
      if (component != null) {
        slot++;
      }
    }

    return slot;
  }
}
