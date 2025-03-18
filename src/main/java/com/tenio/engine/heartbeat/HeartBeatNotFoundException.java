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

package com.tenio.engine.heartbeat;

/**
 * The HeartBeatNotFoundException is thrown when attempting to access or manipulate
 * a heartbeat that does not exist in the HeartBeatManager. This exception helps
 * identify and handle cases where a requested heartbeat component cannot be found.
 * <p>
 * Features:
 * - Detailed error messages
 * - Heartbeat identification
 * - Exception chaining
 * - Error tracking
 * <p>
 * The exception supports:
 * - Custom error messages
 * - Root cause tracking
 * - Error recovery
 * - Debugging assistance
 * <p>
 * Example usage:
 * <pre>
 * try {
 *     HeartBeatManager manager = HeartBeatManager.getInstance();
 *     AbstractHeartBeat heartBeat = manager.getHeartBeat("nonexistent");
 *     heartBeat.start();
 * } catch (HeartBeatNotFoundException e) {
 *     // Handle missing heartbeat
 *     logger.error("Failed to start heartbeat: {}", e.getMessage());
 *     // Create and register missing heartbeat
 *     manager.registerHeartBeat("nonexistent", new DefaultHeartBeat());
 * }
 * </pre>
 *
 * @see HeartBeatManager
 * @see AbstractHeartBeat
 * @since 0.5.0
 */
public final class HeartBeatNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -5550084534331675241L;

  /**
   * Creates a new HeartBeatNotFoundException with the specified message.
   *
   * @param message the detail message
   */
  public HeartBeatNotFoundException(String message) {
    super(message);
  }

  /**
   * Creates a new HeartBeatNotFoundException with the specified message and cause.
   *
   * @param message the detail message
   * @param cause   the cause of this exception
   */
  public HeartBeatNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

} 