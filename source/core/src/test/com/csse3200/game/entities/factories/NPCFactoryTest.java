package com.csse3200.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class NPCFactoryTest {

    private Entity xenoGrunt;
    private Entity towerTarget;
    private Entity engineerTarget;
    private Entity playerTarget;

    private final String[] texture = {
            "images/towers/turret_deployed.png",
            "images/towers/turret01.png",
            "images/towers/WallTower.png"
    };
    private final String[] atlas = {"images/towers/turret01.atlas",
            "images/mobs/xenoGrunt.atlas"};
    private static final String[] sounds = {
            "sounds/towers/gun_shot_trimmed.mp3",
            "sounds/towers/deploy.mp3",
            "sounds/towers/stow.mp3"
    };


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
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadSounds(sounds);
        resourceService.loadAll();
        ServiceLocator.getResourceService()
                .getAsset("images/mobs/xenoGrunt.atlas", TextureAtlas.class);
        //playerTarget = PlayerFactory.createPlayer();
        //towerTarget = TowerFactory.createBaseTower();
        //engineerTarget = EngineerFactory.createEngineer();
        xenoGrunt = NPCFactory.createXenoGrunt();
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

    @Test
    public void testCreateXenoGruntHasPhysicsComponent() {
        assertNotNull(xenoGrunt.getComponent(PhysicsComponent.class),
                "Xeno Grunt should have PhysicsComponent");
    }

    @Test
    public void testXenoGruntCombatStatsComponent() {
        assertEquals(100, xenoGrunt.getComponent(CombatStatsComponent.class).getHealth(),
                "Health should be 100");
        assertEquals(10, xenoGrunt.getComponent(CombatStatsComponent.class).getBaseAttack(),
                "BaseAttack should be 10");
    }

    @Test
    public void xenoGruntHasAnimationComponent() {
        assertNotNull(xenoGrunt.getComponent(AnimationRenderComponent.class),
                "Xeno Grunt should have AnimationRenderComponent");
    }

    @Test
    public void testCreateXenoGruntHasPhysicsMovementComponent() {
        assertNotNull(xenoGrunt.getComponent(PhysicsMovementComponent.class),
                "Xeno Grunt should have PhysicsMovementComponent");
    }

}
