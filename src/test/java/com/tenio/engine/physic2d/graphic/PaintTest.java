package com.tenio.engine.physic2d.graphic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tenio.engine.physic2d.math.Vector2;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PaintTest {

    private Paint paint;
    
    @Mock
    private Graphics graphics;
    
    @Mock
    private FontMetrics fontMetrics;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paint = Paint.getInstance();
        when(graphics.getFontMetrics()).thenReturn(fontMetrics);
        paint.startDrawing(graphics);
    }

    @Test
    void whenGettingInstance_shouldReturnSameInstance() {
        Paint anotherInstance = Paint.getInstance();
        assertSame(paint, anotherInstance);
    }

    @Test
    void whenDrawingText_shouldUseGraphics() {
        String text = "Test Text";
        int x = 10;
        int y = 20;
        when(fontMetrics.getHeight()).thenReturn(12);
        when(fontMetrics.getAscent()).thenReturn(10);
        when(fontMetrics.getDescent()).thenReturn(2);
        when(fontMetrics.stringWidth(text)).thenReturn(50);

        paint.drawTextAtPosition(x, y, text);

        verify(graphics).drawString(text, x, y + fontMetrics.getHeight() - 2);
    }

    @Test
    void whenDrawingLine_shouldUseGraphics() {
        int x1 = 10;
        int y1 = 20;
        int x2 = 30;
        int y2 = 40;

        paint.drawLine(x1, y1, x2, y2);

        verify(graphics).setColor(any(Color.class));
        verify(graphics).drawLine(x1, y1, x2, y2);
    }

    @Test
    void whenDrawingPolyLine_shouldUseGraphics() {
        List<Vector2> points = new ArrayList<>();
        points.add(Vector2.valueOf(10, 20));
        points.add(Vector2.valueOf(30, 40));
        points.add(Vector2.valueOf(50, 60));

        paint.drawPolyLine(points);

        verify(graphics).setColor(any(Color.class));
        verify(graphics).drawPolygon(any(Polygon.class));
    }

    @Test
    void whenDrawingCross_shouldDrawTwoLines() {
        Vector2 position = Vector2.valueOf(50, 50);
        int diameter = 10;

        paint.drawCross(position, diameter);

        verify(graphics, times(2)).setColor(any(Color.class));
        verify(graphics, times(2)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void whenDrawingCircle_shouldUseGraphics() {
        float x = 50;
        float y = 50;
        float radius = 25;

        paint.drawCircle(x, y, radius);

        verify(graphics).setColor(any(Color.class));
        verify(graphics).drawOval(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void whenDrawingRect_shouldUseGraphics() {
        int left = 10;
        int top = 20;
        int right = 30;
        int bottom = 40;

        paint.drawRect(left, top, right, bottom);

        verify(graphics).setColor(any(Color.class));
        verify(graphics).drawRect(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    void whenSettingColors_shouldUpdateColors() {
        Color penColor = Color.RED;
        Color bgColor = Color.BLUE;
        Color textColor = Color.GREEN;

        paint.setPenColor(penColor);
        paint.setBgColor(bgColor);
        paint.setTextColor(textColor);

        // Draw something to verify color usage
        paint.drawLine(0, 0, 10, 10);
        verify(graphics).setColor(penColor);
    }

    @Test
    void whenDrawingWithOpaqueText_shouldFillBackground() {
        String text = "Test Text";
        int x = 10;
        int y = 20;
        when(fontMetrics.getHeight()).thenReturn(12);
        when(fontMetrics.getAscent()).thenReturn(10);
        when(fontMetrics.getDescent()).thenReturn(2);
        when(fontMetrics.stringWidth(text)).thenReturn(50);

        paint.enableOpaqueText(true);
        paint.drawTextAtPosition(x, y, text);

        verify(graphics).fillRect(anyInt(), anyInt(), anyInt(), anyInt());
        verify(graphics).drawString(eq(text), anyInt(), anyInt());
    }
} 