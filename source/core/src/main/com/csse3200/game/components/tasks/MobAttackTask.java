package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Melee;
import com.csse3200.game.entities.Weapon;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Task that allows mobs to shoot projectiles or melee attack towers
 */
public class MobAttackTask extends DefaultTask implements PriorityTask {
  private static final int INTERVAL = 1; // time interval to scan for towers in
  private static final short TARGET_LAYER = PhysicsLayer.HUMANS; // mobs detecting for towers
  // ^ fix this

  private static final String STOW = "wanderStart";
  private static final String DEPLOY = "deployStart";
  private static final String FIRING = "shootStart";
  private static final String IDLE = "stop";

  private Fixture target;

  private final int priority;
  private final Vector2 maxRangePosition = new Vector2();
  private final PhysicsEngine physics;
  private GameTime timeSource;

  private final RaycastHit hit = new RaycastHit();

  private static final long DELAY = 1000; // delay between shots
  private long startTime;

  private enum STATE {
    IDLE, DEPLOY, FIRING, STOW
  }

  private STATE mobState = STATE.IDLE;

  /**
   * @param priority Task priority when targets are detected (0 when nothing detected). Must be a positive integer.
   */
  public MobAttackTask(int priority) {
    this.priority = priority;
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
    Vector2 mobPosition = owner.getEntity().getCenterPosition();
    this.maxRangePosition.set(0, mobPosition.y);
    long endTime = timeSource.getTime() + (INTERVAL * 500);
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
    switch (mobState) {

      case IDLE -> {
        if (isTargetVisible()) {
          // targets detected in idle mode - start deployment
          owner.getEntity().getEvents().trigger(DEPLOY);
          mobState = STATE.DEPLOY;
        }
      }

      case DEPLOY -> {
        // currently deploying,
        if (isTargetVisible() || this.meleeOrProjectile() != null) {
          owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(false);
          this.owner.getEntity().getEvents().trigger(FIRING);
          mobState = STATE.FIRING;
        } else {
          this.owner.getEntity().getEvents().trigger(STOW);
          mobState = STATE.STOW;
        }
      }

      case FIRING -> {
        // targets gone or cannot be attacked - stop firing
        if (!isTargetVisible() || this.meleeOrProjectile() == null) {
          this.owner.getEntity().getEvents().trigger(STOW);
          mobState = STATE.STOW;
        } else if (this.meleeOrProjectile() instanceof Melee) {
            TouchAttackComponent attackComp = owner.getEntity().getComponent(TouchAttackComponent.class);
            HitboxComponent hitboxComp = owner.getEntity().getComponent(HitboxComponent.class);
            attackComp.onCollisionStart(hitboxComp.getFixture(), target);
            this.owner.getEntity().getEvents().trigger("meleeStart");
        } else {
            Entity newProjectile = ProjectileFactory.createMobBall(PhysicsLayer.HUMANS, new Vector2(0, owner.getEntity().getPosition().y), new Vector2(2f,2f));
            newProjectile.setPosition(owner.getEntity().getPosition().x, owner.getEntity().getPosition().y);
            newProjectile.setScale(-1f, 1f);
            ServiceLocator.getEntityService().register(newProjectile);

            this.owner.getEntity().getEvents().trigger(FIRING);
            mobState = STATE.STOW;
        }
        owner.getEntity().getComponent(PhysicsMovementComponent.class).setEnabled(true);
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
    if ((startTime + DELAY) < timeSource.getTime() && isTargetVisible() && this.meleeOrProjectile() != null) {
      return priority;
    }
    return -1;
  }

  /**
   * Uses a raycast to determine whether there are any targets in detection range.
   * 
   * @return true if a target is visible, false otherwise
   */
  private boolean isTargetVisible() {
    Vector2 newVector = new Vector2(owner.getEntity().getPosition().x - 10f, owner.getEntity().getPosition().y - 2f);
    return physics.raycast(owner.getEntity().getPosition(), newVector, TARGET_LAYER, hit);
  }

  /**
   * Uses a custom raycast method to find the closest target to the mob. Based on the distance to the
   * target, the mob will choose a weapon to attack with.
   *
   * If the object does not have a CombatStatsComponent (which handles dealing damage etc), then
   * the function will return null. If it returns null when the mob is in state FIRING or DEPLOY, it will not fire
   * and will STOW.
   *
   * @return the Weapon (Melee or Projectile) the mob will use to attack the target. null if immune target or no target
   * */
  private Weapon meleeOrProjectile() {

    setTarget();
    TouchAttackComponent comp = owner.getEntity().getComponent(TouchAttackComponent.class);
    Weapon chosenWeapon = null;
    if (comp != null) {
      chosenWeapon = comp.chooseWeapon(target);
    }

    return chosenWeapon;
  }

  private void setTarget() {
    Vector2 newVector = new Vector2(owner.getEntity().getPosition().x - 10f, owner.getEntity().getPosition().y - 2f);
    target = physics.raycastGetHit(owner.getEntity().getPosition(), newVector, TARGET_LAYER);
  }
}
