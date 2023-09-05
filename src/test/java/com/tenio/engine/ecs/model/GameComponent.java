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

package com.tenio.engine.ecs.model;

import com.tenio.engine.ecs.model.component.Animation;
import com.tenio.engine.ecs.model.component.Motion;
import com.tenio.engine.ecs.model.component.Position;
import com.tenio.engine.ecs.model.component.View;

public final class GameComponent {

  public static byte ANIMATION = 0;
  public static byte MOTION = 1;
  public static byte POSITION = 3;
  public static byte VIEW = 4;

  private static final int numberComponents = 5;
  private static final String[] componentNames = {"Animation", "Motion", null, "Position", "View"};
  private static final Class<?>[] componentTypes
		  = {Animation.class, Motion.class, null, Position.class, View.class};

  public static int getNumberComponents() {
    return numberComponents;
  }

  public static String[] getComponentNames() {
    return componentNames;
  }

  public static Class<?>[] getComponentTypes() {
    return componentTypes;
  }
}
