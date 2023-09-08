package com.csse3200.game.components.tasks.human;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.tasks.EngineerCombatTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class HumanWanderTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(HumanWanderTask.class);

  private float maxRange;
  private Vector2 wanderRange;
  private final float waitTime;
  private Vector2 startPos;
  private HumanMovementTask movementTask;
  private HumanWaitTask waitTask;

  private EngineerCombatTask combatTask;
  private Task currentTask;

  private boolean isDead = false;

  /**
   * @param waitTime How long in seconds to wait between wandering.
   */
  public HumanWanderTask(float waitTime, float maxRange) {
    this.waitTime = waitTime;
    this.maxRange = maxRange;
  }

  @Override
  public int getPriority() {
    return 1; // Low priority task
  }

  @Override
  public void start() {
    super.start();
    startPos = owner.getEntity().getPosition();
    this.wanderRange = owner.getEntity().getCenterPosition();
    waitTask = new HumanWaitTask(waitTime);
    waitTask.create(owner);

    movementTask = new HumanMovementTask(this.wanderRange, 1f);
    movementTask.create(owner);
    movementTask.start();

    combatTask = new EngineerCombatTask(maxRange);
    combatTask.create(owner);
    combatTask.start();

    currentTask = movementTask;
  }

  @Override
  public void update() {
    // Check if engineer has died since last update
    if (!isDead && owner.getEntity().getComponent(CombatStatsComponent.class).isDead()) {
      owner.getEntity().getEvents().trigger("deathStart");
      owner.getEntity().getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NONE);
      owner.getEntity().getComponent(HitboxComponent.class).setLayer(PhysicsLayer.NONE);
      currentTask.stop();
      // Add a time delay here to allow animation to play?
      isDead = true;
    }
    // Check if engineer has finished dying
    else if (isDead && owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
      owner.getEntity().setFlagForDelete(true);
      // TODO: make the appropriate calls to decrement the human count.
    }
    // otherwise doing engineer things
    else if (!isDead) {
      if (currentTask.getStatus() != Status.ACTIVE) {

        if (currentTask == movementTask) {
          startWaiting();
          owner.getEntity().getEvents().trigger("idleRight");
        } else if (combatTask.isTargetVisible()) {
          if (combatTask.fetchTarget().y < owner.getEntity().getCenterPosition().y + 2 &&
                  combatTask.fetchTarget().y > owner.getEntity().getCenterPosition().y - 2) {
            startCombat();
          } else {
            startMoving(new Vector2(owner.getEntity().getCenterPosition().x, combatTask.fetchTarget().y));
          }
        }
      }
      currentTask.update();
    }
  }

  private void startWaiting() {
    logger.debug("Starting waiting");
    swapTask(waitTask);
  }

  private void startMoving(Vector2 destination) {
    logger.debug("Starting moving");
    movementTask.setTarget(destination);
    swapTask(movementTask);
  }

  private void startCombat() {
    logger.debug("Starting Combat");
    swapTask(combatTask);
  }

  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  private Vector2 getDirection() {
//    float y = startPos.y;
//    return new Vector2(0, y);
    return this.wanderRange;
  }
}
