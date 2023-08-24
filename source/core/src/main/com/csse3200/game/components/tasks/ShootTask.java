package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.badlogic.gdx.math.Vector2;

/**
 * Task that prints a message to the terminal whenever it is called.
 */
public class ShootTask extends DefaultTask {
  private String message;

  /**
   * @param target The entity to chase.
   */
  public ShootTask(Vector2 target) {
    this.message = "Shoot Task Activated: " + target;

  }

  /**
   * Print the message when the task is started.
   * Will need to implement in here exactly how the shooting of the projectile occurs.
   */
  @Override
  public void start() {
    super.start();
    System.out.println(this.message);
  }
}
