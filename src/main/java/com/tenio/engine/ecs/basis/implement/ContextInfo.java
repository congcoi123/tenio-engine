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
import java.util.Arrays;

/**
 * The context information.
 */
public final class ContextInfo {

  /**
   * List of component names of an entity.
   */
  private final String[] componentNames;
  /**
   * List of component classes of an entity.
   */
  private final Class<?>[] componentTypes;
  /**
   * The context's name.
   */
  private final String name;
  /**
   * The number of component for each entity.
   */
  private final int numberComponents;

  /**
   * Initialization.
   *
   * @param initialSize the initial size of components array
   */
  public ContextInfo(int initialSize) {
    this.name = "Default";
    this.componentNames = new String[initialSize];
    this.componentTypes = new Class<?>[initialSize];
    this.numberComponents = initialSize;
  }

  /**
   * Initialization.
   *
   * @param name             the context's name
   * @param componentNames   list of component's names
   * @param componentTypes   list of component's types
   * @param numberComponents the number of components
   */
  public ContextInfo(String name, String[] componentNames, Class<?>[] componentTypes,
                     int numberComponents) {
    this.name = name;
    this.componentNames = componentNames;
    this.componentTypes = componentTypes;
    this.numberComponents = numberComponents;
  }

  /**
   * Gets the name of the context.
   *
   * @return the context name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the array of component names.
   *
   * @return array of component names
   */
  public String[] getComponentNames() {
    return componentNames;
  }

  /**
   * Gets the array of component types.
   *
   * @return array of component class types
   */
  public Class<?>[] getComponentTypes() {
    return componentTypes;
  }

  /**
   * Gets the total number of components.
   *
   * @return the number of components
   */
  public int getNumberComponents() {
    return numberComponents;
  }

  /**
   * Gets the index for a component type.
   *
   * @param componentClass the component class to get index for
   * @return the index of the component type
   */
  public int getComponentIndex(Class<? extends Component> componentClass) {
    for (int i = 0; i < componentTypes.length; i++) {
      if (componentTypes[i] != null && componentTypes[i].equals(componentClass)) {
        return i;
      }
    }
    // If not found, assign to first empty slot
    for (int i = 0; i < componentTypes.length; i++) {
      if (componentTypes[i] == null) {
        componentTypes[i] = componentClass;
        componentNames[i] = componentClass.getSimpleName();
        return i;
      }
    }
    throw new IllegalStateException("No space available for new component type: " + componentClass.getName());
  }

  @Override
  public String toString() {
    return String.format("ContextInfo{name=%s, numberComponents=%d, componentNames=%s}", name,
        numberComponents,
        Arrays.toString(componentNames));
  }
}
