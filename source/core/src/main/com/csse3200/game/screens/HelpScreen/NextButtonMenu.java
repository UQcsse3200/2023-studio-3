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

public class NextButtonMenu {

    public static Entity nextMenu(GdxGame game) {

        Entity TutorialMenu = new Entity()
                .addComponent(new PauseMenuTimeStopComponent())
                .addComponent(new PauseMenuContinueButton());

        TutorialMenu.setScale(16, 8.2f);
        TutorialMenu.setPosition(center+1.3f, center);

        Table table = new Table();
        table.setFillParent(true); // This makes the table the size of the stage

        BitmapFont font = new BitmapFont(); // You can customize the font
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        Label textLabel = new Label("Select and place towers and press continue", style);

        // Add the text label to the table
        table.add(textLabel).center();


        ServiceLocator.getEntityService().register(TutorialMenu);
        return TutorialMenu;

    }
}

