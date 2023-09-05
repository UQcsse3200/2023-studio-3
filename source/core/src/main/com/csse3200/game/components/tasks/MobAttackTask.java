package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.entities.factories.ProjectileFactory;
//import com.csse3200.game.rendering.DebugRenderer;


/**
 * Task that prints a message to the terminal whenever it is called.
 */
public class MobAttackTask extends DefaultTask implements PriorityTask {
  private static final int INTERVAL = 1; // time interval to scan for towers in
  private static final short TARGET = PhysicsLayer.OBSTACLE; // mobs detecting for towers
  // ^ fix this

  private static final String STOW = "stowStart";
  private static final String DEPLOY = "deployStart";
  private static final String FIRING = "firingStart";
  private static final String IDLE = "idleStart";

  private final int priority;
  private final float maxRange;
  private Vector2 mobPosition = new Vector2(10f,10f);
  private final Vector2 maxRangePosition = new Vector2();
  private final PhysicsEngine physics;
  private GameTime timeSource;
  private long endTime;
  private final RaycastHit hit = new RaycastHit();
//  private final DebugRenderer debugRenderer;

  private enum STATE {
    IDLE, DEPLOY, FIRING, STOW
  }

  private STATE mobState = STATE.IDLE;

  /**
   * @param target The entity to shoot at.
   * @param priority Task priority when shooting (0 when not chasing).
   * @param viewDistance Maximum distance from the entity at which shooting can start.
   * @param maxChaseDistance Maximum distance from the entity while shooting before giving up.
   */
  public MobAttackTask(int priority, float maxRange) {
    this.priority = priority;
    this.maxRange = maxRange;

    physics = ServiceLocator.getPhysicsService().getPhysics();
//    debugRenderer = ServiceLocator.getRenderService().getDebug();
    timeSource = ServiceLocator.getTimeSource();
  }

  @Override
  public void start() {
    super.start();

    this.mobPosition = owner.getEntity().getCenterPosition();
    this.maxRangePosition.set(mobPosition.x + maxRange, mobPosition.y);
    owner.getEntity().getEvents().trigger(IDLE);
    endTime = timeSource.getTime() + (INTERVAL * 500);
  }

  @Override
  public void update() {
    if (timeSource.getTime() >= endTime) {
      updateMobState();
      endTime = timeSource.getTime() + (INTERVAL * 1000);
    }
  }

  public void updateMobState() {
    switch (mobState) {

      case IDLE -> {
        if (isTargetVisible()) {
          owner.getEntity().getEvents().trigger(DEPLOY);
          mobState = STATE.DEPLOY;
        }
      }

      case DEPLOY -> {
        if (isTargetVisible()) {
          owner.getEntity().getEvents().trigger(FIRING);
          mobState = STATE.FIRING;
        } else {
          owner.getEntity().getEvents().trigger(STOW);
          mobState = STATE.STOW;
        }
      }

      case FIRING -> {
        if (!isTargetVisible()) {
          owner.getEntity().getEvents().trigger(STOW);
          mobState = STATE.STOW;
        } else {
          owner.getEntity().getEvents().trigger(FIRING);
          Entity newProjectile = ProjectileFactory.createFireBall(owner.getEntity(), new Vector2(0, owner.getEntity().getPosition().y), new Vector2(2f,2f));
          newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.75), (float) (owner.getEntity().getPosition().y + 0.75));
          ServiceLocator.getEntityService().register(newProjectile);
        }
      }

      case STOW -> {
        if (isTargetVisible()) {
          owner.getEntity().getEvents().trigger(DEPLOY);
          mobState = STATE.DEPLOY;
        } else {
          owner.getEntity().getEvents().trigger(IDLE);
          mobState = STATE.IDLE;
        }
      }
    }
  }

  @Override
  public void stop() {
    super.stop();
    owner.getEntity().getEvents().trigger(STOW);
  }

  @Override
  public int getPriority() {
    return isTargetVisible() ? getActivePriority() : getInactivePriority();
  }

//  private float getDistanceToTarget() {
//    return owner.getEntity().getPosition().dst(target.getPosition());
//  }

  private int getActivePriority() {
    return !isTargetVisible() ? 0 : priority;
  }

  private int getInactivePriority() {
    return isTargetVisible() ? priority : 0;
  }

  private boolean isTargetVisible() {
    return physics.raycast(mobPosition, maxRangePosition, TARGET, hit);
  }
//    Vector2 from = owner.getEntity().getCenterPosition();
//    Vector2 to = target.getCenterPosition();
//
//    // If there is an obstacle in the path to the player, not visible.
//    if (physics.raycast(from, to, PhysicsLayer.OBSTACLE, hit)) {
//      debugRenderer.drawLine(from, hit.point);
//      return false;
//    }
//    debugRenderer.drawLine(from, to);
//    return true;
//  }
}
