package com.csse3200.game.screens;

public class GameLevelData {
    private static int selectedLevel = -1;

    public static int getSelectedLevel() {
        return selectedLevel;
    }

    public static void setSelectedLevel(int level) {
        selectedLevel = level;
    }
}