package com.csse3200.game.screens;

import com.badlogic.gdx.Game;
import com.csse3200.game.GdxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code GameLevelData} class is responsible for managing the selected game level.
 * It provides methods to get and set the selected game level.
 */
public class GameLevelData {
    private static int selectedLevel = -1;
    private static final Logger logger = LoggerFactory.getLogger(GameLevelData.class);

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

    /**
     * Handle the click event for a planet based on its index.
     *
     * @param planetIndex The index of the planet clicked.
     * @param game        The Game instance.
     */
    public static void handlePlanetClick(int planetIndex, Game game) {
        switch (planetIndex) {
            case 0:
                handleDesertPlanetClick(game);
                break;
            case 1:
                handleIcePlanetClick(game);
                break;
            case 2:
                handleLavaPlanetClick(game);
                break;
            default:
                logger.warn("Invalid planet index: " + planetIndex);
                break;
        }
    }

    private static void handleDesertPlanetClick(Game game) {
        // Implement logic for when the desert planet is clicked
        logger.info("Desert planet clicked.");
        game.setScreen(new DesertGameScreen((GdxGame) game)); // Load the DesertGameScreen
    }

    private static void handleIcePlanetClick(Game game) {
        // Implement logic for when the ice planet is clicked
        logger.info("Ice planet clicked.");
        game.setScreen(new IceGameScreen((GdxGame) game)); // Load the IceGameScreen
    }

    private static void handleLavaPlanetClick(Game game) {
        // Implement logic for when the lava planet is clicked
        logger.info("Lava planet clicked.");
        game.setScreen(new LavaGameScreen((GdxGame) game)); // Load the LavaGameScreen
    }
}
