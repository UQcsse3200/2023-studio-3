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
    private static int pauseMenusActive = 0;

    /**
     * Creates the pause menu
     * @param game The Gdx Game that handles screen changes
     * @param duplicateOverride If false, will not create a pause menu if any currently exist
     * @return entity, or null if no entity was created
     */
    public static Entity createPauseMenu(GdxGame game, boolean duplicateOverride) {
        if (pauseMenusActive == 0 || duplicateOverride) {
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
            pauseMenusActive++;
            return pauseMenu;
        }
        return null;
    }

    /**
     * Called when a pause menu is disposed to decrement the count of active pause menus.
     */
    public static void decrementPauseMenuCount() {
        pauseMenusActive--;
    }
}
