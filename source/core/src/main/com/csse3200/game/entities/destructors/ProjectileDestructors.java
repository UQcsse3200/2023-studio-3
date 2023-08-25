package com.csse3200.game.entities.destructors;

import com.csse3200.game.entities.Entity;

/**
 * Responsible for destroying projectiles within the game
 */
public class ProjectileDestructors {

  /**
   * Destroys the projectile entity.
   * 
   * @param projectile The projectile entity that needs to be destroyed.
   * @return Destroyed projectile entity.
   */
  public static Entity destroyProjectile(Entity projectile) {
    projectile.dispose();
    return projectile;
  }
  /**
   * Prevents the creation of a ProjectileDestructor class from being instantiated.
   */
  private ProjectileDestructors() {
    throw new IllegalStateException("Instantiating static util class");
  }
}