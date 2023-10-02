package com.csse3200.game.components.tasks.waves;

import com.csse3200.game.entities.factories.WaveFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.ServiceLocator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class LevelWavesTest {

    LevelWaves levelWaves;
    WaveClass wave;
    ServiceLocator serviceLocator;

    @BeforeEach
    void setUp() {
        levelWaves = (LevelWaves) WaveFactory.createWaves();
        wave =  mock(WaveClass.class);
    }

    @Test
    public void testAddWave() {
        levelWaves.addWave(wave);
        assertEquals(3, levelWaves.waves.size());
    }

    @Test
    public void testSpawnWaveStart() {

    }
}
