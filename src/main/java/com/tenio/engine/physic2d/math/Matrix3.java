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

import java.util.List;

/**
 * A 3x3 matrix class designed for 2D transformations and coordinate space operations.
 * This class provides functionality for common matrix operations including:
 * - Matrix multiplication
 * - Transformation operations (translation, rotation, scaling)
 * - Identity matrix creation
 * - Matrix transposition
 * - Determinant calculation
 * <p>
 * The matrix is stored in row-major order and can be used for:
 * - 2D affine transformations
 * - Coordinate space conversions
 * - Composite transformations through matrix multiplication
 * <p>
 * Example usage:
 * <pre>
 * Matrix3 transform = Matrix3.identity();
 * transform = transform.translate(2.0f, 3.0f);
 * transform = transform.rotate(45.0f);
 * Vector2 transformed = transform.transformVector(new Vector2(1.0f, 1.0f));
 * </pre>
 *
 * @see Vector2
 * @since 0.5.0
 */
public final class Matrix3 {

  /**
   * The main matrix storage for transformation operations.
   */
  private final Matrix matrix = new Matrix();

  /**
   * Temporary matrix used for intermediate calculations.
   */
  private final Matrix tempMatrix = new Matrix();

  /**
   * Private constructor to enforce factory method usage.
   * Initializes the matrix to an identity matrix.
   */
  private Matrix3() {
    initialize();
  }

  /**
   * Creates a new Matrix3 instance initialized as an identity matrix.
   *
   * @return a new Matrix3 instance
   */
  public static Matrix3 newInstance() {
    return new Matrix3();
  }

  /**
   * Initializes this matrix to an identity matrix.
   * The identity matrix has ones on the main diagonal and zeros elsewhere:
   * <pre>
   * | 1 0 0 |
   * | 0 1 0 |
   * | 0 0 1 |
   * </pre>
   */
  public void initialize() {
    matrix.p11 = 1;
    matrix.p12 = 0;
    matrix.p13 = 0;
    matrix.p21 = 0;
    matrix.p22 = 1;
    matrix.p23 = 0;
    matrix.p31 = 0;
    matrix.p32 = 0;
    matrix.p33 = 1;
  }

  /**
   * Sets the value at position (1,1) in the matrix.
   *
   * @param val the value to set
   */
  public void p11(float val) {
    matrix.p11 = val;
  }

  /**
   * Sets the value at position (1,2) in the matrix.
   *
   * @param val the value to set
   */
  public void p12(float val) {
    matrix.p12 = val;
  }

  /**
   * Sets the value at position (1,3) in the matrix.
   *
   * @param val the value to set
   */
  public void p13(float val) {
    matrix.p13 = val;
  }

  /**
   * Sets the value at position (2,1) in the matrix.
   *
   * @param val the value to set
   */
  public void p21(float val) {
    matrix.p21 = val;
  }

  /**
   * Sets the value at position (2,2) in the matrix.
   *
   * @param val the value to set
   */
  public void p22(float val) {
    matrix.p22 = val;
  }

  /**
   * Sets the value at position (2,3) in the matrix.
   *
   * @param val the value to set
   */
  public void p23(float val) {
    matrix.p23 = val;
  }

  /**
   * Sets the value at position (3,1) in the matrix.
   *
   * @param val the value to set
   */
  public void p31(float val) {
    matrix.p31 = val;
  }

  /**
   * Sets the value at position (3,2) in the matrix.
   *
   * @param val the value to set
   */
  public void p32(float val) {
    matrix.p32 = val;
  }

  /**
   * Sets the value at position (3,3) in the matrix.
   *
   * @param val the value to set
   */
  public void p33(float val) {
    matrix.p33 = val;
  }

