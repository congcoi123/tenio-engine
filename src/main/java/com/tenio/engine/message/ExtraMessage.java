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

package com.tenio.engine.message;

/**
 * The ExtraMessage interface represents additional data that can be attached to messages
 * sent between entities in the game engine. This class provides a flexible way to
 * include various types of data with messages, such as game state information,
 * entity properties, or action parameters.
 *
 * <p>
 * Features:
 * - Generic data storage
 * - Type-safe data retrieval
 * - Support for multiple data types
 * - Null-safe operations
 * </p>
 *
 * <p>
 * The class supports:
 * - Custom data payloads
 * - Message-specific information
 * - Entity state data
 * - Action parameters
 * </p>
 *
 * <p>
 * Example usage:
 * {@code
 * // Create a message with damage information
 * ExtraMessage attackData = new ExtraMessage();
 * attackData.setValue("damage", 50);
 * attackData.setValue("type", "fire");
 * 
 * // Create a message with position data
 * ExtraMessage moveData = new ExtraMessage();
 * moveData.setValue("x", 100.0f);
 * moveData.setValue("y", 200.0f);
 * moveData.setValue("speed", 5.0f);
 * 
 * // Retrieve data
 * int damage = attackData.getValue("damage", Integer.class);
 * String type = attackData.getValue("type", String.class);
 * float x = moveData.getValue("x", Float.class);
 * }
 * </p>
 *
 * @see com.tenio.engine.message.MessageDispatcher
 * @see com.tenio.engine.fsm.entity.Telegram
 * @since 0.5.0
 */
public interface ExtraMessage {

  /**
   * Sets a value in the message.
   *
   * @param key   the key to store the value under
   * @param value the value to store
   */
  void setValue(String key, Object value);

  /**
   * Retrieves a value from the message.
   *
   * @param <T>   the type of value to retrieve
   * @param key   the key of the value to retrieve
   * @param clazz the class of the value type
   * @return the value if found and of the correct type, null otherwise
   */
  <T> T getValue(String key, Class<T> clazz);

  /**
   * Checks if a key exists in the message.
   *
   * @param key the key to check
   * @return true if the key exists, false otherwise
   */
  boolean containsKey(String key);

  /**
   * Removes a value from the message.
   *
   * @param key the key of the value to remove
   */
  void removeValue(String key);

  /**
   * Clears all values from the message.
   */
  void clear();

}
