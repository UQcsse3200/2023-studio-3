package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NPCFactoryTest {

    private Entity xenoGrunt;
    private Entity towerTarget;
    private Entity engineerTarget;
    private String[] atlas = {"images/mobs/xenoGrunt.atlas"};


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
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadAll();
        ServiceLocator.getResourceService()
                .getAsset("images/towers/turret01.atlas", TextureAtlas.class);
        towerTarget = TowerFactory.createBaseTower();
        engineerTarget = EngineerFactory.createEngineer();
        xenoGrunt = NPCFactory.createXenoGrunt(towerTarget);
    }

    @Test
    public void testCreateXenoGruntNotNull() {
        assertNotNull(xenoGrunt, "Xeno Grunt should not be null");
    }

    @Test
    public void testCreateXenoGruntHasColliderComponent() {
        assertNotNull(xenoGrunt.getComponent(ColliderComponent.class),
                "Xeno Grunt should have ColliderComponent");
    }

    @Test
    public void testCreateXenoGruntHasHitboxComponent() {
        assertNotNull(xenoGrunt.getComponent(HitboxComponent.class),
                "Xeno Grunt should have HitboxComponent");
    }

}
