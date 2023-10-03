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

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WaveClassTest {

    HashMap<String, Integer> waveContents;
    WaveClass waveClass;
    @BeforeEach
    void setUp() {
        waveContents = new HashMap<>();
        waveContents.put("Xeno", 2);
        waveContents.put("DodgingDragon", 3);
        waveContents.put("SplittingXeno", 5);
        waveContents.put("DeflectXeno", 7);
        waveClass = new WaveClass(waveContents);
    }

    @Test
    public void testGetMobs() {
        List<String> enemies = waveClass.getMobs();
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
