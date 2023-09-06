package com.csse3200.game.components.tasks.human;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move to a given position, finishing when you get close enough. Requires an entity with a
 * PhysicsMovementComponent.
 */
public class HumanMovementTask extends DefaultTask {
  private static final Logger logger = LoggerFactory.getLogger(HumanMovementTask.class);

  private final GameTime gameTime;
  private Entity target;
  private float stopDistance = 0.01f;
  private long lastTimeMoved;
  private Vector2 lastPos;
  private PhysicsMovementComponent movementComponent;

  public HumanMovementTask(Entity target) {
    this.target = target;
    this.gameTime = ServiceLocator.getTimeSource();
  }

  public HumanMovementTask(Entity target, float stopDistance) {
    this(target);
    this.stopDistance = stopDistance;
  }

  @Override
  public void start() {
    super.start();
    this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
    movementComponent.setTarget(target.getPosition());
    movementComponent.setMoving(true);

    // Trigger the correct walk animation depending on the target location.
    if (target.getPosition().x < owner.getEntity().getPosition().x) {
      owner.getEntity().getEvents().trigger("walkLeftStart");
    } else {
      owner.getEntity().getEvents().trigger("walkRightStart");
    }

    logger.debug("Starting movement towards {}", target);
    lastTimeMoved = gameTime.getTime();
    lastPos = owner.getEntity().getPosition();
  }

  @Override
  public void update() {
    if (isAtTarget()) {
      movementComponent.setMoving(false);
      owner.getEntity().getEvents().trigger("idleStart");
      status = Status.FINISHED;
      logger.debug("Finished moving to {}", target);
    } else {
      checkIfStuck();
    }
  }

  public void setTarget(Entity target) {
    this.target = target;
    movementComponent.setTarget(target.getPosition());
  }

  @Override
  public void stop() {
    super.stop();
    movementComponent.setMoving(false);
    logger.debug("Stopping movement");
  }

  private boolean isAtTarget() {
    return owner.getEntity().getPosition().dst(target.getPosition()) <= stopDistance;
  }

  private void checkIfStuck() {
    if (didMove()) {
      lastTimeMoved = gameTime.getTime();
      lastPos = owner.getEntity().getPosition();
    } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
      movementComponent.setMoving(false);
      status = Status.FAILED;
      logger.debug("Got stuck! Failing movement task");
    }
  }

  private boolean didMove() {
    return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
  }
}
