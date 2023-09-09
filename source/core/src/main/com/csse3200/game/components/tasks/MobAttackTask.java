package com.csse3200.game.components.tasks;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.entities.factories.ProjectileFactory;


/**
 * Task that allows mobs to shoot projectiles or melee attack towers
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

  private final long delay = 1000; // delay between shots
  private long startTime;

  private enum STATE {
    IDLE, DEPLOY, FIRING, STOW
  }

  private STATE mobState = STATE.IDLE;

  /**
   * @param priority Task priority when targets are detected (0 when nothing detected). Must be a positive integer.
   * @param maxRange Maximum effective range of the weapon mob. This determines the detection distance of targets
   */
  public MobAttackTask(int priority, float maxRange) {
    this.priority = priority;
    this.maxRange = maxRange;
    startTime = 0;

    physics = ServiceLocator.getPhysicsService().getPhysics();
    timeSource = ServiceLocator.getTimeSource();
  }

  /**
   * Starts the task running, triggers the initial "idleStart" event.
   */
  @Override
  public void start() {
    super.start();
    startTime = timeSource.getTime();
    this.mobPosition = owner.getEntity().getCenterPosition();
    this.maxRangePosition.set(4, mobPosition.y);
    owner.getEntity().getEvents().trigger(IDLE);
    endTime = timeSource.getTime() + (INTERVAL * 500);
    owner.getEntity().getEvents().trigger("shootStart");
    System.out.println("mob attack started for " + owner.getEntity().getId());
  }

  /**
   * The update method is what is run every time the TaskRunner in the AiTaskComponent calls update().
   * Triggers events depending on the presence or otherwise of targets in the detection range
   */
  @Override
  public void update() {
    updateMobState();

    if (mobState == STATE.STOW) {
      status = Status.FINISHED;
    }
  }

  /**
   * Mob state machine. Updates mob state by scanning for towers, and
   * triggers the appropriate events corresponding to the STATE enum.
   */
  public void updateMobState() {
//    TouchAttackComponent attackComp = owner.getEntity().getComponent(TouchAttackComponent.class);
    CombatStatsComponent statsComp = owner.getEntity().getComponent(CombatStatsComponent.class);
//    if (statsComp != null) {
//    System.out.println("is the target visible " + isTargetVisible());
//    }
//    if (!isTargetVisible()) {
//      System.out.println("target is not visible for " + owner.getEntity().getId());
//    }
    switch (mobState) {

      case IDLE -> {
        if (isTargetVisible()) {
          // targets detected in idle mode - start deployment
          owner.getEntity().getEvents().trigger(DEPLOY);
          mobState = STATE.DEPLOY;
        }
        System.out.println("idle for " + owner.getEntity().getId());

      }

      case DEPLOY -> {
        // currently deploying,
        if (isTargetVisible()) {
          owner.getEntity().getEvents().trigger(FIRING);
          mobState = STATE.FIRING;
          owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(false);
        } else {
          owner.getEntity().getEvents().trigger(STOW);
          mobState = STATE.STOW;

        }
        System.out.println("deploying for " + owner.getEntity().getId());

      }

      case FIRING -> {
//        owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(false);
        // targets gone - stop firing
        if (!isTargetVisible()) {
          owner.getEntity().getEvents().trigger(STOW);
          mobState = STATE.STOW;
        } else {
          owner.getEntity().getEvents().trigger(FIRING);
          Entity newProjectile = ProjectileFactory.createFireBall(owner.getEntity(), new Vector2(0, owner.getEntity().getPosition().y), new Vector2(2f,2f));
          newProjectile.setPosition((float) (owner.getEntity().getPosition().x), (float) (owner.getEntity().getPosition().y));
          newProjectile.setScale(-1f, 0.5f);
          ServiceLocator.getEntityService().register(newProjectile);
          mobState = STATE.STOW;
          owner.getEntity().getEvents().trigger("shootStart");
        }
        System.out.println("firing for " + owner.getEntity().getId());
        owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(true);
        System.out.println("the fixture for " + owner.getEntity().getId() + " is " + this.meleeOrProjectile());

      }

      case STOW -> {
        // currently stowing
        if (isTargetVisible()) {
          owner.getEntity().getEvents().trigger(DEPLOY);
          mobState = STATE.DEPLOY;
        } else {
          owner.getEntity().getEvents().trigger(IDLE);
          mobState = STATE.IDLE;
        }
        System.out.println("stowing for " + owner.getEntity().getId());

      }
    }
  }

  /**
   * For stopping the attack task
   */
  @Override
  public void stop() {
    if (mobState == STATE.FIRING || mobState == STATE.DEPLOY) {
      this.updateMobState();
    } else {
//      owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(true);
      System.out.println("mob attack stopped for " + owner.getEntity().getId());
      super.stop();
      owner.getEntity().getEvents().trigger(STOW);
    }
  }

  /**
   * Returns the current priority of the task.
   * @return active priority value if targets detected, inactive priority otherwise
   */
  @Override
  public int getPriority() {
//    return  -1;
    if (status == Status.ACTIVE) {
      return getActivePriority();
    }
    return getInactivePriority();
//    return isTargetVisible() ? getActivePriority() : getInactivePriority();
  }

  /**
   * Fetches the active priority of the Task if a target is visible.
   * @return (int) active priority if a target is visible, -1 otherwise
   */
  private int getActivePriority() {
     if ((startTime + delay) < timeSource.getTime() && isTargetVisible()) {
//     if (isTargetVisible() && (startTime + delay) > timeSource.getTime()) {
//       System.out.println("ready to fire while active");
       return priority;
     }
//     System.out.println("not ready to fire while active");
//     return !isTargetVisible() ? -1 : priority;
    return -1;
  }

  /**
   * Fetches the inactive priority of the Task if a target is not visible.
   * @return (int) -1 if a target is not visible, active priority otherwise
   */
  private int getInactivePriority() {
//    return isTargetVisible() ? priority : 0;
    if ((startTime + delay) < timeSource.getTime() && isTargetVisible()) {
//    if (isTargetVisible() && (startTime + delay) > timeSource.getTime()) {
//      System.out.println("ready to fire while inactive");
      return priority;
    }
    return -1;
//    System.out.println("not ready to fire while inactive");
//    return isTargetVisible() ? priority : -1;
  }

  /**
   * Uses a raycast to determine whether there are any targets in detection range
   * @return true if a target is visible, false otherwise
   */
  private boolean isTargetVisible() {
    Vector2 newVector = new Vector2(owner.getEntity().getPosition().x - 10f, owner.getEntity().getPosition().y - 2f);
    return physics.raycast(owner.getEntity().getPosition(), newVector, TARGET, hit);
  }

  private Fixture meleeOrProjectile() {
//    RaycastHit[] hits = physics.raycastAll(owner.getEntity().getPosition(), new Vector2(0, 0), TARGET);
//    owner.getEntity().getComponent(CombatStatsComponent.class);
    Vector2 newVector = new Vector2(owner.getEntity().getPosition().x - 10f, owner.getEntity().getPosition().y - 2f);
    Fixture hitraycast = physics.raycastGetHit(owner.getEntity().getPosition(), newVector, TARGET, hit);
    System.out.println("hit fixture if worked is: " + hitraycast);
    return hitraycast;
  }
}
