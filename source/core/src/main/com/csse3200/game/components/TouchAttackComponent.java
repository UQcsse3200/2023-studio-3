package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.npc.DeflectingComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.Weapon;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.components.projectile.EngineerBulletsAnimationController;
import com.csse3200.game.components.projectile.ProjectileAnimationController;
import com.csse3200.game.components.projectile.SnowBallProjectileAnimationController;
import com.csse3200.game.components.projectile.StunEffectProjectileAnimationController;
import com.csse3200.game.components.projectile.BurnEffectProjectileAnimationController;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import java.util.Timer;
import java.util.TimerTask;
import com.csse3200.game.components.npc.XenoAnimationController;
import com.csse3200.game.components.npc.DragonKnightAnimationController;
import com.csse3200.game.components.npc.FireWormAnimationController;
import com.csse3200.game.components.npc.SkeletonAnimationController;
import com.csse3200.game.components.npc.WizardAnimationController;
import com.csse3200.game.components.npc.WaterQueenAnimationController;
import com.csse3200.game.components.npc.WaterSlimeAnimationController;
import com.csse3200.game.ai.tasks.AITaskComponent;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and
 * apply a knockback.
 * Has an optional disposeOnHit property that disposes projectile upon
 * collision.
 *
 * <p>
 * Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>
 * Damage is only applied if target entity has a CombatStatsComponent. Knockback
 * is only applied
 * if target entity has a PhysicsComponent.
 */
public class TouchAttackComponent extends Component {
  private short targetLayer;
  private float knockbackForce = 0f;
  private boolean disposeOnHit = false;
  private int aoeSize = 0;
  private CombatStatsComponent combatStats;
  private HitboxComponent hitboxComponent;

  /**
   * Create a component which attacks entities on collision, without knockback.
   * 
   * @param targetLayer The physics layer of the target's collider.
   */
  public TouchAttackComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  /**
   * Create a component which attacks entities on collision, with knockback.
   * 
   * @param targetLayer The physics layer of the target's collider.
   * @param knockback   The magnitude of the knockback applied to the entity.
   */
  public TouchAttackComponent(short targetLayer, float knockback) {
    this.targetLayer = targetLayer;
    this.knockbackForce = knockback;
  }

  /**
   * Create a component which attacks entities on collision, with knockback and
   * self-dispose.
   * 
   * @param targetLayer  The physics layer of the target's collider.
   * @param knockback    The magnitude of the knockback applied to the entity.
   * @param disposeOnHit Whether this entity should be disposed on hit.
   */
  public TouchAttackComponent(short targetLayer, float knockback, boolean disposeOnHit) {
    this.targetLayer = targetLayer;
    this.knockbackForce = knockback;
    this.disposeOnHit = disposeOnHit;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
    combatStats = entity.getComponent(CombatStatsComponent.class);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
  }

  public void onCollisionStart(Fixture me, Fixture other) {
		if (me==null || hitboxComponent==null || hitboxComponent.getFixture() != me) {
		  // Not triggered by hitbox, ignore
		  return;
		}
		if(other!=null)
		{
			if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
			  // Doesn't match our target layer, ignore
			  return;
			}
			Component deflectComponent = ((BodyUserData) other.getBody().getUserData()).entity.getComponent(DeflectingComponent.class);
			if (deflectComponent != null && deflectComponent.enabled)
				return;
		}else
			return;
    // Try to attack target.
    Entity target = ((BodyUserData) other.getBody().getUserData()).entity;

