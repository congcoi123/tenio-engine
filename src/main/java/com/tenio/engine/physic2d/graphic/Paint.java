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

import com.tenio.engine.physic2d.math.Vector2;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.List;
import java.util.Objects;

/**
 * A singleton class that provides methods for rendering graphics to a screen.
 * This class wraps Java AWT graphics operations and provides convenient methods for
 * drawing text, shapes, lines, and other graphical elements.
 * 
 * <p>Features include:
 * <ul>
 *   <li>Text rendering with optional background</li>
 *   <li>Basic shape drawing (dots, lines, crosses)</li>
 *   <li>Polygon rendering</li>
 *   <li>Arrow drawing</li>
 *   <li>Color management for pen, background, and text</li>
 * </ul>
 * 
 * <p>Usage example:
 * <pre>
 * Paint paint = Paint.getInstance();
 * paint.startDrawing(graphics);
 * paint.setPenColor(Color.RED);
 * paint.drawLine(0, 0, 100, 100);
 * </pre>
 * 
 * @since 0.1.0
 */
public final class Paint {

  /**
   * The singleton instance of the Paint class.
   */
  private static final Paint instance = new Paint();

  /**
   * Temporary vectors used for calculations in various drawing operations.
   */
  private final Vector2 temp1 = Vector2.newInstance();
  private final Vector2 temp2 = Vector2.newInstance();
  private final Vector2 temp3 = Vector2.newInstance();
  private final Vector2 temp4 = Vector2.newInstance();

  /**
   * Polygon instance used for shape rendering operations.
   */
  private final Polygon polygon = new Polygon();

  /**
   * Background color for text rendering.
   */
  private final Color bgTextColor;

  /**
   * The current graphics context for drawing operations.
   */
  private Graphics brush;

  /**
   * The current pen color used for drawing operations.
   */
  private Color penColor;

  /**
   * The current background color used for filling operations.
   */
  private Color bgColor;

  /**
   * Flag indicating whether text should be drawn with an opaque background.
   */
  private boolean bgTextOpaque;

  /**
   * The current color used for text rendering.
   */
  private Color textColor;

  /**
   * Private constructor to enforce singleton pattern.
   * Initializes default colors and states.
   * 
   * @throws UnsupportedOperationException if attempting to create multiple instances
   */
  private Paint() {
    if (Objects.nonNull(instance)) {
      throw new UnsupportedOperationException("Could not recreate this instance");
    }

    brush = null;
    penColor = Color.BLACK;
    bgColor = null;
    bgTextOpaque = false;
    textColor = Color.BLACK;
    bgTextColor = Color.WHITE;
  }

  /**
   * Returns the singleton instance of the Paint class.
   * This method is thread-safe as the instance is created during class initialization.
   *
   * @return the singleton Paint instance
   */
  public static Paint getInstance() {
    return instance;
  }

  /**
   * Initializes drawing operations with the specified graphics context.
   * This method must be called before any drawing operations.
   *
   * @param graphic the graphics context to use for drawing
   */
  public void startDrawing(Graphics graphic) {
    brush = graphic;
  }

  // ------------------ Draw Text ------------------
  // -----------------------------------------------

  /**
   * Draws text at the specified position with current text settings.
   * If text background is enabled ({@link #enableOpaqueText}), draws a background
   * rectangle before rendering the text.
   *
   * @param x the x-coordinate where to draw the text
   * @param y the y-coordinate where to draw the text
   * @param text the text to draw
   */
  public void drawTextAtPosition(int x, int y, String text) {
    final Color back = brush.getColor();
    y += getFontHeight() - 2;
    if (bgTextOpaque) {
      FontMetrics fm = brush.getFontMetrics();
      brush.setColor(bgTextColor);
      brush.fillRect(x, y - fm.getAscent() + fm.getDescent(), fm.stringWidth(text), fm.getAscent());
    }
    brush.setColor(textColor);
    brush.drawString(text, x, y);
    brush.setColor(back);
  }

