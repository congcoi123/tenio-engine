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

package com.tenio.engine.ecs.basis;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a relationship between entities in the ECS system.
 *
 * <p>This class manages parent-child relationships between entities, allowing for
 * hierarchical organization of game objects.
 *
 * <p>Example usage:
 * {@code
 * EntitiesRelationship relationship = new EntitiesRelationship();
 * relationship.setParent(parentEntity);
 * relationship.addChild(childEntity);
 * List<Entity> children = relationship.getChildren();
 * }
 *
 * @see com.tenio.engine.ecs.basis.Entity
 * @since 0.5.0
 */
public final class EntitiesRelationship {

  private final Entity parent;
  private final List<Entity> children;

  /**
   * Creates a new relationship with the specified parent entity.
   *
   * @param parent the parent entity
   */
  public EntitiesRelationship(Entity parent) {
    this.parent = parent;
    this.children = new ArrayList<>();
  }

  /**
   * Gets the parent entity.
   *
   * @return the parent entity
   */
  public Entity getParent() {
    return parent;
  }

  /**
   * Gets all child entities.
   *
   * @return the list of child entities
   */
  public List<Entity> getChildren() {
    return children;
  }

  /**
   * Adds a child entity to the relationship.
   *
   * @param child the child entity to add
   */
  public void addChild(Entity child) {
    if (!children.contains(child)) {
      children.add(child);
    }
  }

  /**
   * Removes a child entity from the relationship.
   *
   * @param child the child entity to remove
   */
  public void removeChild(Entity child) {
    children.remove(child);
  }

  /**
   * Removes all child entities from the relationship.
   */
  public void clearChildren() {
    children.clear();
  }

  /**
   * Checks if an entity is a child in this relationship.
   *
   * @param entity the entity to check
   * @return true if the entity is a child, false otherwise
   */
  public boolean hasChild(Entity entity) {
    return children.contains(entity);
  }

  /**
   * Gets the number of child entities.
   *
   * @return the number of children
   */
  public int getChildCount() {
    return children.size();
  }

} 