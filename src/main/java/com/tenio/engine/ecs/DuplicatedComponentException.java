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

package com.tenio.engine.ecs;

/**
 * Thrown when attempting to add a duplicate component to an entity.
 *
 * <p>This exception is thrown when an attempt is made to add a component to an entity that already has a
 * component of the same type. Each entity can only have one instance of each component type.
 *
 * <p>The exception provides information about the attempted duplicate component addition and helps maintain
 * the integrity of the entity-component system.
 *
 * <p>This class extends {@link RuntimeException} to indicate that this is an unchecked exception that can
 * occur during normal operation of the ECS.
 *
 * @see com.tenio.engine.ecs.basis.Entity
 * @see com.tenio.engine.ecs.basis.Component
 * @since 0.5.0
 */
public final class DuplicatedComponentException extends RuntimeException {

  private static final long serialVersionUID = -5550084534331675241L;

  /**
   * Creates a new DuplicatedComponentException with the specified message.
   *
   * @param message the detail message
   */
  public DuplicatedComponentException(String message) {
    super(message);
  }

  /**
   * Creates a new DuplicatedComponentException with the specified message and cause.
   *
   * @param message the detail message
   * @param cause   the cause of this exception
   */
  public DuplicatedComponentException(String message, Throwable cause) {
    super(message, cause);
  }

} 