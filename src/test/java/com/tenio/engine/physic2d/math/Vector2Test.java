package com.tenio.engine.physic2d.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Vector2Test {

    private static final float DELTA = 0.0001f;
    private Vector2 vector;

    @BeforeEach
    void setUp() {
        vector = Vector2.newInstance();
    }

    @Test
    void whenCreated_shouldBeZero() {
        assertTrue(vector.isZero());
        assertEquals(0, vector.x, DELTA);
        assertEquals(0, vector.y, DELTA);
    }

    @Test
    void whenValueOf_shouldSetCorrectValues() {
        vector = Vector2.valueOf(3.0f, 4.0f);
        assertEquals(3.0f, vector.x, DELTA);
        assertEquals(4.0f, vector.y, DELTA);
    }

    @Test
    void whenCloned_shouldCreateNewInstance() {
        vector.set(3.0f, 4.0f);
        Vector2 clone = vector.clone();
        
        assertNotSame(vector, clone);
        assertEquals(vector.x, clone.x, DELTA);
        assertEquals(vector.y, clone.y, DELTA);
    }

    @Test
    void whenGettingLength_shouldCalculateCorrectly() {
        vector.set(3.0f, 4.0f);
        assertEquals(5.0f, vector.getLength(), DELTA);
        assertEquals(25.0f, vector.getLengthSqr(), DELTA);
    }

    @Test
    void whenNormalized_shouldHaveLengthOne() {
        vector.set(3.0f, 4.0f);
        vector.normalize();
        assertEquals(1.0f, vector.getLength(), DELTA);
    }

    @Test
    void whenNormalizedZeroVector_shouldRemainZero() {
        vector.zero();
        vector.normalize();
        assertTrue(vector.isZero());
    }

    @Test
    void whenCalculatingDotProduct_shouldBeCorrect() {
        vector.set(1.0f, 0.0f);
        Vector2 other = Vector2.valueOf(0.0f, 1.0f);
        assertEquals(0.0f, vector.getDotProductValue(other), DELTA); // Perpendicular vectors

        other.set(1.0f, 0.0f);
        assertEquals(1.0f, vector.getDotProductValue(other), DELTA); // Parallel vectors
    }

    @Test
    void whenGettingSignValue_shouldIndicateCorrectOrientation() {
        vector.set(1.0f, 0.0f);
        Vector2 other = Vector2.valueOf(0.0f, 1.0f);
        assertEquals(Vector2.ANTI_CLOCK_WISE, vector.getSignValue(other));

        other.set(0.0f, -1.0f);
        assertEquals(Vector2.CLOCK_WISE, vector.getSignValue(other));
    }

    @Test
    void whenMakingPerpendicular_shouldRotate90Degrees() {
        vector.set(3.0f, 4.0f);
        vector.perpendicular();
        assertEquals(-4.0f, vector.x, DELTA);
        assertEquals(3.0f, vector.y, DELTA);
    }

    @Test
    void whenTruncating_shouldLimitLength() {
        vector.set(3.0f, 4.0f);
        float maxLength = 2.5f;
        vector.truncate(maxLength);
        assertTrue(vector.getLength() <= maxLength);
    }

    @Test
    void whenCalculatingDistance_shouldBeCorrect() {
        vector.set(1.0f, 1.0f);
        Vector2 other = Vector2.valueOf(4.0f, 5.0f);
        assertEquals(5.0f, vector.getDistanceValue(other), DELTA);
        assertEquals(25.0f, vector.getDistanceSqrValue(other), DELTA);
    }

    @Test
    void whenReversed_shouldInvertComponents() {
        vector.set(3.0f, 4.0f);
        vector.reverse();
        assertEquals(-3.0f, vector.x, DELTA);
        assertEquals(-4.0f, vector.y, DELTA);
    }

    @Test
    void whenAdding_shouldCombineComponents() {
        vector.set(1.0f, 2.0f);
        vector.add(2.0f, 3.0f);
        assertEquals(3.0f, vector.x, DELTA);
        assertEquals(5.0f, vector.y, DELTA);

        Vector2 other = Vector2.valueOf(1.0f, 1.0f);
        vector.add(other);
        assertEquals(4.0f, vector.x, DELTA);
        assertEquals(6.0f, vector.y, DELTA);
    }

    @Test
    void whenSubtracting_shouldReduceComponents() {
        vector.set(5.0f, 7.0f);
        vector.sub(2.0f, 3.0f);
        assertEquals(3.0f, vector.x, DELTA);
        assertEquals(4.0f, vector.y, DELTA);

        Vector2 other = Vector2.valueOf(1.0f, 1.0f);
        vector.sub(other);
        assertEquals(2.0f, vector.x, DELTA);
        assertEquals(3.0f, vector.y, DELTA);
    }

    @Test
    void whenMultiplying_shouldScaleComponents() {
        vector.set(2.0f, 3.0f);
        vector.mul(2.0f);
        assertEquals(4.0f, vector.x, DELTA);
        assertEquals(6.0f, vector.y, DELTA);
    }

    @Test
    void whenDividing_shouldReduceComponents() {
        vector.set(4.0f, 6.0f);
        vector.div(2.0f);
        assertEquals(2.0f, vector.x, DELTA);
        assertEquals(3.0f, vector.y, DELTA);
    }

    @Test
    void whenComparingEquality_shouldCheckComponents() {
        vector.set(1.0f, 2.0f);
        Vector2 same = Vector2.valueOf(1.0f, 2.0f);
        Vector2 different = Vector2.valueOf(1.0f, 3.0f);

        assertTrue(vector.isEqual(same));
        assertFalse(vector.isEqual(different));
    }

    @Test
    void whenConvertingToString_shouldFormatCorrectly() {
        vector.set(1.0f, 2.0f);
        assertEquals("(1.0, 2.0)", vector.toString());
    }
}

