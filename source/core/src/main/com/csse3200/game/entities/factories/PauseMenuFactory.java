package com.csse3200.game.entities.factories;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;
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
                .addComponent(new PauseMenuContinueButton())
                .addComponent(new PauseMenuSettingsButton(game))
                .addComponent(new PauseMenuPlanetSelectButton(game))
                .addComponent(new PauseMenuMainMenuButton(game))
                .addComponent(new TextureRenderComponent("images/ui/Sprites/UI_Glass_Toggle_Bar_01a.png"));
        pauseMenu.setScale(8, 8);
        pauseMenu.setPosition(6.3f, 2f);
        ServiceLocator.getEntityService().register(pauseMenu);
        return pauseMenu;
    }
}
