package com.csse3200.game.entities.factories;

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
  public static Entity createProjectile() {
    Entity projectile = new Entity()
        .addComponent(new TextureRenderComponent("images/projectile.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
        .addComponent(new ColliderComponent());

    projectile.getComponent(TextureRenderComponent.class).scaleEntity();

    PhysicsUtils.setScaledCollider(projectile, 0.9f, 0.4f);
    return projectile;
  }

  /**
   * Prevents the creation of a Projectile Factory entity being created
   */
  private ProjectileFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
