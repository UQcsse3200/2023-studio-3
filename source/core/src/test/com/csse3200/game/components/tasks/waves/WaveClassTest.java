package com.csse3200.game.components.tasks.waves;


import com.csse3200.game.components.tasks.waves.WaveClass;
import com.csse3200.game.extensions.GameExtension;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)

@Disabled
class WaveClassTest {

    HashMap<String, int[]> waveContents;
    WaveClass waveClass;
    @BeforeEach
    void setUp() {
        waveContents = new HashMap<>();
        waveContents.put("Xeno", new int[]{2, 20});
        waveContents.put("DodgingDragon", new int[]{3, 40});
        waveContents.put("SplittingXeno", new int[]{5, 60});
        waveContents.put("DeflectXeno", new int[]{7, 80});
        waveClass = new WaveClass(waveContents);
    }

    @Test
    public void testGetMobs() {
        List<Tuple> enemies = waveClass.getMobs();
        assertTrue(enemies.contains("Xeno")
                && enemies.contains("DodgingDragon")
                && enemies.contains("SplittingXeno")
                && enemies.contains("DeflectXeno"));
    }

    @Test
    public void testGetSize() {
        int size = waveClass.getSize();
        assertEquals(17, size);
    }
}
