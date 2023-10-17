package com.csse3200.game.services;

 public class GameState {
    private static final GameState instance = new GameState();

    private int currentLevel;

    private GameState() {
        // Initialize default values
        currentLevel = 0;
    }

    public static GameState getInstance() {
        return instance;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int level) {
        currentLevel = level;
    }
}


