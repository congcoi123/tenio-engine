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
 * A spatial partitioning system that divides space into a grid of cells.
 *
 * <p>This class implements a spatial partitioning system that divides a 2D space into a grid of cells,
 * allowing for efficient neighbor queries and collision detection. Each cell maintains a list of entities
 * that intersect with it.
 *
 * <p>Example usage:
 * {@code
 * // Create a partition for a 1000x1000 world with 10x10 cells
 * CellSpacePartition partition = new CellSpacePartition(
 *     1000, 1000,  // World dimensions
 *     10, 10       // Cell divisions
 * );
 *
 * // Add entities to the partition
 * partition.addEntity(entity);
 *
 * // Query entities in range
 * List<BaseGameEntity> neighbors = partition.calculateNeighbors(position, radius);
 * }
 *
 * @see com.tenio.engine.physic2d.graphic.Cell
 * @see com.tenio.engine.physic2d.common.BaseGameEntity
 * @see com.tenio.engine.physic2d.math.Vector2
 * @since 0.5.0
 */
public final class CellSpacePartition {

  private final List<Cell> cells;
  private final int numCellsX;
  private final int numCellsY;
  private final float cellSizeX;
  private final float cellSizeY;

  /**
   * Creates a new spatial partition with the specified dimensions and cell count.
   *
   * @param width        the total width of the space
   * @param height       the total height of the space
   * @param cellsX      the number of cells in the X direction
   * @param cellsY      the number of cells in the Y direction
   * @param maxEntities the expected maximum number of entities
   */
  public CellSpacePartition(float width, float height, int cellsX, int cellsY,
      int maxEntities) {
    numCellsX = cellsX;
    numCellsY = cellsY;
    cellSizeX = width / cellsX;
    cellSizeY = height / cellsY;

    // Create the cells
    cells = new ArrayList<>(numCellsX * numCellsY);
    for (int y = 0; y < numCellsY; y++) {
      for (int x = 0; x < numCellsX; x++) {
        Vector2 topLeft = new Vector2(x * cellSizeX, y * cellSizeY);
        cells.add(new Cell(topLeft, cellSizeX, cellSizeY));
      }
    }
  }

  /**
   * Adds an entity to the appropriate cell(s) based on its position.
   *
   * @param entity the entity to add
   */
  public void addEntity(BaseGameEntity entity) {
    int cellIndex = positionToIndex(entity.getPosition());
    cells.get(cellIndex).addEntity(entity);
  }

  /**
   * Updates an entity's position in the partition.
   * This method should be called after an entity's position has changed.
   *
   * @param entity the entity to update
   */
  public void updateEntity(BaseGameEntity entity) {
    // Find the entity in all cells and remove it
    for (Cell cell : cells) {
      cell.removeEntity(entity);
    }

    // Add to the correct cell based on new position
    int newIndex = positionToIndex(entity.getPosition());
    cells.get(newIndex).addEntity(entity);
  }

  /**
   * Calculates all entities within a specified range of a position.
   *
   * @param position the center position to check from
   * @param radius   the radius to check within
   * @return a list of entities within the specified range
   */
  public List<BaseGameEntity> calculateNeighbors(Vector2 position, float radius) {
    List<BaseGameEntity> neighbors = new ArrayList<>();
    float radiusSq = radius * radius;

    // Calculate the cell range to check
    float minX = position.getX() - radius;
    float maxX = position.getX() + radius;
    float minY = position.getY() - radius;
    float maxY = position.getY() + radius;

    int startX = (int) Math.max(0, (minX) / cellSizeX);
    int endX = (int) Math.min(numCellsX - 1, (maxX) / cellSizeX);
    int startY = (int) Math.max(0, (minY) / cellSizeY);
    int endY = (int) Math.min(numCellsY - 1, (maxY) / cellSizeY);

    // Check each cell in range
    for (int y = startY; y <= endY; y++) {
      for (int x = startX; x <= endX; x++) {
        Cell cell = cells.get(y * numCellsX + x);
        for (BaseGameEntity entity : cell.getEntities()) {
          float distSq = position.distanceSq(entity.getPosition());
          if (distSq <= radiusSq) {
            neighbors.add(entity);
          }
        }
      }
    }

    return neighbors;
  }

  /**
   * Converts a position to a cell index.
   *
   * @param position the position to convert
   * @return the index of the cell containing the position
   */
  private int positionToIndex(Vector2 position) {
    float spaceLeft = 0;
    float spaceBottom = 0;
    float cellSizeInv = 1.0f / cellSizeX;

    int idx = (int) ((position.getX() - spaceLeft) * cellSizeInv);
    int idy = (int) ((position.getY() - spaceBottom) * cellSizeInv);

    // Clamp to grid bounds
    idx = Math.min(numCellsX - 1, Math.max(0, idx));
    idy = Math.min(numCellsY - 1, Math.max(0, idy));

    return idy * numCellsX + idx;
  }

  /**
   * Gets all cells in the partition.
   *
   * @return the list of cells
   */
  public List<Cell> getCells() {
    return cells;
  }

} 