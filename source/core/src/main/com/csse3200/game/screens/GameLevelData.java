package com.csse3200.game.screens;

/**
 * The {@code GameLevelData} class is responsible for managing the selected game level.
 * It provides methods to get and set the selected game level.
 */
public class GameLevelData {
    private static int selectedLevel = -1;

    /**
     * Get the currently selected game level.
     *
     * @return The selected game level.
     */
    public static int getSelectedLevel() {
        return selectedLevel;
    }

    /**
     * Set the selected game level.
     *
     * @param level The new game level to be selected.
     */
    public static void setSelectedLevel(int level) {
        selectedLevel = level;
    }

}