package com.csse3200.game.services;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.WaveTask;
import com.csse3200.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;

import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@Disabled
@ExtendWith(GameExtension.class)
public class WaveServiceTest {

    WaveTask waveTask;
    ResourceService resourceService;
    LevelWaves level;

    WaveService waveService;
    @BeforeEach
    void setUp() {
        resourceService = ServiceLocator.getResourceService();
        waveService = ServiceLocator.getWaveService();
        GameTime globalTime = mock(GameTime.class);
        level = mock(LevelWaves.class);
        ServiceLocator.registerTimeSource(globalTime);
        waveTask = new WaveTask();
        String[] sounds = waveTask.getSounds();
        resourceService.getAsset(sounds[0], Sound.class);
        resourceService.getAsset(sounds[1], Sound.class);
    }

    @Test
    void shouldSetNextWaveTime() {

        waveTask.start();
        ServiceLocator.getWaveService().setEnemyCount(0);
        waveTask.update();

        assertTrue(ServiceLocator.getWaveService().getNextWaveTime() > 0);

    }

}