  /**
   * Draws text at the specified floating-point coordinates.
   * Coordinates are converted to integers before drawing.
   *
   * @param x the x-coordinate where to draw the text
   * @param y the y-coordinate where to draw the text
   * @param text the text to draw
   */
  public void drawTextAtPosition(float x, float y, String text) {
    drawTextAtPosition((int) x, (int) y, text);
  }

  /**
   * Draws text at the position specified by a Vector2.
   *
   * @param position the position where to draw the text
   * @param text the text to draw
   */
  public void drawTextAtPosition(Vector2 position, String text) {
    drawTextAtPosition((int) position.x, (int) position.y, text);
  }

  /**
   * Enables or disables opaque text background.
   * When enabled, text is drawn with a solid background color.
   *
   * @param enabled true to enable opaque text background, false to disable
   */
  public void enableOpaqueText(boolean enabled) {
    bgTextOpaque = enabled;
  }

  /**
   * Sets the color used for text rendering.
   *
   * @param color the color to use for text
   */
  public void setTextColor(Color color) {
    textColor = color;
  }

  /**
   * Sets the text color using RGB components.
   *
   * @param r the red component (0-255)
   * @param g the green component (0-255)
   * @param b the blue component (0-255)
   */
  public void setTextColor(int r, int g, int b) {
    textColor = new Color(r, g, b);
  }

  /**
   * Retrieves the height of font.
   *
   * @return the font's height
   */
  public int getFontHeight() {
    if (Objects.isNull(brush)) {
      return 0;
    }
    return brush.getFontMetrics().getHeight();
  }

  /**
   * Gets the width of the specified text string when rendered with the current font.
   *
   * @param text the text to measure
   * @return the width of the text in pixels
   */
  public int getTextWidth(String text) {
    return brush.getFontMetrics().stringWidth(text);
  }

  // ------------------ Draw Pixels ----------------
  // -----------------------------------------------

  /**
   * Draws a dot at the specified integer coordinates.
   *
   * @param x the x-coordinate of the dot
   * @param y the y-coordinate of the dot
   */
  public void drawDot(int x, int y) {
    brush.drawLine(x, y, x, y);
  }

  /**
   * Draws a dot at the specified floating-point coordinates.
   * Coordinates are converted to integers before drawing.
   *
   * @param x the x-coordinate of the dot
   * @param y the y-coordinate of the dot
   */
  public void drawDot(float x, float y) {
    drawDot((int) x, (int) y);
  }

  /**
   * Draws a dot at the position specified by a Vector2.
   *
   * @param position the position where to draw the dot
   */
  public void drawDot(Vector2 position) {
    drawDot((int) position.x, (int) position.y);
  }

  // ------------------ Draw Line ------------------
  // -----------------------------------------------

  /**
   * Draws a line between two points specified by integer coordinates.
   *
   * @param x1 the x-coordinate of the start point
   * @param y1 the y-coordinate of the start point
   * @param x2 the x-coordinate of the end point
   * @param y2 the y-coordinate of the end point
   */
  public void drawLine(int x1, int y1, int x2, int y2) {
    brush.drawLine(x1, y1, x2, y2);
  }

  /**
   * Draws a line between two points specified by floating-point coordinates.
   *
   * @param x1 the x-coordinate of the start point
   * @param y1 the y-coordinate of the start point
   * @param x2 the x-coordinate of the end point
   * @param y2 the y-coordinate of the end point
   */
  public void drawLine(float x1, float y1, float x2, float y2) {
    drawLine((int) x1, (int) y1, (int) x2, (int) y2);
  }

  /**
   * Draws a line between two points specified by Vector2 objects.
   *
   * @param from the start point
   * @param to the end point
   */
  public void drawLine(Vector2 from, Vector2 to) {
    drawLine((int) from.x, (int) from.y, (int) to.x, (int) to.y);
  }

  /**
   * Draw a polygon line.
   *
   * @param points the list of points
   */
  public void drawPolyLine(List<Vector2> points) {
    // make sure we have at least 2 points
    if (points.size() < 2) {
      return;
    }

    polygon.reset();

    for (Vector2 v : points) {
      polygon.addPoint((int) v.x, (int) v.y);
    }
    brush.setColor(penColor);
    brush.drawPolygon(polygon);
  }

