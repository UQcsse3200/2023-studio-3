package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.tasks.DroidCombatTask;
import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.WaveTask;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Level;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@Disabled
@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WaveTaskTest {

    WaveTask waveTask;
    ResourceService resourceService;
    LevelWaves level;
    ForestGameArea gameArea;
    @BeforeEach
    void setUp() {
        resourceService = ServiceLocator.getResourceService();
        GameTime globalTime = mock(GameTime.class);
        level = mock(LevelWaves.class);
        ServiceLocator.registerTimeSource(globalTime);
        waveTask = new WaveTask();
    }

    @Test
    public void testLoadSounds() {
        String[] sounds = waveTask.getSounds();
        resourceService.getAsset(sounds[0], Sound.class);
        resourceService.getAsset(sounds[1], Sound.class);
    }

    @Test
    public void testGetPriority() {
        int priority = waveTask.getPriority();
        assertEquals(10, priority);
    }

    @Test
    public void testStartFirstWave() {
        waveTask.start();
        assertEquals(10, waveTask.getPriority());
        assertTrue(waveTask.isWaveInProgress());
        assertEquals(1, waveTask.getCurrentWaveIndex());
    }

    @Test
    public void testIsWaveInProgress() {
        waveTask.start();
        assertTrue(waveTask.isWaveInProgress());
    }

    @Test
    public void testUpdateOnEmptyWaveAndNoNextWave() {
        waveTask.start();
        ServiceLocator.getWaveService().setEnemyCount(0);
        waveTask.update();
        assertTrue(ServiceLocator.getWaveService().isLevelCompleted());
    }

    @Test
    public void testUpdateOnEmptyWaveAndNextWave() {
        waveTask.start();
        int waveNumber = waveTask.getCurrentWaveIndex();
        ServiceLocator.getWaveService().setEnemyCount(0);
        waveTask.update();
        assertTrue(waveNumber + 1 == waveTask.getCurrentWaveIndex());
    }

}
