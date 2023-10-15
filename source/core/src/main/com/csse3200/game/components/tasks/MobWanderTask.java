package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class MobWanderTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(MobWanderTask.class);

  private final float waitTime;
  private Vector2 startPos;
  private MovementTask movementTask;
  private WaitTask waitTask;
  private Task currentTask;
  private boolean isDead = false;

  /**
   * @param waitTime How long in seconds to wait between wandering.
   */
  public MobWanderTask(float waitTime) {
    this.waitTime = waitTime;
  }

  @Override
  public int getPriority() {
    return 1; // Low priority task
  }

  @Override
  public void start() {
    super.start();
    startPos = owner.getEntity().getPosition();

    waitTask = new WaitTask(waitTime);
    waitTask.create(owner);

    movementTask = new MovementTask(getDirection());
    movementTask.create(owner);

    movementTask.start();

    currentTask = movementTask;


    // this.owner.getEntity().getEvents().trigger("wanderStart");
  }

  @Override
  public void update() {

    // Update the position of the mob
    Vector2 mobPosition = owner.getEntity().getPosition();

    // If the mob is at zero health, kill the mob,
    // play the death animation and stop the task
    // This method is the idea of Ahmad who very kindly helped with section, massive props to him for his help!
    if (!isDead && Boolean.TRUE.equals(owner.getEntity().getComponent(CombatStatsComponent.class).isDead())) {
      this.owner.getEntity().getEvents().trigger("dieStart");
      currentTask.stop();
      isDead = true;
      ServiceLocator.getWaveService().updateEnemyCount();
    }

    // Check if the mob has finished death animation
    else if (isDead && owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
      // Drop scrap at the mobs location
      Entity scrap = DropFactory.createScrapDrop();
      scrap.setPosition(mobPosition.x, mobPosition.y);
      ServiceLocator.getEntityService().register(scrap);

      // Delete the mob and update count for number of mobs remaining in the wave
      owner.getEntity().setFlagForDelete(true);

    }
    // If not dead, do normal things...
    else if (!isDead) {

      if (currentTask.getStatus() != Status.ACTIVE) {
        if (currentTask == movementTask) {
          startWaiting();
        } else {
          startMoving();
        }
      }
      currentTask.update();
    }
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    this.owner.getEntity().getEvents().trigger("stop");
    swapTask(waitTask);
  }

  private void startMoving() {
    logger.debug("Starting moving");
    movementTask.setTarget(getDirection());
    this.owner.getEntity().getEvents().trigger("wanderStart");
    swapTask(movementTask);
  }

  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  private Vector2 getDirection() {
    float y = startPos.y;
    return new Vector2(0, y);
  }
}
