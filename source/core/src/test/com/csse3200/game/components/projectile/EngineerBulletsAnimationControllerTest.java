package com.csse3200.game.components.projectile;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.badlogic.gdx.physics.box2d.Fixture;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


@ExtendWith(GameExtension.class)
public class EngineerBulletsAnimationControllerTest {

    private Entity engineerBullet;
    private final String[] atlas = {"images/projectiles/engineer_projectile.atlas"};

    @BeforeEach
    public void setUp() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadAll();

        engineerBullet = ProjectileFactory.createEngineerBullet(PhysicsLayer.NPC,
                                new Vector2(100, 0),
                                new Vector2(4f, 4f));
                        engineerBullet.setScale(0.8f, 0.8f);
                        engineerBullet.setPosition(0, 0);
        engineerBullet.create();
    }
	
    @Test
    public void testAnimateStart() {
		engineerBullet.getEvents().trigger("startProjectile");
        assertEquals("bullet", engineerBullet.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }
	
    @Test
    public void testAnimateFinal() {
		engineerBullet.getEvents().trigger("startProjectileFinal");
        assertEquals("bulletFinal", engineerBullet.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    public void testAnimateCollide() {
		engineerBullet.getEvents().trigger("collisionStart", null, null);
        assertEquals("bulletCollide", engineerBullet.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }
}
