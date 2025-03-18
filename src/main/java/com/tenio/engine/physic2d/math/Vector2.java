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

package com.tenio.engine.physic2d.math;

import com.tenio.common.utility.MathUtility;

/**
 * A 2D vector class for representing coordinates and directions in 2D space.
 * This class provides comprehensive functionality for vector operations including:
 * - Basic arithmetic operations (addition, subtraction, multiplication, division)
 * - Vector normalization
 * - Dot and cross products
 * - Vector rotation
 * - Distance and angle calculations
 * - Vector reflection and truncation
 * <p>
 * The class is designed to be immutable - operations return new Vector2 instances
 * rather than modifying existing ones, ensuring thread-safety and preventing
 * unintended side effects.
 * <p>
 * Example usage:
 * <pre>
 * Vector2 v1 = new Vector2(1.0f, 2.0f);
 * Vector2 v2 = new Vector2(3.0f, 4.0f);
 * Vector2 sum = v1.add(v2);
 * float distance = v1.distance(v2);
 * </pre>
 *
 * @see Matrix3
 * @since 0.5.0
 */
public final class Vector2 implements Cloneable {

  /**
   * Constant indicating clockwise rotation direction.
   */
  public static final int CLOCK_WISE = 1;

  /**
   * Constant indicating counter-clockwise rotation direction.
   */
  public static final int ANTI_CLOCK_WISE = -1;

  /**
   * The x-coordinate of this vector.
   */
  private float x;

  /**
   * The y-coordinate of this vector.
   */
  private float y;

  /**
   * Creates a new vector with coordinates (0,0).
   */
  public Vector2() {
    zero();
  }

  /**
   * Creates a new vector with the specified coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public Vector2(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Creates a new vector as a copy of another vector.
   *
   * @param other the vector to copy
   */
  public Vector2(Vector2 other) {
    this.x = other.x;
    this.y = other.y;
  }

  /**
   * Factory method to create a new vector instance.
   *
   * @return a new vector with coordinates (0,0)
   */
  public static Vector2 newInstance() {
    return new Vector2();
  }

  /**
   * Sets the coordinates of this vector.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return this vector for chaining
   */
  public Vector2 set(float x, float y) {
    this.x = x;
    this.y = y;
    return this;
  }

  /**
   * Sets this vector's coordinates to match another vector.
   *
   * @param other the vector to copy from
   * @return this vector for chaining
   */
  public Vector2 set(Vector2 other) {
    this.x = other.x;
    this.y = other.y;
    return this;
  }

  /**
   * Adds the given values to this vector's coordinates.
   *
   * @param a value to add to x
   * @param b value to add to y
   * @return this vector for chaining
   */
  public Vector2 add(float a, float b) {
    x += a;
    y += b;
    return this;
  }

  /**
   * Subtracts the given values from this vector's coordinates.
   *
   * @param a value to subtract from x
   * @param b value to subtract from y
   * @return this vector for chaining
   */
  public Vector2 sub(float a, float b) {
    x -= a;
    y -= b;
    return this;
  }

  /**
   * Adds another vector to this vector.
   *
   * @param other the vector to add
   * @return this vector for chaining
   */
  public Vector2 add(Vector2 other) {
    x += other.x;
    y += other.y;
    return this;
  }

  /**
   * Subtracts another vector from this vector.
   *
   * @param other the vector to subtract
   * @return this vector for chaining
   */
  public Vector2 sub(Vector2 other) {
    x -= other.x;
    y -= other.y;
    return this;
  }

  /**
   * Multiplies this vector by a scalar value.
   *
   * @param scalar the scalar value
   * @return this vector for chaining
   */
  public Vector2 mul(float scalar) {
    x *= scalar;
    y *= scalar;
    return this;
  }

  /**
   * Divides this vector by a scalar value.
   *
   * @param scalar the scalar value
   * @return this vector for chaining
   */
  public Vector2 div(float scalar) {
    x /= scalar;
    y /= scalar;
    return this;
  }

  /**
   * Calculates the squared distance between this vector and another vector.
   *
   * @param other the other vector
   * @return the squared distance between the vectors
   */
  public float distanceSq(Vector2 other) {
    float dx = x - other.x;
    float dy = y - other.y;
    return dx * dx + dy * dy;
  }

  /**
   * Calculates the distance between this vector and another vector.
   *
   * @param other the other vector
   * @return the distance between the vectors
   */
  public float distance(Vector2 other) {
    return (float) Math.sqrt(distanceSq(other));
  }

  /**
   * Normalizes this vector (makes it unit length).
   *
   * @return this vector for chaining
   */
  public Vector2 normalize() {
    float length = length();
    if (length != 0) {
      x /= length;
      y /= length;
    }
    return this;
  }

