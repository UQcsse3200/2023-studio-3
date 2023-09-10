package com.csse3200.game.services;

import com.csse3200.game.components.gamearea.EngineerCountDisplay;

public class GameEndService {

    private int engineerCount;

    private boolean gameOver = false;

    private EngineerCountDisplay display;

    public GameEndService() {
        this.engineerCount = 5;
        this.display = new EngineerCountDisplay();
    }

    public int getEngineerCount() {
        return engineerCount;
    }

    public void updateEngineerCount() {
        engineerCount -= 1;
        display.updateCount();

        if (engineerCount == 0) {
            // loss screen
            gameOver = true;
        }
    }

    public boolean hasGameEnded() {
        return gameOver;
    }

    public EngineerCountDisplay getDisplay() {
        return display;
    }
}
