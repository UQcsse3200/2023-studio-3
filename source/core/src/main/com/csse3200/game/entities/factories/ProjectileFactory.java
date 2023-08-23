package com.csse3200.game.entities.factories;

import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.components.tasks.TrajectTask;
import com.csse3200.game.components.tasks.WanderTask;
import com.badlogic.gdx.math.Vector2;
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

/**
 * Responsible for creating projectiles within the game
 */
public class ProjectileFactory {

  private static final NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/NPCS.json");

  public static Entity createProjectile(Entity shooter, Entity target) {
    BaseEntityConfig config = configs.projectile;

    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new TrajectTask(shooter, target,10,100f, 100f));

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
