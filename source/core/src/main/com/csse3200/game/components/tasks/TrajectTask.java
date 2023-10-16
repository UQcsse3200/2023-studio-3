package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;

/** Trajects a projectile from an entity towards the enemy entities */
public class TrajectTask extends DefaultTask implements PriorityTask {
  private static final int PRIORITY = 10;
  private MovementTask movementTask;
  private Vector2 destination;
  private static final String START = "startProjectile";
  private static final String FINAL = "startProjectileFinal";

  private enum STATE {
    START, FINAL
  }
  private STATE projectileState = STATE.START;

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

    this.owner.getEntity().getEvents().trigger(START);
    this.owner.getEntity().getEvents().trigger("rotate");
    this.owner.getEntity().getEvents().trigger("start");
    this.owner.getEntity().getEvents().trigger("startMobBoss");
  }

  /**
   * Switches the state to FINAL if it is START.
   */
  public void switchProjectileState() {
    if (projectileState == STATE.START) {
      this.owner.getEntity().getEvents().trigger(FINAL);
      projectileState = STATE.FINAL;
    }
  }

  @Override
  public void update() {
    movementTask.setTarget(destination);
    movementTask.update();
    if (movementTask.getStatus() != Status.ACTIVE) {
      movementTask.start();
      switchProjectileState();
    }
  }

  @Override
  public void stop() {
    super.stop();
    movementTask.stop();
    this.owner.getEntity().getEvents().trigger(FINAL);
  }

  @Override
  public int getPriority() {
    return PRIORITY;
  }
}
