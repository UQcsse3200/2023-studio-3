package com.csse3200.game.components.pausemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the visual aspects of the pause menu, including button interactions.
 */
public class PauseMenuButtonComponent extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PauseMenuButtonComponent.class);
    private static final float Z_INDEX = 2f;
    private Window window;
    private final GdxGame game;
    private static final float WINDOW_SIZE_X = 300;
    private static final float WINDOW_SIZE_Y = 400;
    private final String[] sounds = {
            "sounds/ui/click/click_01.ogg",
            "sounds/ui/open_close/close_01.ogg",
    };
    private Sound click;
    private Sound closeSound;

    public PauseMenuButtonComponent(GdxGame screenSwitchHandle) {
        game = screenSwitchHandle;
    }

    /**
     * Sets up the buttons and window of the pause menu when it is first made.
     */
    @Override
    public void create() {
        super.create();
        addActors();
        loadSounds();
    }

    /**
     * Initialises the pause menu buttons
     * Positions them on the stage using a window
     */
    private void addActors() {

        window = new Window("", new Skin(Gdx.files.internal("images/ui/buttons/glass.json")));

        TextButton continueBtn = ButtonFactory.createButton("Continue");
        continueBtn.pad(20f);
        TextButton settingsBtn = ButtonFactory.createButton("Settings");
        settingsBtn.pad(20f);
        TextButton planetSelectBtn = ButtonFactory.createButton("Planet Select");
        planetSelectBtn.pad(20f);
        TextButton mainMenuBtn = ButtonFactory.createButton("Main Menu");
        mainMenuBtn.pad(20f);

        continueBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Continue button clicked");
                        click.play(0.5f);
                        closeSound.play(0.5f);
                        ServiceLocator.getTimeSource().setPaused(false);
                        entity.dispose();
                    }
                });
        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        click.play(0.5f);
                        logger.debug("Settings button clicked");
                        game.setScreen(GdxGame.ScreenType.SETTINGS);
                    }
                });
        planetSelectBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        click.play(0.5f);
                        logger.debug("Planet select button clicked");
                        game.setScreen(GdxGame.ScreenType.LEVEL_SELECT);
                    }
                });
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        click.play(0.5f);
                        logger.debug("Main menu button clicked");
                        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
                    }
                });

        window.setResizable(true);
        window.setModal(true);
        window.setTouchable(Touchable.enabled);
        window.setMovable(true);
        window.setFillParent(false);
        window.center();
        window.add(continueBtn).center();
        window.row();
        window.add(settingsBtn).center();
        window.row();
        window.add(planetSelectBtn).center();
        window.row();
        window.add(mainMenuBtn).center();

        window.setWidth(WINDOW_SIZE_X);
        window.setHeight(WINDOW_SIZE_Y);
        window.setX((ServiceLocator.getRenderService().getStage().getWidth() / 2) - (WINDOW_SIZE_X / 2));
        window.setY((ServiceLocator.getRenderService().getStage().getHeight() / 2) - (WINDOW_SIZE_Y / 2));

        // Animate the pause menu opening
        window.setPosition(((float) Gdx.graphics.getWidth() / 2) - (WINDOW_SIZE_X / 2),0);
        window.addAction(new SequenceAction(Actions.moveTo(
                ( ((float) Gdx.graphics.getWidth() / 2) - (WINDOW_SIZE_X / 2) ),
                ( ((float) Gdx.graphics.getHeight() / 2) - (WINDOW_SIZE_Y / 2) ),
                        0.3f,
                        Interpolation.fastSlow),
                Actions.fadeIn(0.3f)));



        stage.addActor(window);
    }

    /**
     * Draws the pause menu on the game screen.
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // handled by stage
    }

    /**
     * Loads sound assets for use in the class
     */
    public void loadSounds() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        click = ServiceLocator.getResourceService().getAsset(sounds[0], Sound.class);
        closeSound = ServiceLocator.getResourceService().getAsset(sounds[1], Sound.class);
    }

    /**
     * Gets the z-index of the pause menu
     * @return The z-index of the pause menu
     */
    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    /**
     * Removes the pause menu when the entity is disposed.
     */
    @Override
    public void dispose() {
        click.play(0.5f);
        closeSound.play(0.5f);
        ServiceLocator.getTimeSource().setPaused(false);
        window.clear();
        window.remove();
        super.dispose();
    }
}
