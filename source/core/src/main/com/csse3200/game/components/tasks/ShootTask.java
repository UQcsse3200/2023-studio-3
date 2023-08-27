package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;


/**
 * Task that prints a message to the terminal whenever it is called.
 */
public class ShootTask extends DefaultTask implements PriorityTask {
  private String message;
  private final Entity target;
  private final int priority;

  /**
   * @param target The entity to chase.
   */
  public ShootTask(Entity target, int priority) {
    this.target = target;
    this.priority = priority;
    this.message = "Shoot Task Activated " + target;

    this.owner.getEntity().getEvents().trigger("shootStart");
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

  @Override
  public int getPriority() {
    return 10; // High priority task
  }
}
