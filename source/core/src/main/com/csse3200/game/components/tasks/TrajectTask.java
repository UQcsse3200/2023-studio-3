package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;

/** Trajects a projectile from an entity towards the enemy entities */
public class TrajectTask extends DefaultTask implements PriorityTask {
  private final int priority = 10;
  private MovementTask movementTask;
  private Vector2 destination;

  /**
   * @param destination The destination that the projectile will move towards.
   */
  public TrajectTask(Vector2 destination) {
    this.destination = destination;
  }

  /**
   * Start moving in the indicated direction.
   */
  @Override
  public void start() {
    super.start();
    movementTask = new MovementTask(destination);
    movementTask.create(owner);
    movementTask.start();
    
    this.owner.getEntity().getEvents().trigger("rotate");
  }

  @Override
  public void update() {
    movementTask.setTarget(destination);
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
    }
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
