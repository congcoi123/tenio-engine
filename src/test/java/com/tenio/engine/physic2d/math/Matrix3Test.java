package com.tenio.engine.physic2d.math;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Matrix3Test {

    private static final float DELTA = 0.0001f;
    private Matrix3 matrix;

    @BeforeEach
    void setUp() {
        matrix = Matrix3.newInstance();
    }

    @Test
    void whenCreated_shouldBeIdentityMatrix() {
        // Given
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);

        // When
        matrix.transformVector2D(point);

        // Then
        assertEquals(1.0f, point.getX());
        assertEquals(1.0f, point.getY());
    }

    @Test
    void whenTranslating_shouldTranslatePoint() {
        // Given
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);
        matrix.translate(2.0f, 3.0f);

        // When
        matrix.transformVector2D(point);

        // Then
        assertEquals(3.0f, point.getX());
        assertEquals(4.0f, point.getY());
    }

    @Test
    void whenScaling_shouldScalePoint() {
        Vector2 point = Vector2.valueOf(2.0f, 3.0f);
        matrix.scale(2.0f, 3.0f);
        matrix.transformVector2D(point);
        assertEquals(4.0f, point.getX(), DELTA);
        assertEquals(9.0f, point.getY(), DELTA);
    }

    @Test
    void whenTransformingMultiplePoints_shouldTransformAll() {
        List<Vector2> points = new ArrayList<>();
        points.add(Vector2.valueOf(1.0f, 1.0f));
        points.add(Vector2.valueOf(2.0f, 2.0f));
        points.add(Vector2.valueOf(3.0f, 3.0f));

        matrix.translate(1.0f, 1.0f);
        matrix.transformVector2Ds(points);

        assertEquals(2.0f, points.get(0).getX(), DELTA);
        assertEquals(2.0f, points.get(0).getY(), DELTA);
        assertEquals(3.0f, points.get(1).getX(), DELTA);
        assertEquals(3.0f, points.get(1).getY(), DELTA);
        assertEquals(4.0f, points.get(2).getX(), DELTA);
        assertEquals(4.0f, points.get(2).getY(), DELTA);
    }

    @Test
    void whenSettingMatrixElements_shouldUpdateTransformation() {
        matrix.p11(2.0f);
        matrix.p22(2.0f);
        
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);
        matrix.transformVector2D(point);
        
        assertEquals(2.0f, point.getX(), DELTA);
        assertEquals(2.0f, point.getY(), DELTA);
    }

    @Test
    void whenInitializing_shouldResetToIdentity() {
        // First apply some transformations
        matrix.translate(2.0f, 3.0f);
        matrix.scale(2.0f, 2.0f);
        
        // Then initialize back to identity
        matrix.initialize();
        
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);
        matrix.transformVector2D(point);
        
        assertEquals(1.0f, point.getX(), DELTA);
        assertEquals(1.0f, point.getY(), DELTA);
    }

    @Test
    void whenTranslating_shouldMovePoint() {
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);
        matrix.translate(2.0f, 3.0f);
        matrix.transformVector2D(point);
        assertEquals(3.0f, point.getX(), DELTA);
        assertEquals(4.0f, point.getY(), DELTA);
    }
}

