package com.csse3200.game.screens.HelpScreen;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PauseMenuFactory;
import com.csse3200.game.services.ServiceLocator;

import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The NextContinueButton class displays a button in the game menu to resume the game and put away the menu.
 * It extends UIComponent to handle UI-related functionality.
 */
public class NextContinueButton extends UIComponent {

    private static final float Z_INDEX = 2f;
    private Table table1;
    private GdxGame game;
    /**
     * Creates the UI components and adds them to the stage.
     */

    @Override
    public void create() {
        super.create();
        addActors();

    }

    private void addActors() {

        table1 = new Table();

        table1.setFillParent(true);
//        TextButton NextMenuBtn = buttonFactory.createButton("Continue");
        TextButton NextMenuBtn = new TextButton("Continue", skin);

        // Triggers an event when the button is pressed.
        NextMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        //logger.debug("Continue button clicked");

                        entity.dispose();

                    }
                });

        table1.add(NextMenuBtn);
        stage.addActor(table1);
        table1.top().right();
        table1.padTop(350f).padRight(100f);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the renderer component
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table1.clear();
        super.dispose();
    }

}
