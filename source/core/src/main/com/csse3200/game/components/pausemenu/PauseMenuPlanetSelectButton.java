package com.csse3200.game.components.pausemenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button in the pause menu to return to the planet select screen.
 */
public class PauseMenuPlanetSelectButton extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PauseMenuPlanetSelectButton.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    private final GdxGame game;

    public PauseMenuPlanetSelectButton(GdxGame screenSwitchHandle) {
        game = screenSwitchHandle;
    }
    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.top().right();
        table.setFillParent(true);
        TextButton pauseMenuBtn = ButtonFactory.createButton("Planet Select");

        // Triggers an event when the button is pressed.
        pauseMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Planet select button clicked");
                        game.setScreen(GdxGame.ScreenType.LEVEL_SELECT);
                    }
                });

        table.add(pauseMenuBtn).padTop(400f).padRight(675f);

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
