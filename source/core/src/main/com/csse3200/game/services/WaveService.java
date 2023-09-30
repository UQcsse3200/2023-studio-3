package com.csse3200.game.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaveService {
    private static final Logger logger = LoggerFactory.getLogger(WaveService.class);
    private int enemyCount;
    private boolean gameOver = false;
    private int lane;

    private int waveCount = 1;

    private boolean levelCompleted = false;


    /**
     * Constructor for the Wave Service
     */
    public WaveService() {
        this.enemyCount = 0;
    }

    /**
     * Set the enemy limit. During instantiation, limit defaults to 0.
     * @param newLimit as an integer representing the maximum number of enemy deaths
     */
    public void setEnemyCount(int newLimit) {
        if (newLimit > 0) {
            enemyCount = newLimit;
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
        enemyCount -= 1;
        logger.info("{} enemies remaining in wave", getEnemyCount());
    }

    /**
     * Set the level to be completed. Will be called when there are no waves remaining.
     */
    public void setLevelCompleted() {
        if (!levelCompleted) {
            levelCompleted = true;
        }
    }

    public void setNextLane(int lane) {
        this.lane = lane;
    }

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

    public int getWaveCount() {
        return this.waveCount;
    }

    public void setWaveCount(int waveCount) {
        this.waveCount += waveCount;
    }
}
