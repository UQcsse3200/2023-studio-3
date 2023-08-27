package com.csse3200.game.entities.factories;

import com.csse3200.game.components.AoeComponent;
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
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.compression.lzma.Base;

/**
 * Responsible for creating projectiles within the game
 */
public class ProjectileFactory {

  private static final NPCConfigs configs = 
      FileLoader.readClass(NPCConfigs.class, "configs/NPCS.json");

  /**
   * Creates a fireball Entity.
   * @param target The enemy entities that the projectile collides with.
   * @param destination Direction the projectile needs to go towards.
   * @param speed Speed of the projectile.
   * @return Returns the new projectile entity.
   */
  public static Entity createFireBall(Entity target, Vector2 destination, Vector2 speed) {
    BaseEntityConfig config = configs.fireBall;

    Entity projectile = createBaseProjectile(target, destination);

    projectile
        .addComponent(new TextureRenderComponent("images/projectile.png"))
        .addComponent(new ColliderComponent().setSensor(true))

        // * This is the component that allows the projectile to damage a specified target.
        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f, true))
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack));

    projectile
        .getComponent(TextureRenderComponent.class).scaleEntity();
    
    projectile
        .getComponent(PhysicsMovementComponent.class).setSpeed(speed);

    return projectile;
  }

  /**
   * Creates a AOE fireball Entity.
   * @param target The enemy entities that the projectile collides with.
   * @param destination Direction the projectile needs to go towards.
   * @param speed Speed of the projectile.
   * @return Returns the new projectile entity.
   */
  public static Entity createAOEFireBall(Entity target, Vector2 destination, Vector2 speed, int aoeSize) {
    BaseEntityConfig config = configs.fireBall;
    Entity projectile = createFireBall(target, destination, speed);
    projectile
            .addComponent(new TextureRenderComponent("images/aoe_projectile.png"))

            // * This is the component that allows the projectile to damage a specified target.
            .addComponent(new AoeComponent(aoeSize));

    return projectile;
  }

  /**
   * Creates a generic projectile entity that can be used for multiple types of projectiles.
   * @param target The enemy entities that the projectile collides with.
   * @param destination Direction the projectile needs to go towards.
   * @return
   */
  private static Entity createBaseProjectile(Entity target, Vector2 destination) {
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new TrajectTask(destination));
    
    Entity projectile = new Entity()
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
        .addComponent(aiComponent);


    // Able to alter the collider component's size in proportion to the Entity's size.
    return projectile;
  }

  /**
   * Prevents the creation of a Projectile Factory entity being created
   */
  private ProjectileFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
