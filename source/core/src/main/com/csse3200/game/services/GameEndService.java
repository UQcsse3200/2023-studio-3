package com.csse3200.game.services;

public class GameEndService {

    private int engineerCount;

    public GameEndService() {
        this.engineerCount = 5;
    }

    public int getEngineerCount() {
        return engineerCount;
    }

    public void updateEngineerCount() {
        engineerCount -= 1;
        if (engineerCount == 0) {
            // loss screen

        }
    }
}
