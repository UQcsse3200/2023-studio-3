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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


@ExtendWith(GameExtension.class)
public class XenoAnimationControllerTest {

    private Entity xenoGrunt;
    private Entity target;
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

        xenoGrunt = NPCFactory.createXenoGrunt(target); // Replace with actual Droid Tower creation logic
        xenoGrunt.create();
    }

    @Test
    public void testAnimateWander() {
        xenoGrunt.getEvents().trigger("wanderStart");
        assertEquals("xeno_run", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    public void testAnimateHurt() {
        xenoGrunt.getEvents().trigger("runHurt");
        assertEquals("xeno_hurt", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    public void testAnimateMelee1() {
        xenoGrunt.getEvents().trigger("meleeStart");
        assertEquals("xeno_melee_1", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

//    @Test
//    public void testAnimateMelee2() {
//        xenoGrunt.getEvents().trigger("meleeStart");
//        assertEquals("xeno_melee_2", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
//    }

    @Test
    public void testAnimateDie() {
        xenoGrunt.getEvents().trigger("dieStart");
        assertEquals("xeno_die", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

//    @Test
//    public void testAnimateStop() {
//        xenoGrunt.getEvents().trigger("stop");
//        assertEquals("default", xenoGrunt.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
//    }
}
