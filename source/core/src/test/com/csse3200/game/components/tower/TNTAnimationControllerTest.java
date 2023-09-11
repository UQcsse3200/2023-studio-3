package com.csse3200.game.components.tower;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TowerFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;



@ExtendWith(GameExtension.class)
public class TNTAnimationControllerTest {

    private Entity mockEntity;
    private final String[] texture = {"images/towers/TNTTower.png"};
    private final String[] atlas = {"images/towers/TNTTower.atlas"};


    @BeforeEach
    public void setUp() {
        // Initialize the TNTAnimationController object
        ServiceLocator.registerPhysicsService(new PhysicsService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadTextures(texture);
        resourceService.loadTextureAtlases(atlas);
        resourceService.loadAll();

      mockEntity = TowerFactory.createTNTTower();
      mockEntity.create();
    }

    @Test
    public void testAnimateDig() {

        // Trigger the animateDig method
        mockEntity.getEvents().trigger("digStart");

        // Verify if the "dig" animation was started
        assertEquals("dig",mockEntity.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    public void testAnimateDefault() {
        // Trigger the animateDefault method
        mockEntity.getEvents().trigger("defaultStart");

        // Verify if the "default" animation was started
        assertEquals("default",mockEntity.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }

    @Test
    public void testAnimateExplode() {
        // Trigger the animateExplode method
        mockEntity.getEvents().trigger("explodeStart");

        // Verify if the "explode" animation was started
        assertEquals("explode",mockEntity.getComponent(AnimationRenderComponent.class).getCurrentAnimation());
    }
}
