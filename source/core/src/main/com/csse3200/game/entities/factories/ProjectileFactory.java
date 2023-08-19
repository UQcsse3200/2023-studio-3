package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Responsible for creating projectiles within the game
 */
public class ProjectileFactory {
    public static Entity createProjectile() {
        Entity projectile = 
            new Entity().addComponent(new TextureRenderComponent("images/projectile.png"));
        
        projectile.getComponent(TextureRenderComponent.class).scaleEntity();
        return projectile;
    }

    /**
     * Prevents the creation of a Projectile Factory entity being created
     */
    private ProjectileFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
