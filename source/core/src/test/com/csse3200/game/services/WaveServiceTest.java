package com.csse3200.game.services;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.rendering.DebugRenderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class WaveServiceTest {

    @BeforeEach
    void setUp() {
        GameTime gameTime = mock(GameTime.class);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
        RenderService render = new RenderService();
        render.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(render);
        ResourceService resourceService = mock(ResourceService.class);
        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerWaveService(new WaveService());
    }

    @Test
    void testSetWaveCount() {
        // setting the number of waves to be 5 will give you an index of 6 because the wave number
        // goes from 1-6 instead of 0-5. Because players like to see "starting at level 1 not 0.
        ServiceLocator.getWaveService().setWaveCount(5);
        assertEquals(6, ServiceLocator.getWaveService().getWaveCount());
    }

    @Test
    public void testSetLevelCompleted() {
        ServiceLocator.getWaveService().setLevelCompleted();
        assertTrue(ServiceLocator.getWaveService().isLevelCompleted());
    }

    @Test
    void testSetNextLane() {
        ServiceLocator.getWaveService().setNextLane(2);
        assertEquals(2, ServiceLocator.getWaveService().getNextLane());
    }

    @Test
    void testToggleDelay() {
        ServiceLocator.getWaveService().toggleDelay();
        assertTrue(ServiceLocator.getWaveService().shouldSkip());
    }

    @Test
    void testSetTotalMobs() {
        ServiceLocator.getWaveService().setTotalMobs(100);
        assertEquals(100, ServiceLocator.getWaveService().totalMobs());
        assertEquals(100, ServiceLocator.getWaveService().remainingMobsForLevel());
    }

    @Test
    void testToggleGamePaused() {
        // the toggle game paused method should flip the
        // gamePaused variable
        // This is checked with an assertEquals test.
        boolean gamePaused = ServiceLocator.getWaveService().getGamePaused();
        ServiceLocator.getWaveService().toggleGamePause();
        assertEquals(!gamePaused, ServiceLocator.getWaveService().getGamePaused());
    }


}
