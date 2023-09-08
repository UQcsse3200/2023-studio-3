package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A task which spawns a new wave of mobs at a set spawning interval
 *
 */
public class SpawnWaveTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(SpawnWaveTask.class);
    private final GameTime globalTime;
    private long endTime;
    private final int SPAWNING_INTERVAL = 10;
    public SpawnWaveTask() {
        this.globalTime = ServiceLocator.getTimeSource();
    }

    @Override
    public int getPriority() {
        return 10; // Low priority task
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
