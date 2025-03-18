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

package com.tenio.engine.physic2d.utility;

import com.tenio.engine.physic2d.math.Matrix3;
import com.tenio.engine.physic2d.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides utility methods for transforming 2D vectors between world and local space coordinates.
 * This class implements common transformation operations used in 2D graphics and physics simulations,
 * such as point/vector transformations, rotations, and coordinate space conversions.
 * <p>
 * The class uses Matrix3 for transformation operations and supports:
 * - Point to world space conversion
 * - Vector to world space conversion
 * - Point to local space conversion
 * - Vector to local space conversion
 * - Batch transformations of points
 * <p>
 * All methods are thread-safe as they create new instances for transformed vectors rather than
 * modifying existing ones.
 *
 * @see Matrix3
 * @see Vector2
 * @since 0.5.0
 */
public final class Transformation {

  /**
   * Given a list of 2D vectors (points), a position, orientation and scale, this function
   * transforms the 2D vectors into the object's world space.
   *
   * @param points   a list of 2D vectors
   * @param position the position
   * @param forward  the forward vector
   * @param side     the side vector
   * @param scale    the scale value
   * @return a list of vectors in world space
   */
  public static List<Vector2> pointsToWorldSpace(List<Vector2> points, Vector2 position,
                                                 Vector2 forward,
                                                 Vector2 side, Vector2 scale) {
    // copy the original vertices into the buffer about to be transformed
    final List<Vector2> tranVector2Ds = clone(points);

    // create a transformation matrix
    Matrix3 matrix = Matrix3.newInstance();

    // scale
    if ((scale.getX() != 1) || (scale.getY() != 1)) {
      matrix.scale(scale.getX(), scale.getY());
    }

    // rotate
    matrix.rotate(forward, side);

    // and translate
    matrix.translate(position.getX(), position.getY());

    // now transform the object's vertices
    matrix.transformVector2Ds(tranVector2Ds);

    return tranVector2Ds;
  }

  /**
   * Given a list of 2D vectors (points), a position, orientation, this function transforms the
   * 2D vectors into the object's world space.
   *
   * @param points   a list of 2D vectors
   * @param position the position
   * @param forward  the forward vector
   * @param side     the side vector
   * @return a list of vectors in world space
   */
  public static List<Vector2> pointsToWorldSpace(List<Vector2> points, Vector2 position,
                                                 Vector2 forward,
                                                 Vector2 side) {
    // copy the original vertices into the buffer about to be transformed
    List<Vector2> tranVector2Ds = clone(points);

    // create a transformation matrix
    Matrix3 matrix = Matrix3.newInstance();

    // rotate
    matrix.rotate(forward, side);

    // and translate
    matrix.translate(position.getX(), position.getY());

    // now transform the object's vertices
    matrix.transformVector2Ds(tranVector2Ds);

    return tranVector2Ds;
  }

  /**
   * Transforms a point from the agent's local space into world space.
   *
   * @param point         the source point
   * @param agentHeading  the agent heading vector
   * @param agentSide     the agent side vector
   * @param agentPosition the agent position
   * @return the new vector in the world space
   */
  public static Vector2 pointToWorldSpace(Vector2 point, Vector2 agentHeading, Vector2 agentSide,
                                          Vector2 agentPosition) {
    // Make a copy of the point
    Vector2 transPoint = new Vector2(point);

    // Create a transformation matrix
    float[] matrix = new float[9];
    matrix[0] = agentHeading.getX();
    matrix[1] = agentSide.getX();
    matrix[2] = agentPosition.getX();
    matrix[3] = agentHeading.getY();
    matrix[4] = agentSide.getY();
    matrix[5] = agentPosition.getY();
    matrix[6] = 0;
    matrix[7] = 0;
    matrix[8] = 1;

    // Transform the point
    float tempX = (matrix[0] * transPoint.getX()) + (matrix[1] * transPoint.getY()) + matrix[2];
    float tempY = (matrix[3] * transPoint.getX()) + (matrix[4] * transPoint.getY()) + matrix[5];

    transPoint.setX(tempX);
    transPoint.setY(tempY);

    return transPoint;
  }

  /**
   * Transforms a vector from the agent's local space into world space.
   *
   * @param vector       the source point
   * @param agentHeading the agent heading vector
   * @param agentSide    the agent side vector
   * @return the new vector in the world space
   */
  public static Vector2 vectorToWorldSpace(Vector2 vector, Vector2 agentHeading,
                                           Vector2 agentSide) {
    // Make a copy of the vector
    Vector2 transVec = new Vector2(vector);

    // Create a transformation matrix
    float[] matrix = new float[9];
    matrix[0] = agentHeading.getX();
    matrix[1] = agentSide.getX();
    matrix[2] = 0;
    matrix[3] = agentHeading.getY();
    matrix[4] = agentSide.getY();
    matrix[5] = 0;
    matrix[6] = 0;
    matrix[7] = 0;
    matrix[8] = 1;

    // Transform the vector
    float tempX = (matrix[0] * transVec.getX()) + (matrix[1] * transVec.getY());
    float tempY = (matrix[3] * transVec.getX()) + (matrix[4] * transVec.getY());

    transVec.setX(tempX);
    transVec.setY(tempY);

    return transVec;
  }

