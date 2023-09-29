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

import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameTime;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class NewMobWanderTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(MobWanderTask.class);

  private final float waitTime;
  private Vector2 startPos;
  private MovementTask movementTask;
  private WaitTask waitTask;
  private Task currentTask;
  private boolean isDead = false;
  private Vector2 mobPosition;
  private final PhysicsEngine physics;
  private static final short TARGET = PhysicsLayer.HUMANS;
  private final RaycastHit hit = new RaycastHit();
  
  /**
   * @param wanderRange Distance in X and Y the entity can move from its position when start() is
   *     called.
   * @param waitTime How long in seconds to wait between wandering.
   */
  public NewMobWanderTask(float waitTime) {
    this.waitTime = waitTime;
    physics = ServiceLocator.getPhysicsService().getPhysics();
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

    this.owner.getEntity().getEvents().trigger("wanderStart");
  }

  @Override
  public void update() {

    //Update the position of the mob
    mobPosition = owner.getEntity().getPosition();

    // If the mob is at zero health, kill the mob,
    // play the death animation and stop the task
    // This method is the idea of Ahmad who very kindly helped
    // with section, massive props to him for his help!
    if (!isDead && owner.getEntity().getComponent(CombatStatsComponent.class).isDead()) {
      this.owner.getEntity().getEvents().trigger("dieStart");
      currentTask.stop();
      isDead = true;
    }

    // Check if the mob has finished death animation
    else if (isDead && owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {

      // Drop scrap at the mobs location for player
      // to collect.
      Entity scrap = DropFactory.createScrapDrop();
      scrap.setPosition(mobPosition.x,mobPosition.y);
      ServiceLocator.getEntityService().register(scrap);

      // Delete the mob.
      owner.getEntity().setFlagForDelete(true);

    }
    // If not dead, do normal things...
    else if (!isDead) {

      if (currentTask.getStatus() != Status.ACTIVE) {
        if (currentTask == movementTask) {
          if (isTargetVisible()) {
                Entity newProjectile = ProjectileFactory.createMobBall(PhysicsLayer.HUMANS, new Vector2(0, owner.getEntity().getPosition().y), new Vector2(2f,2f));
                newProjectile.setScale(-1f, 1f);
                newProjectile.setPosition((float) (owner.getEntity().getPosition().x), (float) (owner.getEntity().getPosition().y));
                ServiceLocator.getEntityService().register(newProjectile);

        //            System.out.printf("ANIMATION: " + owner.getEntity().getComponent(AnimationRenderComponent.class).getCurrentAnimation() + "\n");
                this.owner.getEntity().getEvents().trigger("shootStart");
                System.out.println("TESTING SHOOT");
            }   
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

  /**
   * Uses a raycast to determine whether there are any targets in detection range
   * @return true if a target is visible, false otherwise
   */
  private boolean isTargetVisible() {
    Vector2 newVector = new Vector2(owner.getEntity().getPosition().x - 10f, owner.getEntity().getPosition().y - 2f);
    return physics.raycast(owner.getEntity().getPosition(), newVector, TARGET, hit);
  }
}
