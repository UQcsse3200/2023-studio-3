package com.csse3200.game.components.player;

import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EngineerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class HumanAnimationControllerTest {
    private final String[] atlas = {"images/engineers/engineer.atlas"};
    private static final String[] sounds = {
            "sounds/engineers/firing_auto.mp3",
            "sounds/engineers/firing_single.mp3"
    };

    private final String[] animations = {
            "idle_right",
            "walk_left",
            "walk_right",
            "walk_prep",
            "prep",
            "firing_auto",
            "firing_single",
            "hit",
            "death"
    };

    private Entity engineer;

    @Mock
    GameTime gameTime;

    @BeforeEach
    void setUp() {
        gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadSounds(sounds);
        resourceService.loadAll();
        engineer = EngineerFactory.createEngineer();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldHaveAnimationController() {
        assertNotNull(engineer.getComponent(HumanAnimationController.class),
                "Created Engineer entity should have a HumanAnimationController");
    }

//    @Test
//    void shouldAnimateIdleRight() {
//        engineer.getEvents().trigger("idleStart");
//        when(gameTime.getDeltaTime()).thenReturn(0.1f);
//        assertEquals("idle_right", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
//                "'idleStart' event should trigger 'idle_right' animation'");
//    }
//
//    @Test
//    void animateLeftWalk() {
//        engineer.getEvents().trigger("walkLeftStart");
//        assertEquals("walk_left", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
//                "'walkLeftStart' event should trigger 'walk_left' animation'");
//    }
//
//    @Test
//    void animateRightWalk() {
//        engineer.getEvents().trigger("walkRightStart");
//        assertEquals("walk_right", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
//                "'walkRightStart' event should trigger 'walk_right' animation'");
//    }
//
//    @Test
//    void animateFiring() {
//        engineer.getEvents().trigger("firingSingleStart");
//        assertEquals("firing_single", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
//                "'firingSingleStart' event should trigger 'firing_single' animation'");
//    }
//
//    @Test
//    void animateHit() {
//        engineer.getEvents().trigger("hitStart");
//        assertEquals("hit", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
//                "'hitStart' event should trigger 'hit' animation'");
//    }
//
//    @Test
//    void animateDeath() {
//        engineer.getEvents().trigger("hitStart");
//        assertEquals("death", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
//                "'deathStart' event should trigger 'death' animation'");
//    }
}