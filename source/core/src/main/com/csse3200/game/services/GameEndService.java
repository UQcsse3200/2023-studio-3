package com.csse3200.game.services;

import com.csse3200.game.components.gamearea.EngineerCountDisplay;

public class GameEndService {

    private int engineerCount;

    private boolean gameOver = false;

    private EngineerCountDisplay display;

    /**
     * Constructor for the Game End Service
     */
    public GameEndService() {
        this.engineerCount = 5;
        this.display = new EngineerCountDisplay();
    }

    /**
     * Returns the number of engineers left
     * @return (int) engineer count
     */
    public int getEngineerCount() {
        return engineerCount;
    }

    /**
     * Updates engineer count and the UI display
     * If engineer count is 0, the game is over.
     */
    public void updateEngineerCount() {
        engineerCount -= 1;
        display.updateCount();

        if (engineerCount == 0) {
            // loss screen
            gameOver = true;
        }
    }

    /**
     * Returns the game over state
     * @return (boolean) true if the game is over; false otherwise
     */
    public boolean hasGameEnded() {
        return gameOver;
    }

    /**
     * Returns the Engineer Count UI component
     */
    public EngineerCountDisplay getDisplay() {
        return display;
    }
}
