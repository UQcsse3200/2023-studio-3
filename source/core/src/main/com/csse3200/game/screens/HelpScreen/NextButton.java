package com.csse3200.game.screens.HelpScreen;



import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.PauseMenuFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to go to next screen in the game and bring up a menu.
 */
public class NextButton extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(com.csse3200.game.screens.HelpScreen.NextButton.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private GdxGame game;



    /**
     * Creates a new NextButton.
     *
     * @param screenSwitchHandle The main game instance.
     */
    public NextButton(GdxGame screenSwitchHandle) {
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

        TextButton mainMenuBtn = new TextButton("Pause", skin);

        // Spawns a pause menu when the button is pressed.
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Pause button clicked");

//                        PauseMenuFactory.createPauseMenu(game, false);
                        PauseMenuFactory.createPauseMenu(game);

                    }
                });

        table.add(mainMenuBtn).padTop(100f).padRight(10f);

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
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