  /**
   * Multiplies this matrix by another matrix using row-major multiplication.
   * The result is stored in this matrix.
   *
   * @param matrix the matrix to multiply with
   */
  private void mul(final Matrix matrix) {
    // first
    float p11 =
        (this.matrix.p11 * matrix.p11)
            + (this.matrix.p12 * matrix.p21) + (this.matrix.p13 * matrix.p31);
    float p12 =
        (this.matrix.p11 * matrix.p12) + (this.matrix.p12 * matrix.p22)
            + (this.matrix.p13 * matrix.p32);
    float p13 =
        (this.matrix.p11 * matrix.p13) + (this.matrix.p12 * matrix.p23)
            + (this.matrix.p13 * matrix.p33);

    // second
    float p21 =
        (this.matrix.p21 * matrix.p11) + (this.matrix.p22 * matrix.p21)
            + (this.matrix.p23 * matrix.p31);
    float p22 =
        (this.matrix.p21 * matrix.p12) + (this.matrix.p22 * matrix.p22)
            + (this.matrix.p23 * matrix.p32);
    float p23 =
        (this.matrix.p21 * matrix.p13) + (this.matrix.p22 * matrix.p23)
            + (this.matrix.p23 * matrix.p33);

    // third
    float p31 =
        (this.matrix.p31 * matrix.p11) + (this.matrix.p32 * matrix.p21)
            + (this.matrix.p33 * matrix.p31);
    float p32 =
        (this.matrix.p31 * matrix.p12) + (this.matrix.p32 * matrix.p22)
            + (this.matrix.p33 * matrix.p32);
    float p33 =
        (this.matrix.p31 * matrix.p13) + (this.matrix.p32 * matrix.p23)
            + (this.matrix.p33 * matrix.p33);

    this.matrix.set(p11, p12, p13, p21, p22, p23, p31, p32, p33);
  }

  /**
   * Applies this transformation matrix to a list of 2D vectors.
   * Each vector in the list is transformed in place.
   *
   * @param points the list of vectors to transform
   */
  public void transformVector2Ds(List<Vector2> points) {
    points.forEach(vector -> {
      float tempX = (matrix.p11 * vector.getX()) + (matrix.p21 * vector.getY()) + (matrix.p31);
      float tempY = (matrix.p12 * vector.getX()) + (matrix.p22 * vector.getY()) + (matrix.p32);
      vector.setX(tempX);
      vector.setY(tempY);
    });
  }

  /**
   * Applies this transformation matrix to a single 2D vector.
   * The vector is transformed in place.
   *
   * @param point the vector to transform
   */
  public void transformVector2D(Vector2 point) {
    float tempX = (matrix.p11 * point.getX()) + (matrix.p21 * point.getY()) + (matrix.p31);
    float tempY = (matrix.p12 * point.getX()) + (matrix.p22 * point.getY()) + (matrix.p32);

    point.setX(tempX);
    point.setY(tempY);
  }

  /**
   * Creates and applies a translation transformation.
   * This will move points by the specified x and y distances.
   *
   * @param x the distance to move along the x-axis
   * @param y the distance to move along the y-axis
   */
  public void translate(float x, float y) {
    tempMatrix.initialize();
    tempMatrix.set(1, 0, 0, 0, 1, 0, x, y, 1);
    mul(tempMatrix);
  }

  /**
   * Creates and applies a scaling transformation.
   * This will scale points by the specified factors in x and y directions.
   *
   * @param xscale the scaling factor for the x-axis
   * @param yscale the scaling factor for the y-axis
   */
  public void scale(float xscale, float yscale) {
    tempMatrix.initialize();
    tempMatrix.set(xscale, 0, 0, 0, yscale, 0, 0, 0, 1);
    mul(tempMatrix);
  }

  /**
   * Creates and applies a rotation transformation.
   * This will rotate points by the specified angle in radians.
   *
   * @param rotation the rotation angle in radians
   */
  public void rotate(float rotation) {
    float sin = (float) Math.sin(rotation);
    float cos = (float) Math.cos(rotation);

    tempMatrix.initialize();
    tempMatrix.set(cos, -sin, 0, sin, cos, 0, 0, 0, 1);
    mul(tempMatrix);
  }

  /**
   * Creates and applies a rotation transformation based on forward and side vectors.
   * This is useful for orienting objects in 2D space.
   *
   * @param forward the forward direction vector
   * @param side the side direction vector
   */
  public void rotate(Vector2 forward, Vector2 side) {
    tempMatrix.initialize();
    tempMatrix.set(forward.getX(), -side.getX(), 0, forward.getY(), side.getY(), 0, 0, 0, 1);
    mul(tempMatrix);
  }

  private class Matrix {

    public float p11;
    public float p12;
    public float p13;
    public float p21;
    public float p22;
    public float p23;
    public float p31;
    public float p32;
    public float p33;

    public Matrix() {
      initialize();
    }

    public void initialize() {
      p11 = 0;
      p12 = 0;
      p13 = 0;
      p21 = 0;
      p22 = 0;
      p23 = 0;
      p31 = 0;
      p32 = 0;
      p33 = 0;
    }

    public void set(float p11, float p12, float p13, float p21, float p22, float p23, float p31,
                    float p32,
                    float p33) {
      this.p11 = p11;
      this.p12 = p12;
      this.p13 = p13;
      this.p21 = p21;
      this.p22 = p22;
      this.p23 = p23;
      this.p31 = p31;
      this.p32 = p32;
      this.p33 = p33;
    }
  }
}
