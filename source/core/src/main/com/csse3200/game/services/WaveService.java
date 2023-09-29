package com.csse3200.game.services;

public class WaveService {
    private int enemyCount;
    private boolean gameOver = false;
    private int lane;


    /**
     * Constructor for the Game End Service
     */
    public WaveService() {
        this.enemyCount = 0;
    }

    /**
     * Set the enemy limit. During instantiation, limit defaults to 5.
     * @param newLimit as an integer representing the maximum number of engineer deaths
     */
    public void setEnemyCount(int newLimit) {
        if (newLimit > 0) {
            enemyCount = newLimit;
        }
    }

    /**
     * Returns the number of enemy left
     * @return (int) engineer count
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

        if (enemyCount == 0) {
            gameOver = true;
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
    public boolean hasGameEnded() {
        return gameOver;
    }
}
