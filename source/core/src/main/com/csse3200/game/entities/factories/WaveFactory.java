package com.csse3200.game.entities.factories;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.WaveClass;
import com.csse3200.game.components.tasks.waves.WaveTask;
import com.csse3200.game.entities.Entity;

import java.util.HashMap;


public class WaveFactory {
    /**
     * Create a Wave entity.
     * Each wave class represents a single wave, then they are appended to a level.
     * Cases can be written in here to set what happens for each level.
     * @return entity
     */
    public static Entity createWaves() {
        HashMap<String, Integer> mobs = new HashMap<>();
        mobs.put("Xeno", 3);
        mobs.put("DodgingDragon", 4);
        HashMap<String, Integer> mobs2 = new HashMap<>();
        mobs2.put("Xeno", 3);
        WaveClass wave1 = new WaveClass(mobs);
        WaveClass wave2 = new WaveClass(mobs2);
        LevelWaves level = new LevelWaves(10);
        level.addWave(wave1);
        level.addWave(wave2);
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WaveTask());
        return level.addComponent(aiComponent);
    }

    private WaveFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