  /**
   * Draw a line with an arrow on its head.
   *
   * @param from the start point
   * @param to   the target point
   * @param size the size
   */
  public void drawLineWithArrow(Vector2 from, Vector2 to, float size) {
    temp1.set(to).sub(from).normalize();
    Vector2 norm = temp1;

    // calculate where the arrow is attached
    temp2.set(norm).mul(size);
    temp3.set(to).sub(temp2);
    Vector2 crossingPoint = temp3;

    // calculate the two extra points required to make the arrowhead
    temp4.set(norm.perpendicular()).mul(0.4f * size).add(crossingPoint);
    final Vector2 arrowPoint1 = temp4;
    final Vector2 arrowPoint2 = temp4;

    // draw the line
    brush.setColor(penColor);
    brush.drawLine((int) from.x, (int) from.y, (int) crossingPoint.x, (int) crossingPoint.y);

    // draw the arrowhead (filled with the currently selected brush)
    polygon.reset();

    polygon.addPoint((int) arrowPoint1.x, (int) arrowPoint1.y);
    polygon.addPoint((int) arrowPoint2.x, (int) arrowPoint2.y);
    polygon.addPoint((int) to.x, (int) to.y);

    if (Objects.nonNull(bgColor)) {
      brush.setColor(bgColor);
      brush.fillPolygon(polygon);
    }
  }

  /**
   * Draws a cross (X) centered at the specified integer coordinates.
   * The cross size is determined by the CROSS_SIZE constant.
   *
   * @param x the x-coordinate of the cross center
   * @param y the y-coordinate of the cross center
   */
  public void drawCross(int x, int y) {
    final int CROSS_SIZE = 5;
    drawLine(x - CROSS_SIZE, y - CROSS_SIZE, x + CROSS_SIZE, y + CROSS_SIZE);
    drawLine(x - CROSS_SIZE, y + CROSS_SIZE, x + CROSS_SIZE, y - CROSS_SIZE);
  }

  /**
   * Draws a cross (X) centered at the specified floating-point coordinates.
   *
   * @param x the x-coordinate of the cross center
   * @param y the y-coordinate of the cross center
   */
  public void drawCross(float x, float y) {
    drawCross((int) x, (int) y);
  }

  /**
   * Draws a cross (X) centered at the position specified by a Vector2.
   *
   * @param position the center position of the cross
   */
  public void drawCross(Vector2 position) {
    drawCross((int) position.x, (int) position.y);
  }

  // ------------------ Draw Geometry ------------------
  // ---------------------------------------------------

  /**
   * Fill the rectangle with color.
   *
   * @param color  the color
   * @param left   left point
   * @param top    top point
   * @param width  width value
   * @param height height value
   */
  public void fillRect(Color color, int left, int top, int width, int height) {
    Color old = brush.getColor();
    brush.setColor(color);
    brush.fillRect(left, top, width, height);
    brush.setColor(old);
  }

  /**
   * Draw a rectangle.
   *
   * @param left   the left point
   * @param top    the top point
   * @param right  the right point
   * @param bottom the bottom point
   */
  public void drawRect(int left, int top, int right, int bottom) {
    if (left > right) {
      int tmp = right;
      right = left;
      left = tmp;
    }
    brush.setColor(penColor);
    brush.drawRect(left, top, right - left, bottom - top);
    if (Objects.nonNull(bgColor)) {
      brush.setColor(bgColor);
      brush.fillRect(left, top, right - left, bottom - top);
    }
  }

  public void drawRect(float left, float top, float right, float bot) {
    drawRect((int) left, (int) top, (int) right, (int) bot);
  }

  /**
   * Draws a closed shape.
   *
   * @param points the list of points
   */
  public void drawClosedShape(List<Vector2> points) {

    polygon.reset();

    for (Vector2 p : points) {
      polygon.addPoint((int) p.x, (int) p.y);
    }
    brush.setColor(penColor);
    brush.drawPolygon(polygon);
    if (Objects.nonNull(bgColor)) {
      brush.fillPolygon(polygon);
    }
  }

