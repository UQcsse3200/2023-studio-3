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
  private LevelWaves level;
  private WaveClass currentWave;
  private final GameTime globalTime;
  private int currentWaveIndex;
  private boolean waveInProgress;
  private float startTime = 0;
  private float endTime = 0;
  private final float INITIAL_WAIT_INTERVAL = 10;
  private final int SPAWNING_INTERVAL = 10;

  public WaveTask() {
    this.globalTime = ServiceLocator.getTimeSource();
    this.currentWaveIndex = 0;
    this.waveInProgress = false;
  }

  @Override
  public int getPriority() {
    return 10; // High priority task
  }

  @Override
  public void start() {
    super.start();
    this.owner.getEntity().getEvents().addListener("waveFinishedSpawning", () -> waveInProgress = false);
    this.waveInProgress = true;
    this.level = (LevelWaves) this.owner.getEntity();
    this.currentWave = level.getWave(currentWaveIndex);
    ServiceLocator.getWaveService().setEnemyCount(currentWave.getSize());
    logger.info("Wave {} starting", currentWaveIndex);
      //endTime = globalTime.getTime() + (SPAWNING_INTERVAL * 1000);
  }

  @Override
  public void update() {
    if (ServiceLocator.getWaveService().getEnemyCount() == 0) {
      this.waveInProgress = true;
      logger.info("No enemies remaining, begin next wave");
      currentWaveIndex++;
      this.currentWave = this.level.getWave(currentWaveIndex);
      //endTime = globalTime.getTime() + (SPAWNING_INTERVAL * 1000L); // reset end time
    } else {
      logger.info("{} enemies remaining in wave {}", ServiceLocator.getWaveService().getEnemyCount(), currentWaveIndex);
      if (waveInProgress) {
        this.level.spawnWave();
      }
    }
  }
}