	EngineerBulletsAnimationController engineerBulletsAnimationController;
	ProjectileAnimationController projectileAnimationController;
	SnowBallProjectileAnimationController snowBallProjectileAnimationController;
	StunEffectProjectileAnimationController stunEffectProjectileAnimationController;
	BurnEffectProjectileAnimationController burnEffectProjectileAnimationController;
	if((engineerBulletsAnimationController=entity.getComponent(EngineerBulletsAnimationController.class)) != null)
	{
		setDisposeOnHit(false);
		entity.getComponent(PhysicsMovementComponent.class).setSpeed(new Vector2(0, 0));
		new Timer().schedule(new TimerTask() {
		  public void run() {
			Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;
			projectile.setFlagForDelete(true);
		  }
		}, 300/*START_SPEED*frames*1000ms*/);
	}else
	if((projectileAnimationController=entity.getComponent(ProjectileAnimationController.class)) != null)
	{
		setDisposeOnHit(false);
		entity.getComponent(PhysicsMovementComponent.class).setSpeed(new Vector2(0, 0));
		new Timer().schedule(new TimerTask() {
		  public void run() {
			Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;
			projectile.setFlagForDelete(true);
		  }
		}, 300/*START_SPEED*frames*1000ms*/);
	}else
	if((burnEffectProjectileAnimationController=entity.getComponent(BurnEffectProjectileAnimationController.class)) != null)
	{
		setDisposeOnHit(false);
		entity.getComponent(PhysicsMovementComponent.class).setSpeed(new Vector2(0, 0));
		new Timer().schedule(new TimerTask() {
		  public void run() {
			Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;
			projectile.setFlagForDelete(true);
		  }
		}, 800/*START_SPEED*frames*1000ms*/);
	}else
	if((snowBallProjectileAnimationController=entity.getComponent(SnowBallProjectileAnimationController.class)) != null)
	{
		setDisposeOnHit(false);
		entity.getComponent(PhysicsMovementComponent.class).setSpeed(new Vector2(0, 0));
		new Timer().schedule(new TimerTask() {
		  public void run() {
			Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;
			projectile.setFlagForDelete(true);
		  }
		}, 200/*START_SPEED*frames*1000ms*/);
		
		AITaskComponent aiTaskComponent;
		if((aiTaskComponent=target.getComponent(AITaskComponent.class))!=null)
		{
			aiTaskComponent.freezed = true;
			PhysicsMovementComponent physicsMovementComponent;
			if((physicsMovementComponent=target.getComponent(PhysicsMovementComponent.class))!=null)
				physicsMovementComponent.setMoving(false);
			new Timer().schedule(new TimerTask() {
			  public void run() {
				aiTaskComponent.freezed = false;
				PhysicsMovementComponent physicsMovementComponent;
				if((physicsMovementComponent=target.getComponent(PhysicsMovementComponent.class))!=null)
					physicsMovementComponent.setMoving(true);
				target.getEvents().trigger("wanderStart");
			  }
			}, 5000);
		}
		target.getEvents().trigger("freeze");
		/*else
			if(aiTaskComponent!=null)
				aiTaskComponent.freezed = false;*/
	}else
	if((stunEffectProjectileAnimationController=entity.getComponent(StunEffectProjectileAnimationController.class)) != null)
	{
		setDisposeOnHit(false);
		entity.getComponent(PhysicsMovementComponent.class).setSpeed(new Vector2(0, 0));
		new Timer().schedule(new TimerTask() {
		  public void run() {
			Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;
			projectile.setFlagForDelete(true);
		  }
		}, 900/*START_SPEED*frames*1000ms*/);
		
		AITaskComponent aiTaskComponent;
		if((aiTaskComponent=target.getComponent(AITaskComponent.class))!=null)
		{
			aiTaskComponent.freezed = true;
			PhysicsMovementComponent physicsMovementComponent;
			if((physicsMovementComponent=target.getComponent(PhysicsMovementComponent.class))!=null)
				physicsMovementComponent.setMoving(false);
			new Timer().schedule(new TimerTask() {
			  public void run() {
				aiTaskComponent.freezed = false;
				PhysicsMovementComponent physicsMovementComponent;
				if((physicsMovementComponent=target.getComponent(PhysicsMovementComponent.class))!=null)
					physicsMovementComponent.setMoving(true);
			  }
			}, 1000);
		}
	}
    CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
    if (targetStats != null) {
      // If entity has abilities, pick one at random and apply it else use baseAttack
      // damage
      if (combatStats.getWeapon(target) != null) {
        targetStats.hit(combatStats.getWeapon(target).getDamage());
      } else {
        targetStats.hit(combatStats.getBaseAttack());
      }
    }
    // Apply knockback
    PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
    if (physicsComponent != null && knockbackForce > 0f) {
      Body targetBody = physicsComponent.getBody();
      Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
      Vector2 impulse = direction.setLength(knockbackForce);
      targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
    }

    if (disposeOnHit) {
      Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;
      projectile.setFlagForDelete(true);
    }
  }

  public void setDisposeOnHit(boolean disposeOnHit) {
    this.disposeOnHit = disposeOnHit;
  }

  public void setKnockBack(float knockback) {
    this.knockbackForce = knockback;
  }

  private void onCollisionEnd(Fixture me, Fixture other) {
    // Nothing to do on collision end
    if (hitboxComponent.getFixture() != me) {
      // Not triggered by hitbox, ignore
      return;
    }

    if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
      // Doesn't match our target layer, ignore
      return;
    }

    Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;

    // If enemy has enabled deflection component, don't dispose it.
    if (otherEntity.getComponent(DeflectingComponent.class) != null)
      return;

    if (disposeOnHit) {
      Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;
      projectile.setFlagForDelete(true);
    }
  }

  /**
   * Choose the weapon to use against the given fixture.
   *
   * If the fixture has been removed (died) return null, else return the weapon to use.
   * */
  public Weapon chooseWeapon(Fixture other) {
    if (other == null) {
      return null;
    }
    BodyUserData data = ((BodyUserData) other.getBody().getUserData());
    if (data == null) {
      return null;
    }
    Entity target = data.entity;
    Weapon weapon = null;
    if (target.getComponent(CombatStatsComponent.class) != null) {
      weapon = combatStats.getWeapon(target);
    }
    return weapon;
  }

  /**
   * Sets the target layer of this component, changing which entity to "attack"
   * and/or apply knockback to.
   * 
   * @param targetLayer
   */
  public void setTargetLayer(short targetLayer) {
    this.targetLayer = targetLayer;
  }
}
