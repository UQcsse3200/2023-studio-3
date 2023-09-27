package com.csse3200.game.entities.factories;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.waves.WaveTask;
import com.csse3200.game.entities.Entity;


public class WaveFactory {
    /**
     * Create a Wave entity.
     * @return entity
     */
    public static Entity createWave() {
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WaveTask());
        return new Entity().addComponent(aiComponent);
    }

    private WaveFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
