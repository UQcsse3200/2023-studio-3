package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.tasks.DroidCombatTask;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Level;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WaveTaskTest {

    WaveTask waveTask;
    ResourceService resourceService;
    LevelWaves level;
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
    public void testStartWave() {
        WaveTask waveTask = new WaveTask();
        waveTask.start();
        assertEquals(1, waveTask.getPriority());
    }

}