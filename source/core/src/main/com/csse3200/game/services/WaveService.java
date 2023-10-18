package com.csse3200.game.services;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.maingame.UIElementsDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaveService {
    private static final Logger logger = LoggerFactory.getLogger(WaveService.class);
    private int enemyCount;
    private boolean gameOver = false;
    private int lane;

    private int waveCount = 1;

    private boolean levelCompleted = false;

    private long nextWaveTime;
    private final UIElementsDisplay display;

    private int spawnDelay;

    private boolean skipDelay = false;
    private boolean gamePaused = false;
    private long pauseBeginTime = 0;

    private int levelEnemyCount = 0;
    private int remainingLevelEnemyCount = 0;


    /**
     * Constructor for the Wave Service
     */
    public WaveService() {
        this.enemyCount = 0;
        this.display = new UIElementsDisplay();
    }

    /**
     * Set the enemy limit. During instantiation, limit defaults to 0.
     * @param newLimit as an integer representing the maximum number of enemy deaths
     */
    public void setEnemyCount(int newLimit) {
        if (newLimit > 0) {
            enemyCount = newLimit;
            display.updateMobCount();
        }
    }

    /**
     * Returns the number of enemy left
     * @return (int) enemy count
     */

    public int getEnemyCount() {
        return enemyCount;
    }

    /**
     * Updates enemy count
     * If enemy count is 0, the game is over.
     */
    public void updateEnemyCount() {
//        Array<Entity> entities = ServiceLocator.getEntityService().getEntitiesInLayer(PhysicsLayer.NPC);
        enemyCount -= 1;
        remainingLevelEnemyCount -= 1;
        logger.info("{} enemies remaining in wave", getEnemyCount());
    }

    /**
     * Set the level to be completed. Will be called when there are no waves remaining.
     */
    public void setLevelCompleted() {
        if (!levelCompleted) {
            logger.info("Level set to completed");
            levelCompleted = true;
        }
    }

    /**
     * Sets the waveCount
     * @param lane as an integer representing the next lane of a mob.
     */
    public void setNextLane(int lane) {
        this.lane = lane;
    }

    /**
     * Returns the next lane number of a mob
     * @return (int) lane number
     */
    public int getNextLane() {
        return lane;
    }

    /**
     * Returns the game over state
     * @return (boolean) true if the game is over; false otherwise
     */
    public boolean isLevelCompleted() {
        return levelCompleted;
    }

    /**
     * Returns the game over state
     * @return (boolean) true if the game is over; false otherwise
     */
    public int getWaveCount() {
        return this.waveCount;
    }

    /**
     * Sets the waveCount
     * @param waveCount as an integer representing the current wave number.
     *                  This will be added to the current wave number.
     */
    public void setWaveCount(int waveCount) {
        this.waveCount += waveCount;
    }

    /**
     * Returns time of the next wave.
     * @return (long) A timestamp of when the next mobs will spawn. Used for UI elements.
     */
    public long getNextWaveTime() {
        return this.nextWaveTime;
    }

    /**
     * Sets the next wave timestamp
     * @param nextWaveTime as a long which is the time when then next mobs will spawn.
     */
    public void setNextWaveTime(long nextWaveTime) {
        this.nextWaveTime = nextWaveTime;
    }

    /**
     * Sets the spawn delay between levels
     * @param spawnDelay representing the spawnDelay between levels.
     */
    public void setSpawnDelay(int spawnDelay) {this.spawnDelay = spawnDelay;}

    /**
     * Returns the spawn delay between levels
     * @return (int) The spawn delay between levels.
     */
    public int getSpawnDelay() {return this.spawnDelay;}


    /**
     * Used for adding this instance of UIElementsDisplay to the mainGameScreen. This is needed as update is performed
     * for this instance of the display.
     * @return the updating instance of UIElementsDisplay
     */
    public UIElementsDisplay getDisplay() {
        return this.display;
    }

    /**
     * This will invert the value of the skipDelay boolean
     * */
    public void toggleDelay() {
        this.skipDelay = !this.skipDelay;
    }

    /**
     * retrieve the skipDelay condition
     * */
    public boolean shouldSkip() {
      return this.skipDelay;
    }

    /**
     * Return whether the game is currently paused or not.
     * @return the gamePaused variable.
     */
    public boolean getGamePaused() {return this.gamePaused;}

    /**
     * Toggles whether the game is paused or not, to keep track of how long the game is paused.
     * When unpaused, offsets the NextWaveTime by however long the game has been paused.
     */
    public void toggleGamePause() {
        if (gamePaused) {
            long pauseDuration = ServiceLocator.getTimeSource().getTime() - pauseBeginTime;
            long updatedNextWaveTime = getNextWaveTime() + pauseDuration;
            setNextWaveTime(updatedNextWaveTime);
        } else {
            pauseBeginTime = ServiceLocator.getTimeSource().getTime();
        }
        gamePaused = !gamePaused;
    }

    /**
     * retrieve the number of enemies in the level
     * */
    public int totalMobs() {
      return this.levelEnemyCount;
    }

    /**
     * set the total number of enemies in the level
     * */
    public void setTotalMobs(int total) {
      this.levelEnemyCount = total;
      this.remainingLevelEnemyCount = total;
    }

    /**
     * get the number of mobs remaining for the whole level()
     * */
    public int remainingMobsForLevel() {
      return this.remainingLevelEnemyCount;
    }
}
