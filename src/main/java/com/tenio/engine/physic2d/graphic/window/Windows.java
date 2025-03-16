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

package com.tenio.engine.physic2d.graphic.window;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.net.URL;

/**
 * Provides utility methods and constants for window-related operations in the graphics system.
 * This class includes menu flags, color constants, and specialized point classes for 2D graphics.
 * It is designed to be used as a utility class and cannot be instantiated.
 * 
 * @since 0.1.0
 */
public final class Windows {

  /**
   * Menu flag indicating a checked state in a menu item.
   * Value: {@value #MF_CHECKED}
   */
  public static final long MF_CHECKED = 0x00000008L;

  /**
   * Menu flag indicating an unchecked state in a menu item.
   * Value: {@value #MF_UNCHECKED}
   */
  public static final long MF_UNCHECKED = 0x00000000L;

  /**
   * Menu flag state for checked items (alias for {@link #MF_CHECKED}).
   * Value: {@value #MFS_CHECKED}
   */
  public static final long MFS_CHECKED = MF_CHECKED;

  /**
   * Menu flag state for unchecked items (alias for {@link #MF_UNCHECKED}).
   * Value: {@value #MFS_UNCHECKED}
   */
  public static final long MFS_UNCHECKED = MF_UNCHECKED;

  /**
   * Foreground color constant for blue.
   * Value: {@value #FOREGROUND_BLUE}
   */
  public static final int FOREGROUND_BLUE = 0x0001;

  /**
   * Foreground color constant for green.
   * Value: {@value #FOREGROUND_GREEN}
   */
  public static final int FOREGROUND_GREEN = 0x0002;

  /**
   * Foreground color constant for red.
   * Value: {@value #FOREGROUND_RED}
   */
  public static final int FOREGROUND_RED = 0x0004;

  /**
   * Foreground intensity flag for color enhancement.
   * Value: {@value #FOREGROUND_INTENSITY}
   */
  public static final int FOREGROUND_INTENSITY = 0x0008;

  /**
   * Background color constant for red.
   * Value: {@value #BACKGROUND_RED}
   */
  public static final int BACKGROUND_RED = 0x0040;

  /**
   * Loads an icon image from a file resource.
   * The file should be accessible from the classpath as a resource.
   *
   * @param file the path to the icon file resource
   * @return the loaded Image object, or null if the resource cannot be found
   */
  public static Image loadIcon(String file) {
    URL iconUrl = Windows.class.getResource(file);
    return Toolkit.getDefaultToolkit().createImage(iconUrl);
  }

  /**
   * A specialized 2D point class that automatically rounds floating-point coordinates
   * to the nearest integer values. This class extends {@link Point2D.Float} and
   * overrides the coordinate setting behavior to enforce rounding.
   * 
   * <p>This class is particularly useful for graphics operations where coordinates
   * need to be aligned to pixel boundaries.
   */
  public static class D2Point extends Point2D.Float {

    private static final long serialVersionUID = -1196038548500345000L;

    /**
     * Sets the location of this point, rounding the coordinates to the nearest integer values.
     *
     * @param x the X coordinate to set
     * @param y the Y coordinate to set
     */
    @Override
    public void setLocation(float x, float y) {
      super.setLocation(Math.round(x), Math.round(y));
    }
  }

  /**
   * A point class for integer-based 2D coordinates. This class extends {@link Point}
   * and provides additional constructors for convenience.
   * 
   * <p>This class is useful for pixel-precise graphics operations where coordinates
   * must be integer values.
   */
  public static class P2Point extends Point {

    private static final long serialVersionUID = 8698839709008819029L;

    /**
     * Creates a new point at (0,0).
     */
    public P2Point() {
      this(0, 0);
    }

    /**
     * Creates a new point at the specified coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public P2Point(int x, int y) {
      super(x, y);
    }

    /**
     * Creates a new point with the same coordinates as the given point.
     *
     * @param point the point to copy coordinates from
     */
    public P2Point(Point point) {
      super(point);
    }
  }
}
