package com.csse3200.game.components.tasks.human;

import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EngineerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class HumanWanderTaskTest {
    /**
     * Class for testing the HumanWanderTask, adapted from WanderTaskTest by
     * Jonathan Tang
     */

    Entity owner;

    private final String[] atlas = {"images/engineers/engineer.atlas"};
    private static final String[] sounds = {
            "sounds/engineers/firing_auto.mp3",
            "sounds/engineers/firing_single.mp3"
    };

    @BeforeEach
    void setUp() {
        GameTime gameTime = new GameTime();
        PhysicsService physics = new PhysicsService();
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(physics);
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadSounds(sounds);
        resourceService.loadAll();
        owner = EngineerFactory.createEngineer();
        owner.create();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void start() {

    }

    @Test
    void getPriority() {
    }

    @Test
    void shouldStartWaiting() {

    }

    @Test
    void shouldStartMoving() {

    }

    @Test
    void shouldStartCombat() {

    }

    @Test
    void shouldSwapTask() {

    }

    @Test
    void update() {
    }

    Entity createEnemy() {
        Entity enemy = mock(Entity.class);
        enemy
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new PhysicsComponent())
                .addComponent(new TouchAttackComponent(PhysicsLayer.ENGINEER));
        return enemy;
    }
}