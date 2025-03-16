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
        // Identity matrix should have 1s on the diagonal and 0s elsewhere
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);
        matrix.transformVector2D(point);
        assertEquals(1.0f, point.x, DELTA);
        assertEquals(1.0f, point.y, DELTA);
    }

    @Test
    void whenTranslating_shouldMovePoint() {
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);
        matrix.translate(2.0f, 3.0f);
        matrix.transformVector2D(point);
        assertEquals(3.0f, point.x, DELTA); // 1 + 2
        assertEquals(4.0f, point.y, DELTA); // 1 + 3
    }

    @Test
    void whenScaling_shouldScalePoint() {
        Vector2 point = Vector2.valueOf(2.0f, 3.0f);
        matrix.scale(2.0f, 0.5f);
        matrix.transformVector2D(point);
        assertEquals(4.0f, point.x, DELTA); // 2 * 2
        assertEquals(1.5f, point.y, DELTA); // 3 * 0.5
    }

    @Test
    void whenRotating_shouldRotatePoint() {
        Vector2 point = Vector2.valueOf(1.0f, 0.0f);
        // Rotate 90 degrees (π/2 radians)
        matrix.rotate((float) Math.PI / 2);
        matrix.transformVector2D(point);
        assertEquals(0.0f, point.x, DELTA, "X coordinate after 90-degree rotation");
        assertEquals(1.0f, point.y, DELTA, "Y coordinate after 90-degree rotation");
    }

    @Test
    void whenRotatingWithVectors_shouldRotatePoint() {
        Vector2 forward = Vector2.valueOf(0.0f, 1.0f);
        Vector2 side = Vector2.valueOf(1.0f, 0.0f);
        Vector2 point = Vector2.valueOf(1.0f, 0.0f);

        matrix.rotate(forward, side);
        matrix.transformVector2D(point);
        assertEquals(1.0f, point.x, DELTA);
        assertEquals(0.0f, point.y, DELTA);
    }

    @Test
    void whenTransformingMultiplePoints_shouldTransformAll() {
        List<Vector2> points = new ArrayList<>();
        points.add(Vector2.valueOf(1.0f, 1.0f));
        points.add(Vector2.valueOf(2.0f, 2.0f));
        points.add(Vector2.valueOf(3.0f, 3.0f));

        matrix.translate(1.0f, 1.0f);
        matrix.transformVector2Ds(points);

        assertEquals(2.0f, points.get(0).x, DELTA);
        assertEquals(2.0f, points.get(0).y, DELTA);
        assertEquals(3.0f, points.get(1).x, DELTA);
        assertEquals(3.0f, points.get(1).y, DELTA);
        assertEquals(4.0f, points.get(2).x, DELTA);
        assertEquals(4.0f, points.get(2).y, DELTA);
    }

    @Test
    void whenCombiningTransformations_shouldApplyInOrder() {
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);

        // Scale, then rotate 90 degrees, then translate
        matrix.scale(2.0f, 2.0f);
        matrix.rotate((float) Math.PI / 2);
        matrix.translate(1.0f, 1.0f);

        matrix.transformVector2D(point);

        // After scale: (2, 2)
        // After rotation: (-2, 2)
        // After translation: (-1, 3)
        assertEquals(-1.0f, point.x, DELTA);
        assertEquals(3.0f, point.y, DELTA);
    }

    @Test
    void whenSettingMatrixElements_shouldUpdateTransformation() {
        matrix.p11(2.0f);
        matrix.p22(2.0f);
        
        Vector2 point = Vector2.valueOf(1.0f, 1.0f);
        matrix.transformVector2D(point);
        
        assertEquals(2.0f, point.x, DELTA);
        assertEquals(2.0f, point.y, DELTA);
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
        
        assertEquals(1.0f, point.x, DELTA);
        assertEquals(1.0f, point.y, DELTA);
    }
}

