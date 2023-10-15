package com.csse3200.game.components.xenos;


import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


@ExtendWith(GameExtension.class)
class XenoAnimationControllerTest {

    private Entity xenoGrunt;
    private final String[] atlas = {"images/mobs/xenoGrunt.atlas"};

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

        xenoGrunt = NPCFactory.createXenoGrunt(60); // Replace with actual Droid Tower creation logic
        xenoGrunt.create();
    }

    @Test
    void testAnimateWander() {
        xenoGrunt.getEvents().trigger("wanderStart");
        assertEquals("xeno_run", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    void testAnimateHurt() {
        xenoGrunt.getEvents().trigger("runHurt");
        assertEquals("xeno_hurt", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    void testAnimateMelee1() {
        //TODO: Add at least one assertion to this test case.
        xenoGrunt.getEvents().trigger("meleeStart");
        assert(Objects.equals(xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation(), "xeno_melee_1")
                || Objects.equals(xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation(), "xeno_melee_2"));
    }

    @Test
    void testAnimateDie() {
        xenoGrunt.getEvents().trigger("dieStart");
        assertEquals("xeno_die", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

}
