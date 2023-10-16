package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaveTask extends DefaultTask implements PriorityTask {
  private static final Logger logger = LoggerFactory.getLogger(WaveTask.class);
  private LevelWaves level;
  private WaveClass currentWave;
  private final GameTime globalTime;
  private long nextWaveAt = 0;
  private int currentWaveIndex = 0;
  private boolean waveInProgress;

  private static final String[] waveSounds = {
          "sounds/waves/wave-start/Wave_Start_Alarm.ogg",
          "sounds/waves/wave-end/Wave_Over_01.ogg"
  };

  private final Sound waveStart;
  private final Sound waveEnd;

  /**
   * Constructor for the WaveTask
   */
  public WaveTask() {
    this.globalTime = ServiceLocator.getTimeSource();
    this.waveInProgress = false;
    loadSounds();
    this.waveStart = ServiceLocator.getResourceService().getAsset(waveSounds[0], Sound.class);
    this.waveEnd = ServiceLocator.getResourceService().getAsset(waveSounds[1], Sound.class);
  }

  /**
   * Load the sounds to be played when a wave starts or ends
   */
  public void loadSounds() {
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadSounds(waveSounds);
  }

    /**
     * Get the sounds to be played when a wave starts or ends
     * @return String array of sounds
     */
  public String[] getSounds() {
    return waveSounds;
  }

  /**
   * Gets the priority of the current task
   * @return priority of the WaveTask
   */
  @Override
  public int getPriority() {
    return 10; // High priority task
  }

  /**
   * Starts the WaveTask and initialises all relevant attributes.
   * Sets the current count of enemies to be the size of the current wave.
   */
  @Override
  public void start() {
    super.start();
    this.owner.getEntity().getEvents().addListener("waveFinishedSpawning", () -> waveInProgress = false);
    this.waveInProgress = true;
    this.level = (LevelWaves) this.owner.getEntity();
    this.currentWave = level.getWave(currentWaveIndex);
    ServiceLocator.getWaveService().setEnemyCount(currentWave.getSize());
    logger.info("Wave {} starting with {} enemies", currentWaveIndex, ServiceLocator.getWaveService().getEnemyCount());
  }

  /**
   * Checks if the current wave has finished (i.e. number of mobs left is 0) and calls the next wave
   * to begin. If there are still mobs remaining, continue the wave.
   */
  @Override
  public void update() {
    if (ServiceLocator.getWaveService().getEnemyCount() == 0) {

      // Check if level has been completed - no more waves remaining
      if (currentWaveIndex == this.level.getNumWaves() - 1) {
        logger.info("No waves remaining");
        ServiceLocator.getWaveService().setLevelCompleted();

      } else {
        // Spawn the next wave
        if (nextWaveAt == 0) {
          logger.info("Next wave in 10 seconds");
          this.waveEnd.play();
          nextWaveAt = globalTime.getTime() + 10000;
          ServiceLocator.getWaveService().setNextWaveTime(nextWaveAt);
        } else {
          if (globalTime.getTime() >= nextWaveAt || ServiceLocator.getWaveService().shouldSkip()) {
            this.waveStart.play();
            ServiceLocator.getWaveService().toggleDelay();
            currentWaveIndex++;
            ServiceLocator.getWaveService().setNextWaveTime(0);
            nextWaveAt = 0;
            this.waveInProgress = true;
            this.level.setWaveIndex(currentWaveIndex);
            // Set the service wave count to the current wave index.
            ServiceLocator.getWaveService().setWaveCount(currentWaveIndex);
            this.currentWave = this.level.getWave(currentWaveIndex);
            ServiceLocator.getWaveService().setEnemyCount(currentWave.getSize());
            logger.info("Next wave {} starting with {} enemies", currentWaveIndex, ServiceLocator.getWaveService().getEnemyCount());
          }
        }
      }

    } else {
      if (waveInProgress) {
        this.level.spawnWave();
      }
    }
  }

  /**
   * Checks if the current wave is in progress
   * @return true if the wave is in progress, false otherwise
   */
  public boolean isWaveInProgress() {
    return waveInProgress;
    }

    /**
     * Gets the current wave index
     * @return current wave index
     */
    public int getCurrentWaveIndex() {
        return currentWaveIndex;
    }
  }
