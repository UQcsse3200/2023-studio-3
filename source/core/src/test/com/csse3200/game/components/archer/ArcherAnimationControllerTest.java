package com.csse3200.game.components.archer;


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
public class ArcherAnimationControllerTest {

    private Entity arcaneArcher;
    private final String[] atlas = {"images/mobs/arcane_archer.atlas"};

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

        arcaneArcher = NPCFactory.createDodgingArcaneArcher(60); // Replace with actual Droid Tower creation logic
        arcaneArcher.create();
    }

    @Test
    public void testAnimateWander() {
        arcaneArcher.getEvents().trigger("mob_walk");
        assertEquals("arcane_archer_run", arcaneArcher.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    public void testAnimateAttack() {
        arcaneArcher.getEvents().trigger("mob_attack");
        assertEquals("arcane_archer_attack", arcaneArcher.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    public void testAnimateDeath() {
        arcaneArcher.getEvents().trigger("mob_death");
        assertEquals("arcane_archer_death", arcaneArcher.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    public void testAnimateDodge() {
        arcaneArcher.getEvents().trigger("mob_dodge");
        assertEquals("arcane_archer_dodge", arcaneArcher.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }
}
