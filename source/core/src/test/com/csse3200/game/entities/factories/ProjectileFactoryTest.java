package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ProjectileFactoryTest {
    private String[] texture = {"images/projectiles/projectile.png"};
    private Entity projectile;

    @BeforeEach
    public void setUp() {
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getDeltaTime()).thenReturn(0.02f);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(texture);
        resourceService.loadAll();
        ServiceLocator.getResourceService()
                .getAsset("images/projectiles/projectile.png", Texture.class);
        Vector2 destination = new Vector2(0.1f, 0.1f);
        Vector2 speed = new Vector2(0.2f, 0.2f);

        projectile = ProjectileFactory.createBaseProjectile(new Entity(), destination);
    }

    @Test
    public void testBaseProjectileNotNull() {
        assertNotNull(projectile, "Base projectile is null");
    }

    @Test
    public void testBaseProjectileHitbox() {
        assertNotNull(projectile.getComponent(HitboxComponent.class),
                "Projectile does not contain Hotbox component");
    }

    @Test
    public void testBaseProjectilePhysics() {
        assertNotNull(projectile.getComponent(PhysicsComponent.class),
                "Projectile does not have Physics component");
    }

    @Test
    public void testBaseProjectilePhysicsMovement() {
        assertNotNull(projectile.getComponent(PhysicsMovementComponent.class),
                "Projectile does not have PhysicsMovement component");
    }
}
