package com.csse3200.game.screens.HelpScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.TextureRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.top;

public class NextButtonMenu {
    /**
     * Creates and configures a game menu entity with specific components.
     *
     * @param game The GdxGame instance to associate with the game menu.
     * @return The created game menu entity.
     */
    public static Entity nextMenu(GdxGame game) {

        Entity Menu = new Entity()
                .addComponent(new PauseMenuTimeStopComponent())
                .addComponent(new NextContinueButton())
                .addComponent(new TowerSelectionInstruct());
        Menu.setScale(8, 8.2f);
        Menu.setPosition(6.3f,2f);

        ServiceLocator.getEntityService().register(Menu);
        return Menu;

    }
}

