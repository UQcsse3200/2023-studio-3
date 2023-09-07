package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.AoeComponent;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.TrajectTask;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.configs.BaseEntityConfig;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.projectile.ProjectileAnimationController;
import com.csse3200.game.services.ServiceLocator;

/**
 * Responsible for creating projectiles within the game.
 */
public class ProjectileFactory {
  /** Animation constants */
  private static final String BASE_PROJECTILE_ATLAS = "images/projectiles/basic_projectile.atlas";
  private static final String START_ANIM = "projectile";
  private static final String FINAL_ANIM = "projectileFinal";
  private static final float START_SPEED = 0.1f;
  private static final float FINAL_SPEED = 0.1f;

  private static final NPCConfigs configs = 
      FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");

  /**
   * Creates a fireball Entity.
   * 
   * @param target The enemy entities that the projectile collides with.
   * @param destination The destination the projectile heads towards.
   * @param speed The speed of the projectile.
   * @return Returns a new fireball projectile entity.
   */
  public static Entity createFireBall(Entity target, Vector2 destination, Vector2 speed) {
    BaseEntityConfig config = configs.fireBall;

    Entity projectile = createBaseProjectile(target, destination);

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService()
                            .getAsset(BASE_PROJECTILE_ATLAS, TextureAtlas.class));
    animator.addAnimation(START_ANIM, START_SPEED, Animation.PlayMode.NORMAL);
    animator.addAnimation(FINAL_ANIM, FINAL_SPEED, Animation.PlayMode.NORMAL);

    projectile
        .addComponent(new ProjectileAnimationController())
        .addComponent(new ColliderComponent().setSensor(true))
            .addComponent(animator)

        // This is the component that allows the projectile to damage a specified target.
        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f, true))
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack));

//    projectile
//        .getComponent(TextureRenderComponent.class).scaleEntity();
    
    projectile
        .getComponent(PhysicsMovementComponent.class).setSpeed(speed);


    return projectile;
  }

  /**
   * Creates an AOE fireball Entity.
   * 
   * @param target The enemy entities that the projectile collides with.
   * @param destination The destination the projectile heads towards.
   * @param speed The speed of the projectile.
   * @param aoeSize The size of the AOE.
   * @return Returns the new aoe projectile entity.
   */
  public static Entity createAOEFireBall(Entity target, Vector2 destination, Vector2 speed, int aoeSize) {
    BaseEntityConfig config = configs.fireBall;
    Entity projectile = createFireBall(target, destination, speed);
    projectile
            // This is the component that allows the projectile to damage a specified target.
            .addComponent(new AoeComponent(aoeSize));

    return projectile;
  }

  /**
   * Creates a generic projectile entity that can be used for multiple types of * projectiles.
   * 
   * @param target The enemy entities that the projectile collides with.
   * @param destination The destination the projectile heads towards.
   * @return Returns a generic projectile entity.
   */
  public static Entity createBaseProjectile(Entity target, Vector2 destination) {
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
