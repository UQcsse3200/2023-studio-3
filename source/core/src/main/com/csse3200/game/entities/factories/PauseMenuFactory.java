package com.csse3200.game.entities.factories;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;

/**
 * Factory to create the pause menu and attach its components.
 */
public class PauseMenuFactory {

    /**
     * Creates the pause menu
     * @return entity
     */
    public static Entity createPauseMenu(GdxGame game) {
        Entity pauseMenu = new Entity()
                .addComponent(new PauseMenuTimeStopComponent())
                .addComponent(new PauseMenuRenderComponent())
                .addComponent(new PauseMenuContinueButton())
                .addComponent(new PauseMenuSettingsButton(game))
                .addComponent(new PauseMenuPlanetSelectButton(game))
                .addComponent(new PauseMenuMainMenuButton(game));
        ServiceLocator.getEntityService().register(pauseMenu);
        return pauseMenu;
    }
}
