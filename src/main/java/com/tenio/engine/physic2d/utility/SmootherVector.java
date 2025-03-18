package com.tenio.engine.physic2d.utility;

import com.tenio.engine.physic2d.math.Vector2;
import java.util.ArrayList;
import java.util.List;

/**
 * Template class to help calculate the average value of a history of values.
 * This can only be used with types that have a 'zero' value and that have the
 * += and / operators overloaded.
 * <br>
 * Example: Used to smooth frame rate calculations.
 */
public final class SmootherVector {
  /**
   * This holds the history.
   */
  private final List<Vector2> history;
  private final int sampleSize;

  /**
   * Creates a new vector smoother.
   *
   * @param sampleSize the number of samples to use for smoothing
   */
  public SmootherVector(int sampleSize) {
    this.sampleSize = sampleSize;
    history = new ArrayList<>(sampleSize);
  }

  /**
   * Updates the smoother with a new sample.
   *
   * @param mostRecentValue the new value to add
   * @return the smoothed value
   */
  public Vector2 update(Vector2 mostRecentValue) {
    history.add(mostRecentValue.clone());
    if (history.size() > sampleSize) {
      history.remove(0);
    }

    Vector2 sum = new Vector2();
    for (Vector2 value : history) {
      sum.add(value);
    }
    sum.div(history.size());

    return sum;
  }
}
