package com.csse3200.game.components.tasks.waves;

import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.WaveClass;
import com.csse3200.game.entities.factories.WaveFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

@Disabled

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class LevelWavesTest {

    LevelWaves levelWaves;
    WaveClass wave;
    ServiceLocator serviceLocator;
    GameTime gameTime;

    @BeforeEach
    void setUp() {
        levelWaves = mock(LevelWaves.class);
        wave =  mock(WaveClass.class);
        gameTime = mock(GameTime.class);
    }

    @Test
    public void testAddWave() {
        when(levelWaves.getNumWaves()).thenReturn(1);
        int size = levelWaves.getNumWaves();
        levelWaves.addWave(wave);
        assertEquals(size + 1, levelWaves.getNumWaves());
    }

    @Test
    public void testSpawnWave() {
        levelWaves.setWaveIndex(1);
        levelWaves.spawnWave();
        WaveClass thisWave = levelWaves.getWave(levelWaves.getWaveIndex());
        assertTrue(thisWave.getSize() > 0);
        assertTrue(!thisWave.getMobs().isEmpty());
    }

    @Test
    public void testGetStartTime() {
        when(levelWaves.getStartTime()).thenReturn(1000L);
        assertEquals(1000L, levelWaves.getStartTime());
    }

}
