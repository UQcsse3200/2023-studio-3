package com.csse3200.game.entities.factories;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Array;

/**
 * Factory to create the pause menu and attach its components.
 */
public class PauseMenuFactory {
    private static int lastPauseMenuID = -1;

    /**
     * Creates the pause menu, if no previously made pause menu still exists.
     * @param game The Gdx Game that handles screen changes
     * @return entity, or null if no entity was created
     */
    public static Entity createPauseMenu(GdxGame game) {
        if (!previousPauseActive()) {

            Entity pauseMenu = new Entity()
                    .addComponent(new PauseMenuTimeStopComponent())
<<<<<<< Updated upstream
                    .addComponent(new PauseMenuContinueButton())
                    .addComponent(new PauseMenuSettingsButton(game))
                    .addComponent(new PauseMenuPlanetSelectButton(game))
                    .addComponent(new PauseMenuMainMenuButton(game))
                    .addComponent(new TextureRenderComponent("images/ui/Sprites/UI_Glass_Toggle_Bar_01a.png"));
=======
                    .addComponent(new PauseMenuButtonComponent(game));
>>>>>>> Stashed changes
            pauseMenu.setScale(8, 8);
            pauseMenu.setPosition(6.3f, 2f);
            ServiceLocator.getEntityService().register(pauseMenu);
            lastPauseMenuID = pauseMenu.getId();
            return pauseMenu;
        } else {
            return null;
        }
    }

    /**
     * Function for checking if the last pause menu created is still registered to the entity service.
     * @return true if the last pause menu's ID is found in the entity service.
     */
    private static boolean previousPauseActive() {
        Array<Entity> checkList = ServiceLocator.getEntityService().getEntities();
        for (Entity check : checkList) {
            if (check.getId() == lastPauseMenuID) {
                return true;
            }
        }
        return false;
    }
}
