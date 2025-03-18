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

package com.tenio.engine.heartbeat;

import com.tenio.common.logger.AbstractLogger;
import com.tenio.common.utility.MathUtility;
import com.tenio.common.utility.TimeUtility;
import com.tenio.engine.message.ExtraMessage;
import com.tenio.engine.physic2d.graphic.Paint;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Abstract base class for implementing heartbeat functionality.
 *
 * <p>This class provides the core functionality for managing game loop timing and updates.
 * It handles frame rate control, time step calculations, and rendering updates.
 *
 * <p>Example implementation:
 * {@code
 * public class GameHeartBeat extends AbstractHeartBeat {
 *     @Override
 *     public void onInitialize() {
 *         // Initialize game state
 *     }
 *
 *     @Override
 *     public void onUpdate(float deltaTime) {
 *         // Update game logic
 *     }
 *
 *     @Override
 *     public void onRender(Graphics2D g, int width, int height) {
 *         // Render game state
 *     }
 *
 *     @Override
 *     public void onPause() {
 *         // Handle pause state
 *     }
 *
 *     @Override
 *     public void onResume() {
 *         // Handle resume state
 *     }
 * }
 * }
 *
 * @see com.tenio.engine.heartbeat.HeartBeatManager
 * @since 0.5.0
 */
