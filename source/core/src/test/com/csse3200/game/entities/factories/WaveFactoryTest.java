package com.csse3200.game.entities.factories;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.screens.GameLevelData;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.csse3200.game.entities.Entity;
@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WaveFactoryTest {

    @BeforeEach
    void setUp() {
      // TODO this is not set up right and i am not sure what services tasks are linked etc
//      GameTime gameTime = mock(GameTime.class);
//      when(gameTime.getDeltaTime()).thenReturn(0.02f);
//      ServiceLocator.registerTimeSource(gameTime);
//      ServiceLocator.registerPhysicsService(new PhysicsService());
//      RenderService render = new RenderService();
//      render.setDebug(mock(DebugRenderer.class));
//      ServiceLocator.registerRenderService(render);
//      ResourceService resourceService = new ResourceService();
//      ServiceLocator.registerResourceService(resourceService);
    }

    @Test
    void createBaseWaves() {
//      GameLevelData.setSelectedLevel(0);
//      Entity level1 = WaveFactory.createWaves();
//      assertNotNull(level1);
//
//      GameLevelData.setSelectedLevel(1);
//      Entity level2 = WaveFactory.createWaves();
//      assertNotNull(level2);
//
//      GameLevelData.setSelectedLevel(2);
//      Entity level3 = WaveFactory.createWaves();
//      assertNotNull(level3);
    }
}
