package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.audio.Music;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WaveTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(WaveTask.class);
  private List<WaveClass> waves;
  private final GameTime globalTime;
  private int currentWaveIndex;
  private boolean waveInProgress;
  private float startTime = 0;
  private float endTime = 0;
  private final float INITIAL_WAIT_INTERVAL = 10;
  private final int SPAWNING_INTERVAL = 10;
  private WaveClass currentWave;

  public WaveTask() {
    this.globalTime = ServiceLocator.getTimeSource();
    this.currentWaveIndex = 1;
    this.waveInProgress = false;
  }

  @Override
  public int getPriority() {
    return 10; // High priority task
  }

  @Override
  public void start() {
    super.start();
    ServiceLocator.getWaveService().setEnemyCount(5);
    startTime = globalTime.getTime() + (INITIAL_WAIT_INTERVAL * 1000);

    if (globalTime.getTime() >= startTime) {
      this.waveInProgress = true;
      this.currentWave = waves.get(currentWaveIndex);
      logger.info("Wave {} starting", currentWaveIndex);
      endTime = globalTime.getTime() + (SPAWNING_INTERVAL * 1000);
    }
  }

  @Override
  public void update() {
    // globalTime.getTime() >= endTime &&
    if (ServiceLocator.getWaveService().getEnemyCount() == 0) {
      logger.info("No enemies remaining, begin next wave");
      currentWave.spawnWave();
      //this.owner.getEntity().getEvents().trigger("spawnWave");
      endTime = globalTime.getTime() + (SPAWNING_INTERVAL * 1000L); // reset end time
    } else {
      logger.info("{} enemies remaining in wave {}", ServiceLocator.getWaveService().getEnemyCount(), currentWaveIndex);
    }
  }
}