public abstract class AbstractHeartBeat extends AbstractLogger
    implements Callable<Void>, ActionListener {

  /**
   * The target frame per second.
   */
  private static final int TARGET_FPS = 60;
  /**
   * Calculate how many ns each frame should take for our target game hertz.
   */
  private static final double TIME_BETWEEN_UPDATES = 1000000000 / TARGET_FPS;
  private static final double TIME_BETWEEN_RENDER = 1000000000 / TARGET_FPS;
  /**
   * At the very most we will update the game this many times before a new render.
   * If you're worried about visual hitches more than perfect timing, set this to
   * 1.
   */
  private static final int MAX_UPDATES_BEFORE_RENDER = 5;

  /**
   * A Set is used as the container for the delayed messages because of the
   * benefit of automatic sorting and avoidance of duplicates. Messages are sorted
   * by their dispatch time. See {@link HeartbeatMessage}
   */
  private TreeSet<HeartbeatMessage> listener;

  /**
   * For holding a frame.
   */
  private GamePanel gamePanel;

  /**
   * Can be customized action button.
   */
  private JButton action1;
  /**
   * Can be customized action button.
   */
  private JButton action2;
  /**
   * Can be customized action button.
   */
  private JButton action3;

  /**
   * The screen view width (in pixel).
   */
  private int viewWidth;
  /**
   * The screen view height (in pixel).
   */
  private int viewHeight;

  /**
   * Frame per second.
   */
  private int currentFps;
  private int frameCount;

  private int currentCcu;

  private boolean running;
  private boolean debugging;

  /**
   * Create a new instance with default FPS value, see {@value #TARGET_FPS}.
   */
  public AbstractHeartBeat() {
    currentFps = TARGET_FPS;
    frameCount = 0;
    currentCcu = 0;

    running = true;
    debugging = false;
  }

  /**
   * Create a new instance.
   *
   * @param viewWidth  the view width in pixel
   * @param viewHeight the view height in pixel
   */
  public AbstractHeartBeat(int viewWidth, int viewHeight) {
    this();
    this.viewWidth = viewWidth;
    this.viewHeight = viewHeight;
  }

  /**
   * Set the listener for the heart-beat. It's used to communicate with outside.
   * This method must be called before {@link #call()}.
   *
   * @param listener the messages' listener
   */
  public void setMessageListener(TreeSet<HeartbeatMessage> listener) {
    this.listener = listener;
  }

  /**
   * Start a new life cycle (game loop).
   */
  private void start() {
    // seed random number generator
    MathUtility.setSeed(0);
    onInitialization();
    // main loop
    loop();
  }

  /**
   * Pause game loop.
   *
   * @param pause set <b>true</b> if you want to pause the current game loop
   */
  protected void pause(final boolean pause) {
    if (pause) { // pause
      running = false;
      onPause();
    } else { // resume
      onResume();
      running = true;
    }
  }

  /**
   * Retrieves the FPS.
   *
   * @return the FPS
   */
  protected int getFps() {
    return currentFps;
  }

  /**
   * Display a window for debugging.
   *
   * @param title the title of debug window
   */
  public void debug(String title) {
    JFrame jframe = new JFrame();
    jframe.setTitle(title);

    gamePanel = new GamePanel();

    action1 = new JButton("Action 1");
    action2 = new JButton("Action 2");
    action3 = new JButton("Action 3");

    var contentPane = jframe.getContentPane();
    contentPane.setLayout(new BorderLayout());

    var panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2));
    panel.add(action1);
    panel.add(action2);
    panel.add(action3);

    contentPane.add(gamePanel, BorderLayout.CENTER);
    contentPane.add(panel, BorderLayout.SOUTH);

    jframe.setSize(viewWidth, viewHeight);

    action1.addActionListener(this);
    action2.addActionListener(this);
    action3.addActionListener(this);

    jframe.setVisible(true);
    debugging = true;
  }

  /**
   * Updates the CCU information.
   *
   * @param currentCcu the current number of concurrent players on the server
   */
  public void setCcu(int currentCcu) {
    this.currentCcu = currentCcu;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    var s = e.getSource();
    if (s == action1) {
      onAction1();
    } else if (s == action2) {
      onAction2();
    } else if (s == action3) {
      onAction3();
    }
  }

  /**
   * The main loop, the
   * <a href="http://www.java-gaming.org/index.php?topic=24220.0">reference</a>.
   */
  private void loop() {
    // We will need the last update time.
    double lastUpdateTime = System.nanoTime();
    // Store the last time we rendered.
    double lastRenderTime = System.nanoTime();

    // Simple way of finding FPS.
    int lastSecondTime = (int) (lastUpdateTime / 1000000000);

    while (running) {
      double now = System.nanoTime();
      int updateCount = 0;

      // Do as many game updates as we need to, potentially playing catch-up.
      while (now - lastUpdateTime > TIME_BETWEEN_UPDATES
          && updateCount < MAX_UPDATES_BEFORE_RENDER) {
        float delta = 1.0f / currentFps;

        // Message communication
        // get current time
        double currentTime = TimeUtility.currentTimeSeconds();

        // Process messages that are ready to be delivered
        while (!listener.isEmpty()) {
          var message = listener.first(); // Get earliest message
          if (message.getDelayTime() > currentTime) {
            break; // No more messages ready
          }
          onMessage(message.getMessage());
          listener.pollFirst(); // Remove and get next
        }

        // Main update
        onUpdate(delta);

        lastUpdateTime += TIME_BETWEEN_UPDATES;
        updateCount++;
      }

      // If for some reason an update takes forever, we don't want to do an insane
      // number of catch-ups.
      if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
        lastUpdateTime = now - TIME_BETWEEN_UPDATES;
      }

      // Render if debugging
      if (debugging) {
        draw();
      }
      lastRenderTime = now;

      // Update FPS counter
      int thisSecond = (int) (lastUpdateTime / 1000000000);
      if (thisSecond > lastSecondTime) {
        currentFps = frameCount;
        frameCount = 0;
        lastSecondTime = thisSecond;
      }

      // Sleep to maintain target frame rate
      long sleepTime = (long)((TIME_BETWEEN_RENDER - (now - lastRenderTime)) / 1000000);
      if (sleepTime > 0) {
        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt(); // Preserve interrupt status
          onDispose();
          return;
        }
      }

      // update counter
      frameCount++;
    }
  }

  /**
   * Repaint or refresh the screen.
   */
  private void draw() {
    gamePanel.repaint();
  }

  /**
   * Sets the first action text and its color.
   *
   * @param text  the text to display for action 1
   * @param color the color of the text
   */
  public void setTextAction1(String text, Color color) {
    action1.setText(text);
    action1.setBackground(color);
  }

  /**
   * Sets the second action text and its color.
   *
   * @param text  the text to display for action 2
   * @param color the color of the text
   */
  public void setTextAction2(String text, Color color) {
    action2.setText(text);
    action2.setBackground(color);
  }

  /**
   * Sets the third action text and its color.
   *
   * @param text  the text to display for action 3
   * @param color the color of the text
   */
  public void setTextAction3(String text, Color color) {
    action3.setText(text);
    action3.setBackground(color);
  }

  @Override
  public Void call() {
    start();
    return null;
  }

  /**
   * Called when the heart-beat is created.
   */
  protected abstract void onCreate();

  /**
   * Called when the heart-beat is initialized.
   */
  protected abstract void onInitialization();

  /**
   * It is called when the heart-beat receives a message from outside.
   *
   * @param message the coming message, see {@link ExtraMessage}
   */
  protected abstract void onMessage(ExtraMessage message);

  /**
   * Called each update cycle.
   * Override this method to implement the main update logic.
   *
   * @param deltaTime the time elapsed since the last update in seconds
   */
  protected abstract void onUpdate(float deltaTime);

  /**
   * It is called every frame after {@link #onUpdate(float)}.
   *
   * @param paint the painting object
   */
  protected abstract void onRender(Paint paint);

  /**
   * Called when the heartbeat is paused.
   * Override this method to handle pausing game systems.
   */
  protected abstract void onPause();

  /**
   * Called when the heartbeat is resumed.
   * Override this method to handle resuming game systems.
   */
  protected abstract void onResume();

  /**
   * Called when the heartbeat is disposed.
   * Override this method to clean up any resources.
   */
  protected abstract void onDispose();

  /**
   * Customize the button 1 action.
   */
  protected abstract void onAction1();

  /**
   * Customize the button 2 action.
   */
  protected abstract void onAction2();

  /**
   * Customize the button 3 action.
   */
  protected abstract void onAction3();

  /**
   * The debug window.
   */
  private class GamePanel extends JPanel {

    private static final long serialVersionUID = -768827954326690612L;

    private final Paint paint = Paint.getInstance();

    @Override
    public void paintComponent(Graphics graphic) {
      paint.startDrawing(graphic);
      // fill our back buffer with white
      paint.fillRect(Color.WHITE, 0, 0, viewWidth, viewHeight);
      onRender(paint);

      // show FPS
      graphic.setColor(Color.BLACK);
      graphic.drawString(String.format("FPS: %d", currentFps), 5, 10);

      // show CCU
      graphic.setColor(Color.RED);
      graphic.drawString(String.format("CCU: %d", currentCcu), viewWidth / 2 - 30, 10);
    }
  }
}
