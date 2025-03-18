package com.tenio.engine.physic2d.utility;

import org.junit.jupiter.api.Test;

class SmootherTest {
  @Test
  void testAdd() {
    Smoother<Number> smoother = new Smoother<Number>(3, Integer.valueOf(1));
    Integer a = Integer.valueOf(1);
    smoother.<Number>add(a, Integer.valueOf(1));
  }

  @Test
  void testAdd2() {
    Smoother<Number> smoother = new Smoother<Number>(3, Integer.valueOf(1));
    Integer a = Integer.valueOf(0);
    smoother.<Number>add(a, Integer.valueOf(1));
  }

  @Test
  void testAdd3() {
    Smoother<Number> smoother = new Smoother<Number>(3, Integer.valueOf(1));
    Integer a = Integer.valueOf(3);
    smoother.<Number>add(a, Integer.valueOf(1));
  }

  @Test
  void testAdd4() {
    Smoother<Number> smoother = new Smoother<Number>(3, Integer.valueOf(1));
    Integer a = Integer.valueOf(-1);
    smoother.<Number>add(a, Integer.valueOf(1));
  }

  @Test
  void testDiv() {
    Smoother<Number> smoother = new Smoother<Number>(3, Integer.valueOf(1));
    smoother.<Number>div(Integer.valueOf(1), 10.0f);
  }

  @Test
  void testDiv2() {
    Smoother<Number> smoother = new Smoother<Number>(3, Integer.valueOf(1));
    smoother.<Number>div(Integer.valueOf(0), 10.0f);
  }

  @Test
  void testDiv3() {
    Smoother<Number> smoother = new Smoother<Number>(3, Integer.valueOf(1));
    smoother.<Number>div(Integer.valueOf(3), 10.0f);
  }

  @Test
  void testDiv4() {
    Smoother<Number> smoother = new Smoother<Number>(3, Integer.valueOf(1));
    smoother.<Number>div(Integer.valueOf(-1), 10.0f);
  }

  @Test
  void testConstructor() {
    new Smoother<Number>(3, Integer.valueOf(1));
  }
}
