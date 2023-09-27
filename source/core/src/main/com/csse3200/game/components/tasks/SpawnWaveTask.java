package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpawnWaveTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(SpawnWaveTask.class);
    private final GameTime globalTime;
    private long endTime = 0;
    private final int SPAWNING_INTERVAL = 10;
    private boolean wavesPaused = false;
    private long pauseTime = 0;
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
        this.owner.getEntity().getEvents().addListener("toggleWaveTimer", this::toggleWaveTimer);
    }

    @Override
    public void update() {
        if (!wavesPaused) {
            if (globalTime.getTime() >= endTime) {
                this.owner.getEntity().getEvents().trigger("spawnWave");
                endTime = globalTime.getTime() + (SPAWNING_INTERVAL * 1000L); // reset end time
            }
        }
    }

    /**
     * Function for pausing the wave timer when the pause menu is opened, and resuming it when the pause menu is closed.
     */
    private void toggleWaveTimer() {
        if (!wavesPaused) {
            pauseTime = globalTime.getTime();
            wavesPaused = true;
        } else {
            // Offsets the next wave spawn by however long the game was paused.
            endTime += globalTime.getTime() - pauseTime;
            wavesPaused = false;
        }
    }
}
