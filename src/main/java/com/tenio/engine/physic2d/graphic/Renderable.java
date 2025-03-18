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

/**
 * An interface that defines objects that can be rendered to a screen.
 * Classes implementing this interface must provide a method to render themselves
 * using a {@link Paint} instance.
 * 
 * <p>This interface is part of the graphics system and is typically implemented by
 * game objects, UI elements, or any other visual components that need to be drawn
 * to the screen.
 * 
 * <p>Example implementation:
 * <pre>
 * public class GameObject implements Renderable {
 *     public void render(Paint paint) {
 *         paint.setPenColor(Color.RED);
 *         paint.drawCircle(x, y, radius);
 *     }
 * }
 * </pre>
 * 
 * @see Paint
 * @since 0.1.0
 */
public interface Renderable {

  /**
   * Renders this object to the screen using the provided Paint instance.
   * Implementing classes should use the Paint methods to perform their
   * specific rendering operations.
   * 
   * <p>The rendering context (graphics, colors, transformations) is managed by
   * the Paint instance. Implementations should not make assumptions about the
   * state of the Paint instance and should set any required properties
   * (colors, fonts, etc.) before drawing.
   *
   * @param paint the Paint instance to use for rendering operations
   * @see Paint#startDrawing(java.awt.Graphics)
   */
  void render(Paint paint);
}
