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

package com.tenio.engine.physic2d.graphic;

import com.tenio.engine.physic2d.common.BaseGameEntity;
import com.tenio.engine.physic2d.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single cell in a spatial partitioning grid.
 *
 * <p>This class manages a collection of game entities that fall within its spatial boundaries.
 * It provides methods for adding, removing, and querying entities within the cell.
 *
 * <p>Example usage:
 * {@code
 * Cell cell = new Cell(x, y, width, height);
 * cell.addEntity(entity);
 * List<BaseGameEntity> entities = cell.getEntities();
 * }
 *
 * @see com.tenio.engine.physic2d.common.BaseGameEntity
 * @see com.tenio.engine.physic2d.graphic.CellSpacePartition
 * @since 0.5.0
 */
public final class Cell {

  private final Vector2 topLeft;
  private final float width;
  private final float height;
  private final List<BaseGameEntity> entities;

  /**
   * Creates a new cell with the specified position and dimensions.
   *
   * @param topLeft the top-left position of the cell
   * @param width   the width of the cell
   * @param height  the height of the cell
   */
  public Cell(Vector2 topLeft, float width, float height) {
    this.topLeft = topLeft;
    this.width = width;
    this.height = height;
    this.entities = new ArrayList<>();
  }

  /**
   * Adds an entity to this cell.
   *
   * @param entity the entity to add
   */
  public void addEntity(BaseGameEntity entity) {
    entities.add(entity);
  }

  /**
   * Removes an entity from this cell.
   *
   * @param entity the entity to remove
   */
  public void removeEntity(BaseGameEntity entity) {
    entities.remove(entity);
  }

  /**
   * Gets all entities in this cell.
   *
   * @return the list of entities in this cell
   */
  public List<BaseGameEntity> getEntities() {
    return entities;
  }

  /**
   * Gets the top-left position of this cell.
   *
   * @return the top-left position
   */
  public Vector2 getTopLeft() {
    return topLeft;
  }

  /**
   * Gets the width of this cell.
   *
   * @return the cell width
   */
  public float getWidth() {
    return width;
  }

  /**
   * Gets the height of this cell.
   *
   * @return the cell height
   */
  public float getHeight() {
    return height;
  }

  /**
   * Checks if a point is within this cell's boundaries.
   *
   * @param point the point to check
   * @return true if the point is within the cell, false otherwise
   */
  public boolean containsPoint(Vector2 point) {
    return (point.getX() >= topLeft.getX() && point.getX() < topLeft.getX() + width &&
            point.getY() >= topLeft.getY() && point.getY() < topLeft.getY() + height);
  }

} 