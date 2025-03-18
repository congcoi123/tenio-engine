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

package com.tenio.engine.physic2d.common;

import com.tenio.engine.physic2d.math.Matrix3;
import com.tenio.engine.physic2d.math.Vector2;

/**
 * A base class defining an entity that moves. The entity has a local coordinate
 * system <b>root(0, 0)</b> and members for defining its mass and velocity.
 */
public abstract class MoveableEntity extends BaseGameEntity {

  private Vector2 velocity;
  private Vector2 heading;
  private Vector2 side;
  // for entity's mass
  private float mass;
  // the maximum speed this entity may travel at
  private float maxSpeed;
  // the maximum force this entity can produce to power itself
  // (think rockets and thrust)
  private float maxForce;
  // the maximum rate (radians per second) this vehicle can rotate
  private float maxTurnRate;
  // current rotation
  private float rotation;

  /**
   * Creates a new moveable entity with the specified parameters.
   *
   * @param position    the initial position vector
   * @param radius     the bounding radius
   * @param velocity   the initial velocity vector
   * @param maxSpeed   the maximum speed
   * @param heading    the initial heading vector
   * @param mass       the entity's mass
   * @param scale      the scale vector
   * @param maxTurnRate the maximum turn rate
   * @param maxForce   the maximum force that can be applied
   */
  public MoveableEntity(Vector2 position, float radius, Vector2 velocity, float maxSpeed,
                        Vector2 heading, float mass,
                        Vector2 scale, float maxTurnRate, float maxForce) {
    super(0, position.getX(), position.getY(), radius);

    this.velocity = new Vector2();
    this.heading = new Vector2();
    this.side = new Vector2();

    setVelocity(velocity);
    setScale(scale.getX(), scale.getY());
    setHeading(heading);
    this.mass = mass;
    this.maxSpeed = maxSpeed;
    this.maxTurnRate = maxTurnRate;
    this.maxForce = maxForce;
  }

  /**
   * Creates a new moveable entity with the specified ID.
   *
   * @param id the entity's unique identifier
   */
  public MoveableEntity(int id) {
    super(id);
    mass = 0.0f;
    maxSpeed = 0.0f;
    velocity = new Vector2();
    heading = new Vector2(1.0f, 0.0f);
    side = new Vector2(0.0f, 1.0f);
  }

  public Vector2 getVelocity() {
    return velocity;
  }

  public void setVelocity(Vector2 velocity) {
    setVelocity(velocity.getX(), velocity.getY());
  }

  public void setVelocity(float x, float y) {
    velocity.setX(x);
    velocity.setY(y);
  }

  public Vector2 getHeading() {
    return heading;
  }

  public void setHeading(Vector2 heading) {
    setHeading(heading.getX(), heading.getY());
  }

  /**
   * First checks that the given heading is not a vector of zero length. If the
   * new heading is valid this function sets the entity's heading and side vectors
   * accordingly
   *
   * @param x the new heading in X
   * @param y the new heading in Y
   */
  public void setHeading(float x, float y) {
    heading.setX(x);
    heading.setY(y);
    // the side vector must always be perpendicular to the heading
    Vector2 temp = Vector2.newInstance().set(x, y).perpendicular();
    side.setX(temp.getX());
    side.setY(temp.getY());
    // update the rotation
    float angle = (float) Math.atan2(y, x);
    float degrees = (float) (180 * angle / Math.PI);
    degrees = (360 + Math.round(degrees)) % 360;
    rotation = degrees;
  }

  public float getSideX() {
    return side.getX();
  }

  public float getSideY() {
    return side.getY();
  }

  public Vector2 getSide() {
    return side;
  }

  private void setSide(Vector2 side) {
    this.side.setX(side.getX());
    this.side.setY(side.getY());
  }

  public float getRotation() {
    return rotation;
  }

  public float getMass() {
    return mass;
  }

  public float getMaxSpeed() {
    return maxSpeed;
  }

  public void setMaxSpeed(float maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  public float getMaxForce() {
    return maxForce;
  }

  public void setMaxForce(float maxForce) {
    this.maxForce = maxForce;
  }

  public boolean isSpeedMaxedOut() {
    return maxSpeed * maxSpeed >= getVelocity().getLengthSqr();
  }

  public float getSpeed() {
    return getVelocity().getLength();
  }

  public float getSpeedSqr() {
    return getVelocity().getLengthSqr();
  }

  public float getMaxTurnRate() {
    return maxTurnRate;
  }

  public void setMaxTurnRate(float maxTurnRate) {
    this.maxTurnRate = maxTurnRate;
  }

  /**
   * Given a target position, this method rotates the entity's heading and side
   * vectors by an amount not greater than m_dMaxTurnRate until it directly faces
   * the target.
   *
   * @param target the new target vector
   * @return <b>true</b> when the heading is facing in the desired direction
   */
  public boolean isRotatedHeadingToFacePosition(Vector2 target) {
    // get direction between 2 vectors
    Vector2 temp = Vector2.newInstance().set(target).sub(getPosition()).normalize();

    // first determine the angle between the heading vector and the target
    float angle = (float) Math.acos(getHeading().getDotProductValue(temp));
    if (Float.isNaN(angle)) {
      angle = 0;
    }

    // return true if the player is facing the target
    if (angle < 0.00001) {
      return true;
    }

    // clamp the amount to turn to the max turn rate
    if (angle > maxTurnRate) {
      angle = maxTurnRate;
    }

    // The next few lines use a rotation matrix to rotate the player's heading
    // vector accordingly
    Matrix3 matrix3 = Matrix3.newInstance();

    // notice how the direction of rotation has to be determined when creating
    // the rotation matrix
    matrix3.rotate(angle * getHeading().getSignValue(temp));
    matrix3.transformVector2D(getHeading());
    matrix3.transformVector2D(getVelocity());

    // finally, recreate m_vSide
    setSide(getHeading().perpendicular());

    return false;
  }

  public void update(float deltaTime) {
    // Update the position
    Vector2 pos = getPosition();
    Vector2 vel = getVelocity();
    pos.set(pos.getX() + vel.getX() * deltaTime, pos.getY() + vel.getY() * deltaTime);
    setPosition(pos);

    // Update the heading if the velocity is non-zero
    float velocityLengthSqr = getSpeedSqr();
    if (velocityLengthSqr > 0.00001f) {
      setHeading(vel.getX(), vel.getY());
      // Update side vector to be perpendicular to heading
      Vector2 head = getHeading();
      Vector2 perpendicular = Vector2.newInstance().set(-head.getY(), head.getX());
      setSide(perpendicular);
    }
  }
}