  /**
   * Calculates the length (magnitude) of this vector.
   *
   * @return the vector's length
   */
  public float length() {
    return (float) Math.sqrt(x * x + y * y);
  }

  /**
   * Returns the perpendicular vector to this vector.
   *
   * @return a new vector perpendicular to this one
   */
  public Vector2 perpendicular() {
    return new Vector2(-y, x);
  }

  /**
   * @return the vector ({@link Vector2}) that is perpendicular to this one. At an
   * angle of 90° to a given line, plane, or surface or to the ground.
   */
  public Vector2 perpendicularToGround() {
    // swap
    float temp = x;
    x = -y;
    y = temp;

    return this;
  }

  /**
   * Adjusts x and y so that the length of the vector does not exceed max
   * truncates a vector so that its length does not exceed max
   *
   * @param max the max value
   * @return a new truncated vector, see {@link Vector2}
   */
  public Vector2 truncate(float max) {
    if (length() > max) {
      normalize().mul(max);
    }

    return this;
  }

  /**
   * Calculates the Euclidean distance between two vectors
   *
   * @param vector see {@link Vector2}
   * @return the distance between this vector and the one passed as a parameter
   */
  public float getDistanceValue(Vector2 vector) {
    return (float) Math.sqrt(getDistanceSqrValue(vector));
  }

  /**
   * Squared version of distance: Calculates the Euclidean distance squared
   * between two vectors
   *
   * @param vector see {@link Vector2}
   * @return the distance sqr between this vector and the one passed as a
   * parameter
   */
  public float getDistanceSqrValue(Vector2 vector) {
    float ySeparation = vector.y - y;
    float xSeparation = vector.x - x;

    return ySeparation * ySeparation + xSeparation * xSeparation;
  }

  /**
   * @return the new vector that is the reverse of this vector, see
   * {@link Vector2}
   */
  public Vector2 reverse() {
    x *= -1;
    y *= -1;

    return this;
  }

  // ----------------------- Overloaded Operators -----------------------
  // --------------------------------------------------------------------
  public boolean isEqual(Vector2 vector) {
    return (MathUtility.isEqual(x, vector.x) && MathUtility.isEqual(y, vector.y));
  }

  @Override
  public String toString() {
    return String.format("Vector2(x=%.2f, y=%.2f)", x, y);
  }

  /**
   * Calculates the squared length (magnitude) of this vector.
   *
   * @return the vector's squared length
   */
  public float getLengthSqr() {
    return x * x + y * y;
  }

  /**
   * Calculates the dot product of this vector with another vector.
   *
   * @param v2 the other vector
   * @return the dot product value
   */
  public float getDotProductValue(Vector2 v2) {
    return (x * v2.x + y * v2.y);
  }

  /**
   * Returns the sign of the cross product between this vector and another vector.
   * 
   * @param v2 the other vector
   * @return 1 if counter-clockwise, -1 if clockwise
   */
  public int getSignValue(Vector2 v2) {
    return (y * v2.x > x * v2.y) ? 1 : -1;
  }

  /**
   * Creates a copy of this vector.
   *
   * @return a new vector with the same coordinates
   */
  @Override
  public Vector2 clone() {
    return new Vector2(x, y);
  }

  /**
   * Creates a new vector from an existing one.
   *
   * @param v the vector to copy
   * @return a new vector with the same coordinates
   */
  public static Vector2 valueOf(Vector2 v) {
    return new Vector2(v.x, v.y);
  }

  /**
   * Sets this vector to zero (0,0).
   *
   * @return this vector for chaining
   */
  public Vector2 zero() {
    x = 0.0f;
    y = 0.0f;
    return this;
  }

  /**
   * Gets the x-coordinate of this vector.
   *
   * @return the x coordinate
   */
  public float getX() {
    return x;
  }

  /**
   * Sets the x-coordinate of this vector.
   *
   * @param x the x-coordinate to set
   */
  public void setX(float x) {
    this.x = x;
  }

  /**
   * Gets the y-coordinate of this vector.
   *
   * @return the y coordinate
   */
  public float getY() {
    return y;
  }

  /**
   * Sets the y-coordinate of this vector.
   *
   * @param y the y-coordinate to set
   */
  public void setY(float y) {
    this.y = y;
  }

  public float getLength() {
    return (float) Math.sqrt(getLengthSqr());
  }

  public int getSignValue() {
    return (y * x > x * y) ? 1 : -1;
  }

  /**
   * Checks if this vector has zero magnitude (both x and y are 0).
   *
   * @return true if the vector is zero, false otherwise
   */
  public boolean isZero() {
    return x == 0 && y == 0;
  }

  /**
   * Creates a new vector with the specified coordinates.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @return a new Vector2 instance
   */
  public static Vector2 valueOf(float x, float y) {
    return new Vector2(x, y);
  }
}
