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
        engineer.create();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldHaveAnimationController() {
        assertNotNull(engineer.getComponent(HumanAnimationController.class),
                "Created Engineer entity should have a HumanAnimationController");
    }

    @Test
    void shouldAnimateIdleRight() {
        engineer.getEvents().trigger("idleRight");
        assertEquals("idle_right", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
                "'idleRight' event should trigger 'idle_right' animation'");
    }

    @Test
    void shouldAnimateLeftWalk() {
        engineer.getEvents().trigger("walkLeftStart");
        assertEquals("walk_left", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
                "'walkLeftStart' event should trigger 'walk_left' animation'");
    }

    @Test
    void shouldAnimateRightWalk() {
        engineer.getEvents().trigger("walkRightStart");
        assertEquals("walk_right", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
                "'walkRightStart' event should trigger 'walk_right' animation'");
    }

    @Test
    void shoudlAnimateFiring() {
        engineer.getEvents().trigger("firingSingleStart");
        assertEquals("firing_single", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
                "'firingSingleStart' event should trigger 'firing_single' animation'");
    }

    @Test
    void shouldAnimateHit() {
        engineer.getEvents().trigger("hitStart");
        assertEquals("hit", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
                "'hitStart' event should trigger 'hit' animation'");
    }

    @Test
    void shouldAnimateDeath() {
        engineer.getEvents().trigger("deathStart");
        assertEquals("death", engineer.getComponent(AnimationRenderComponent.class).getCurrentAnimation(),
                "'deathStart' event should trigger 'death' animation'");
    }

    @Test
    void notSelectedOnStart() {
        assertFalse(engineer.getComponent(HumanAnimationController.class).isClicked(),
                "Engineer should not be selected when initialised");
    }

    @Test
    void shouldSelectEngineer() {
        engineer.getComponent(HumanAnimationController.class).setClicked(true);
        AnimationRenderComponent animator = engineer.getComponent(AnimationRenderComponent.class);

        engineer.getEvents().trigger("idleRight");
        assertEquals("idle_right_outline", animator.getCurrentAnimation(),
                "After selection, outlined animation should be playing - idle right");

        engineer.getEvents().trigger("walkLeftStart");
        assertEquals("walk_left_outline", animator.getCurrentAnimation(),
                "After selection, outlined animation should be playing - walk left");

        engineer.getEvents().trigger("walkRightStart");
        assertEquals("walk_right_outline", animator.getCurrentAnimation(),
                "After selection, outlined animation should be playing - walk right");

        engineer.getEvents().trigger("firingSingleStart");
        assertEquals("firing_single_outline", animator.getCurrentAnimation(),
                "After selection, outlined animation should be playing - firing single");

        engineer.getEvents().trigger("hitStart");
        assertEquals("hit_outline", animator.getCurrentAnimation(),
                "After selection, outlined animation should be playing - hit");

        engineer.getEvents().trigger("deathStart");
        assertEquals("death_outline", animator.getCurrentAnimation(),
                "After selection, outlined animation should be playing - death");
    }
}