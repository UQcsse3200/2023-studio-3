package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

public class SpawnWaveTask extends DefaultTask implements PriorityTask {

    private final GameTime globalTime;
    private long endTime = 0;
    private final int SPAWNING_INTERVAL = 10;
    public SpawnWaveTask() {
        this.globalTime = ServiceLocator.getTimeSource();
    }

    @Override
    public int getPriority() {
        return 10; // High priority task
    }

    @Override
    public void start() {
        super.start();
        endTime = globalTime.getTime() + (SPAWNING_INTERVAL * 1000);
    }

    @Override
    public void update() {
        if (globalTime.getTime() >= endTime) {
            this.owner.getEntity().getEvents().trigger("spawnWave");
            endTime = globalTime.getTime() + (SPAWNING_INTERVAL * 1000L); // reset end time
        }
    }
}
