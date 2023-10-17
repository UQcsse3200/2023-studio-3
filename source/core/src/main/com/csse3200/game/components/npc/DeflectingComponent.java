package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.TrajectTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;

/**
 * A component that deflects incoming projectiles a set amount of projectile
 * collision events.
 * 
 * The target layer to be deflected is flexible, meaning it can deflect other
 * entities as well, including but not limited to it's own target layer.
 * <p>
 * This class assumes the projectile has only a TrajectTask. If there are
 * multiple tasks, the other tasks will be disposed.
 * </p>
 */
public class DeflectingComponent extends Component {
  private short targetLayer;
  private short dmgLayer;
  private HitboxComponent hitboxComponent;
  private int deflectLimitAmount;
  private int maxHealth;

  /**
   * Initialise a Deflecting component that deflects projectile, reversing the
   * incoming direcition of the entity. Any effects, such as damage, will be
   * negated when deflected.
   * 
   * The projectile's damage target layer will also be changed.
   * 
   * @param targetLayer        Target layer to deflect on
   * @param dmgLayer           Target layer for the projectile to hit.
   * @param deflectLimitAmount Amount of deflections to occur for the disabling of
   *                           this component.
   */
  public DeflectingComponent(short targetLayer, short dmgLayer,
      int deflectLimitAmount) {
    this.targetLayer = targetLayer;
    this.dmgLayer = dmgLayer;
    this.deflectLimitAmount = deflectLimitAmount; // Assumes deflect amount > 0.
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionStart", this::deflectProj);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
    maxHealth = entity.getComponent(CombatStatsComponent.class)
        .getMaxHealth();
  }

  @Override
  public void update() {
    super.update();
  }

  /**
   * Deflects projectile to the opposite direction
   * 
   * @param me    Self entity fixture
   * @param other Colliding projectile fixture.
   */
  private void deflectProj(Fixture me, Fixture other) {
    // If self fixture does not match to the colliding target return;
    if (hitboxComponent.getFixture() != me
        || !PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits))
      return;

    if (deflectLimitAmount-- <= 0) { // Reached deflect limit amt, return.
      entity.getComponent(this.getClass()).setEnabled(false);
      return;
    }

    // Obtain projectile entity.
    Entity projectile = ((BodyUserData) other.getBody().getUserData()).entity;

    // Disposes all tasks for the curr projectile. At this curr time, it assumes
    // projectile only has one significant task, and that is the TrajectTask.
    projectile.getComponent(AITaskComponent.class).dispose(); // stop task
    projectile.getComponent(AITaskComponent.class).disposeAll();

    // Obtain current direction of projectile
    Vector2 direction = projectile.getComponent(
        PhysicsMovementComponent.class).getTarget();

    // Rare occurence that the direction is null if target isn't set.
    if (direction == null)
      return;

    // // Add new traject task with the target in the opposite x-direction.
    projectile.getComponent(AITaskComponent.class)
        .addTask(new TrajectTask(new Vector2(-direction.x, direction.y)));

    // Reverse visual image of projectile
    Vector2 scale = projectile.getScale();
    projectile.setScale(-scale.x, scale.y);

    // Change target layer of projectile.
    projectile.getComponent(TouchAttackComponent.class)
        .setTargetLayer(dmgLayer);
    entity.getEvents().trigger("shootStart");

    // Make sure projectile is not deleted in the next frame.
    projectile.setFlagForDelete(false);

    // Reset health
    resetHealth();
  }

  /**
   * Reverses the entity's health ater each subsequent collisions.
   */
  private void resetHealth() {
    entity.getComponent(CombatStatsComponent.class).setHealth(maxHealth);
  }
}