  /**
   * Transforms a point from the world space into agent local space.
   *
   * @param point         the source point
   * @param agentHeading  the agent heading vector
   * @param agentSide     the agent side vector
   * @param agentPosition the agent position
   * @return the new vector in the local space
   */
  public static Vector2 pointToLocalSpace(Vector2 point, Vector2 agentHeading, Vector2 agentSide,
                                          Vector2 agentPosition) {
    // Make a copy of the point
    Vector2 transPoint = new Vector2(point);

    // Create a transformation matrix
    float[] matrix = new float[9];
    matrix[0] = agentHeading.getX();
    matrix[1] = agentHeading.getY();
    matrix[2] = 0;
    matrix[3] = agentSide.getX();
    matrix[4] = agentSide.getY();
    matrix[5] = 0;
    matrix[6] = agentPosition.getX();
    matrix[7] = agentPosition.getY();
    matrix[8] = 1;

    // Transform the point
    float tempX = (matrix[0] * transPoint.getX()) + (matrix[3] * transPoint.getY()) + matrix[6];
    float tempY = (matrix[1] * transPoint.getX()) + (matrix[4] * transPoint.getY()) + matrix[7];

    transPoint.setX(tempX);
    transPoint.setY(tempY);

    return transPoint;
  }

  /**
   * Transforms a point from the world space into agent local space.
   *
   * @param vector       the source point
   * @param agentHeading the agent heading vector
   * @param agentSide    the agent side vector
   * @return the new vector in the local space
   */
  public static Vector2 vectorToLocalSpace(Vector2 vector, Vector2 agentHeading,
                                           Vector2 agentSide) {
    // Make a copy of the vector
    Vector2 transVec = new Vector2(vector);

    // Create a transformation matrix
    float[] matrix = new float[9];
    matrix[0] = agentHeading.getX();
    matrix[1] = agentHeading.getY();
    matrix[2] = 0;
    matrix[3] = agentSide.getX();
    matrix[4] = agentSide.getY();
    matrix[5] = 0;
    matrix[6] = 0;
    matrix[7] = 0;
    matrix[8] = 1;

    // Transform the vector
    float tempX = (matrix[0] * transVec.getX()) + (matrix[3] * transVec.getY());
    float tempY = (matrix[1] * transVec.getX()) + (matrix[4] * transVec.getY());

    transVec.setX(tempX);
    transVec.setY(tempY);

    return transVec;
  }

  /**
   * Rotates a vector angle radians around the origin.
   *
   * @param vector the vector
   * @param angle  the angle
   * @return a vector in new angle
   */
  public static Vector2 vec2dRotateAroundOrigin(Vector2 vector, float angle) {
    // make a copy of the point
    Vector2 temp = Vector2.newInstance().set(vector);

    // create a transformation matrix
    Matrix3 matrix = Matrix3.newInstance();

    // rotate
    matrix.rotate(angle);

    // now transform the object's vertices
    matrix.transformVector2D(temp);

    return temp;
  }

  /**
   * Rotates a vector angle radians around the origin.
   *
   * @param x     the vector x
   * @param y     the vector y
   * @param angle the angle
   * @return a vector in new angle
   */
  public static Vector2 vec2dRotateAroundOrigin(float x, float y, float angle) {
    // make a copy of the point
    Vector2 temp = Vector2.newInstance().set(x, y);

    // create a transformation matrix
    Matrix3 matrix = Matrix3.newInstance();

    // rotate
    matrix.rotate(angle);

    // now transform the object's vertices
    matrix.transformVector2D(temp);

    return temp;
  }

  /**
   * Given an origin, a facing direction, a 'field of view' describing the limit of the outer
   * whiskers, a whisker length and the number of whiskers this method returns a vector
   * containing the end positions of a series of whiskers radiating away from the origin and with
   * equal distance between them. (like the spokes of a wheel clipped to a specific segment size).
   *
   * @param numWhiskers   the number of whiskers
   * @param whiskerLength the whisker length
   * @param fov           the fov
   * @param facing        the facing
   * @param origin        the origin
   * @return a list of vector
   */
  public static List<Vector2> createWhiskers(int numWhiskers, float whiskerLength, float fov,
                                             Vector2 facing,
                                             Vector2 origin) {
    // this is the magnitude of the angle separating each whisker
    float sectorSize = fov / (float) (numWhiskers - 1);

    // create a vector that points away from the agent along the middle whisker
    List<Vector2> whiskers = new ArrayList<Vector2>(numWhiskers);

    float angle = -fov * 0.5f;

    for (int i = 0; i < numWhiskers; ++i) {
      Vector2 temp2 = Vector2.newInstance().set(facing);
      Vector2 temp = vec2dRotateAroundOrigin(temp2, angle);
      Vector2 temp1 = Vector2.newInstance().set(temp).mul(whiskerLength).add(origin);

      whiskers.add(temp1);

      angle += sectorSize;
    }

    return whiskers;
  }

  /**
   * Wrap around a vector.
   *
   * @param position the position
   * @param maxX     max x
   * @param maxY     max y
   * @return a new vector
   */
  public static Vector2 wrapAround(Vector2 position, int maxX, int maxY) {

    boolean clone = false;

    if (position.getX() > maxX) {
      position.setX(0);
      clone = true;
    }

    if (position.getX() < 0) {
      position.setX(maxX);
      clone = true;
    }

    if (position.getY() < 0) {
      position.setY(maxY);
      clone = true;
    }

    if (position.getY() > maxY) {
      position.setY(0);
      clone = true;
    }

    if (clone) {
      return Vector2.newInstance().set(position);
    }

    return position;
  }

  private static List<Vector2> clone(List<Vector2> list) {
    return list.stream().map(Vector2::clone).collect(Collectors.toList());
  }
}
