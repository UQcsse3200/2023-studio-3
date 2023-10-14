package com.csse3200.game.components.pausemenu;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.components.maingame.MainGamePauseDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PauseMenuFactory;
import com.csse3200.game.services.ServiceLocator;

import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button in the pause menu to resume the game and put away the pause menu.
 */
public class PauseMenuContinueButton extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PauseMenuContinueButton.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private final String[] sounds = {
            "sounds/ui/Open_Close/NA_SFUI_Vol1_Close_01.ogg"
    };
    private Sound closeSound;

    @Override
    public void create() {
        super.create();
        addActors();
        loadSounds();
    }

    private void addActors() {
        table = new Table();
        table.top().right();
        table.setFillParent(true);
        TextButton pauseMenuBtn = ButtonFactory.createButton("Continue");

        // Triggers an event when the button is pressed.
        pauseMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Continue button clicked");
                        closeSound.play(0.4f);
                        entity.dispose();
                    }
                });

        table.add(pauseMenuBtn).padTop(300f).padRight(700f);

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

    public void loadSounds() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        closeSound = ServiceLocator.getResourceService().getAsset(sounds[0], Sound.class);
    }
}
