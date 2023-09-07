package com.csse3200.game.entities.factories;

import com.csse3200.game.components.EffectsComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.TrajectTask;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Responsible for creating projectiles within the game.
 */
public class ProjectileFactory {

  public enum ProjectileEffects {
    FIREBALL, //fireball projectile - deals damage based on baseAttack
    BURN, //burn projectile - does 5 extra ticks of damage over 5 seconds
    SLOW, //slow projectile - slows entity by half for 5 seconds
    STUN //stun projectile - stuns entity for 5 seconds
  }

  private static final NPCConfigs configs = 
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /**
   * Creates a single-targeting projectile with specified effect
   *
   * @param targetLayer The enemy layer that the projectile collides with.
   * @param destination The destination the projectile heads towards.
   * @param speed The speed of the projectile.
   * @param effect Specified effect from the ProjectileEffects enums
   * @return Returns a new single-target projectile entity
   */
  public static Entity createEffectProjectile(short targetLayer, Vector2 destination, Vector2 speed,
                                              ProjectileEffects effect, boolean aoe) {
    BaseEntityConfig config = configs.fireBall;
    Entity projectile = createFireBall(targetLayer, destination, speed);

    switch(effect) {
      case FIREBALL -> {
        projectile.addComponent(new EffectsComponent(targetLayer, 3, ProjectileEffects.FIREBALL, aoe));
      }
      case BURN -> {
        projectile.addComponent(new EffectsComponent(targetLayer, 3, ProjectileEffects.BURN, aoe));
      }
      case SLOW -> {
        projectile.addComponent(new EffectsComponent(targetLayer, 3, ProjectileEffects.SLOW, aoe));
      }
      case STUN -> {
        projectile.addComponent(new EffectsComponent(targetLayer, 3, ProjectileEffects.STUN, aoe));
      }
    }
      return projectile;
  }

  /**
   * Creates a fireball Entity.
   * 
   * @param targetLayer The enemy layer that the projectile collides with.
   * @param destination The destination the projectile heads towards.
   * @param speed The speed of the projectile.
   * @return Returns a new fireball projectile entity.
   */
  public static Entity createFireBall(short targetLayer, Vector2 destination, Vector2 speed) {
    BaseEntityConfig config = configs.fireBall;

    Entity projectile = createBaseProjectile(destination);

    projectile
        .addComponent(new TextureRenderComponent("images/projectiles/projectile.png"))
        .addComponent(new ColliderComponent().setSensor(true))

        // This is the component that allows the projectile to damage a specified target.
        .addComponent(new TouchAttackComponent(targetLayer, 1.5f, true))
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack));

    projectile
        .getComponent(TextureRenderComponent.class).scaleEntity();
    
    projectile
        .getComponent(PhysicsMovementComponent.class).setSpeed(speed);

    return projectile;
  }

  /**
   * Creates a generic projectile entity that can be used for multiple types of * projectiles.
   * 
   * @param destination The destination the projectile heads towards.
   * @return Returns a generic projectile entity.
   */
  public static Entity createBaseProjectile(Vector2 destination) {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new TrajectTask(destination));
    
    Entity projectile = new Entity()
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
        .addComponent(aiComponent);

    return projectile;
  }

  /**
   * Sets the projectile collider so that the collider size can be altered for flexibility.
   * @param projectile Projectile's size collider to be scaled upon.
   * @param x Horizontal scaling of the collider in respect ot the size of the entity
   * @param y Veritcal scaling of the collider in respect to the size of the entity
   */
  public static void setColliderSize(Entity projectile, float x, float y) {
    PhysicsUtils.setScaledCollider(projectile, x, y);
  }

  /**
   * Prevents the creation of a Projectile Factory entity being created
   */
  private ProjectileFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
