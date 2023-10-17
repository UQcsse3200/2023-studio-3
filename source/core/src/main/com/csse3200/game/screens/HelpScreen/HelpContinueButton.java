package com.csse3200.game.screens.HelpScreen;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.pausemenu.PauseMenuButtonComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PauseMenuFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.pausemenu.*;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.pausemenu.PauseMenuButtonComponent;
/**
 * Displays a button in the help menu to resume the game and put away the pause menu.
 */
public class HelpContinueButton extends UIComponent {
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table table1;
    private GdxGame game;

    private Sound closeSound;

    @Override
    public void create() {
        super.create();
        addActors();

    }
    /**
     * Adds the button to the stage.
     */
    private void addActors() {
        table = new Table();
        table.center();
        table.setFillParent(true);
        ButtonFactory buttonFactory = new ButtonFactory();
//        TextButton pauseMenuBtn = buttonFactory.createButton("Continue");
        TextButton pauseMenuBtn = new TextButton("Continue", skin);

        // Triggers an event when the button is pressed.
        pauseMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                       // logger.debug("Continue button clicked");

                        entity.dispose();
                        NextButtonMenu.nextMenu(game);

                    }
                });

        table.add(pauseMenuBtn);
        stage.addActor(table);

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
        table.clear();
        super.dispose();
    }


}
