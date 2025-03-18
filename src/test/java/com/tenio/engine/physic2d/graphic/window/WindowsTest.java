package com.tenio.engine.physic2d.graphic.window;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Windows} class and its inner classes {@link Windows.D2Point} and
 * {@link Windows.P2Point}. These tests verify the functionality of point creation, coordinate
 * handling, and constant values used in window operations.
 */
class WindowsTest {

    /**
     * Tests that when creating a D2Point, floating-point coordinates are properly rounded to the
     * nearest integer values. This test verifies the coordinate rounding behavior of the
     * {@link Windows.D2Point} class.
     */
    @Test
    void whenCreatingD2Point_shouldRoundCoordinates() {
        Windows.D2Point point = new Windows.D2Point();
        point.setLocation(1.6f, 2.3f);
        
        assertEquals(2.0f, point.x);
        assertEquals(2.0f, point.y);
    }

    /**
     * Tests the initialization behavior of P2Point across its different constructors. This test
     * verifies:
     * <ul>
     *   <li>Default constructor initializes to (0,0)</li>
     *   <li>Constructor with x,y coordinates sets values correctly</li>
     *   <li>Constructor with AWT Point sets values correctly</li>
     * </ul>
     */
    @Test
    void whenCreatingP2Point_shouldInitializeCorrectly() {
        // Default constructor
        Windows.P2Point point1 = new Windows.P2Point();
        assertEquals(0, point1.x);
        assertEquals(0, point1.y);

        // Constructor with coordinates
        Windows.P2Point point2 = new Windows.P2Point(5, 10);
        assertEquals(5, point2.x);
        assertEquals(10, point2.y);

        // Constructor with Point
        Point sourcePoint = new Point(15, 20);
        Windows.P2Point point3 = new Windows.P2Point(sourcePoint);
        assertEquals(15, point3.x);
        assertEquals(20, point3.y);
    }

    /**
     * Tests that all constant values defined in the Windows class have their expected values.
     * This includes:
     * <ul>
     *   <li>Menu flags (MF_CHECKED, MF_UNCHECKED, MFS_CHECKED, MFS_UNCHECKED)</li>
     *   <li>Foreground color constants (BLUE, GREEN, RED, INTENSITY)</li>
     *   <li>Background color constants (RED)</li>
     * </ul>
     */
    @Test
    void shouldHaveCorrectConstantValues() {
        assertEquals(0x00000008L, Windows.MF_CHECKED);
        assertEquals(0x00000000L, Windows.MF_UNCHECKED);
        assertEquals(Windows.MF_CHECKED, Windows.MFS_CHECKED);
        assertEquals(Windows.MF_UNCHECKED, Windows.MFS_UNCHECKED);
        assertEquals(0x0001, Windows.FOREGROUND_BLUE);
        assertEquals(0x0002, Windows.FOREGROUND_GREEN);
        assertEquals(0x0004, Windows.FOREGROUND_RED);
        assertEquals(0x0008, Windows.FOREGROUND_INTENSITY);
        assertEquals(0x0040, Windows.BACKGROUND_RED);
    }
}

