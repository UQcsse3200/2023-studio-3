package com.csse3200.game.components.tasks.human;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Move a human entity to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class HumanMovementTask extends DefaultTask implements PriorityTask {
  private final GameTime gameTime;
  private Vector2 target;
  private float stopDistance = 0.01f;
  private long lastTimeMoved;
  private Vector2 lastPos;
  private PhysicsMovementComponent movementComponent;

  private int priority = 1000; // default priority

  public HumanMovementTask(Vector2 target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource();
  }

  public HumanMovementTask(Vector2 target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  @Override
  public void start() {
    super.start();
    this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
    movementComponent.setTarget(target);
    movementComponent.setMoving(true);

    // Trigger the correct walk animation depending on the target location.
    if (target.x < owner.getEntity().getPosition().x) {
      owner.getEntity().getEvents().trigger("walkLeftStart");
    } else {
      owner.getEntity().getEvents().trigger("walkRightStart");
    }

    lastTimeMoved = gameTime.getTime();
    lastPos = owner.getEntity().getPosition();
  }

  @Override
  public void update() {
    if (isAtTarget()) {
      movementComponent.setMoving(false);
      owner.getEntity().getEvents().trigger("idleStart");
      status = Status.FINISHED;
    } else {
      checkIfStuck();
    }
  }

  public void setTarget(Vector2 target) {
    this.target = target;
    movementComponent.setTarget(target);
  }

  @Override
  public void stop() {
    super.stop();
    movementComponent.setMoving(false);
  }

  private boolean isAtTarget() {
    return owner.getEntity().getPosition().dst(target) <= stopDistance;
  }

  private void checkIfStuck() {
    if (didMove()) {
      lastTimeMoved = gameTime.getTime();
      lastPos = owner.getEntity().getPosition();
    } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
      movementComponent.setMoving(false);
      status = Status.FAILED;
    }
  }

  private boolean didMove() {
    return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
