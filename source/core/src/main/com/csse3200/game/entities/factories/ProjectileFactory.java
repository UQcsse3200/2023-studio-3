package com.csse3200.game.entities.factories;

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

/**
 * Responsible for creating projectiles within the game
 */
public class ProjectileFactory {

  private static final NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/NPCS.json");

  /**
   * Creates a projectile Entity.
   * @param target The enemy entities that the projectile collides with.
   * @param destination Direction the projectile needs to go towards.
   * @param speed Speed of the projectile.
   * @return Returns the new projectile entity.
   */
  public static Entity createProjectile(Entity target, Vector2 destination, Vector2 speed) {
    BaseEntityConfig config = configs.projectile;

    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new TrajectTask(destination));

    Entity projectile = new Entity()
        .addComponent(new TextureRenderComponent("images/projectile.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
        .addComponent(new ColliderComponent())
        .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack))
        .addComponent(aiComponent);

    projectile.getComponent(TextureRenderComponent.class).scaleEntity();
    projectile.getComponent(PhysicsMovementComponent.class).setSpeed(speed);


    // Able to alter the collider component's size in proportion to the Entity's size.
    // PhysicsUtils.setScaledCollider(projectile, 0.9f, 0.4f);
    return projectile;
  }

  /**
   * Prevents the creation of a Projectile Factory entity being created
   */
  private ProjectileFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
