package com.csse3200.game.entities.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.screens.GameLevelData;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.WaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.csse3200.game.entities.Entity;

import java.security.Provider;
@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WaveFactoryTest {

    private static final String[] waveSounds = {
            "sounds/waves/wave-start/Wave_Start_Alarm.ogg",
            "sounds/waves/wave-end/Wave_Over_01.ogg"
    };

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
      WaveService waveService = new WaveService();
      ServiceLocator.registerWaveService(waveService);
      ServiceLocator.getResourceService().loadSounds(waveSounds);
    }

    @Test
    void createBaseWaves() {
      GameLevelData.setSelectedLevel(0);
      Entity level1 = WaveFactory.createWaves();
      assertNotNull(level1);

      GameLevelData.setSelectedLevel(1);
      Entity level2 = WaveFactory.createWaves();
      assertNotNull(level2);

      GameLevelData.setSelectedLevel(2);
      Entity level3 = WaveFactory.createWaves();
      assertNotNull(level3);
    }
}