  /**
   * Draws a circle with the specified center and radius.
   *
   * @param x the x-coordinate of the circle center
   * @param y the y-coordinate of the circle center
   * @param radius the radius of the circle in pixels
   */
  public void drawCircle(int x, int y, int radius) {
    brush.drawOval(x - radius, y - radius, radius * 2, radius * 2);
  }

  /**
   * Draws a circle with floating-point center coordinates and radius.
   *
   * @param x the x-coordinate of the circle center
   * @param y the y-coordinate of the circle center
   * @param radius the radius of the circle
   */
  public void drawCircle(float x, float y, float radius) {
    drawCircle((int) x, (int) y, (int) radius);
  }

  /**
   * Draws a circle centered at the specified Vector2 position.
   *
   * @param position the center position of the circle
   * @param radius the radius of the circle
   */
  public void drawCircle(Vector2 position, float radius) {
    drawCircle((int) position.x, (int) position.y, (int) radius);
  }

  /**
   * Draws a filled circle with the specified center and radius.
   *
   * @param x the x-coordinate of the circle center
   * @param y the y-coordinate of the circle center
   * @param radius the radius of the circle in pixels
   */
  public void drawFillCircle(int x, int y, int radius) {
    brush.fillOval(x - radius, y - radius, radius * 2, radius * 2);
  }

  /**
   * Draws a filled circle with floating-point center coordinates and radius.
   *
   * @param x the x-coordinate of the circle center
   * @param y the y-coordinate of the circle center
   * @param radius the radius of the circle
   */
  public void drawFillCircle(float x, float y, float radius) {
    drawFillCircle((int) x, (int) y, (int) radius);
  }

  /**
   * Draws a filled circle centered at the specified Vector2 position.
   *
   * @param position the center position of the circle
   * @param radius the radius of the circle
   */
  public void drawFillCircle(Vector2 position, float radius) {
    drawFillCircle((int) position.x, (int) position.y, (int) radius);
  }

  /**
   * Sets the current pen color for drawing operations.
   *
   * @param color the color to use for drawing
   */
  public void setPenColor(Color color) {
    penColor = color;
    brush.setColor(color);
  }

  /**
   * Sets the pen color using RGB components.
   *
   * @param r the red component (0-255)
   * @param g the green component (0-255)
   * @param b the blue component (0-255)
   */
  public void setPenColor(int r, int g, int b) {
    setPenColor(new Color(r, g, b));
  }

  /**
   * Sets the background color for filling operations.
   *
   * @param color the color to use for filling
   */
  public void setBgColor(Color color) {
    bgColor = color;
  }

  /**
   * Sets the background color using RGB components.
   *
   * @param r the red component (0-255)
   * @param g the green component (0-255)
   * @param b the blue component (0-255)
   */
  public void setBgColor(int r, int g, int b) {
    setBgColor(new Color(r, g, b));
  }

  /**
   * Gets the current pen color used for drawing operations.
   *
   * @return the current pen color
   */
  public Color getPenColor() {
    return penColor;
  }

  /**
   * Gets the current background color used for filling operations.
   *
   * @return the current background color
   */
  public Color getBgColor() {
    return bgColor;
  }

  /**
   * Gets the current text color used for text rendering.
   *
   * @return the current text color
   */
  public Color getTextColor() {
    return textColor;
  }

  /**
   * Gets the current background color used for text rendering.
   *
   * @return the current text background color
   */
  public Color getBgTextColor() {
    return bgTextColor;
  }

  /**
   * Checks if text is currently being rendered with an opaque background.
   *
   * @return true if text has an opaque background, false otherwise
   */
  public boolean isBgTextOpaque() {
    return bgTextOpaque;
  }

  /**
   * Gets the current graphics context used for drawing operations.
   *
   * @return the current graphics context
   */
  public Graphics getBrush() {
    return brush;
  }
}
