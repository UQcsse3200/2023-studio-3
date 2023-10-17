package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMenuDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table table1;
    private Sound clickSound;

    @Override
    public void create() {
        super.create();
        addActors();
        loadSounds();
    }
    private void loadSounds() {
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/Modern4.ogg"));
    }

    private void addActors() {
        /**
         * Loads a custom cursor image and sets it as the system cursor.
         *
         * This method loads an image file named "mouse_effect.png" from the "images/ui" directory
         * and sets it as the system cursor. After setting the custom cursor, it disposes of the
         * Pixmap object used for loading the cursor image to release system resources.
         *
         * @param none
         * @return none
         */
        // Load the custom cursor image
        Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("images/ui/mouse_effect.png"));
        Cursor customCursor = Gdx.graphics.newCursor(cursorPixmap, 0, 0);
        Gdx.graphics.setCursor(customCursor);
        cursorPixmap.dispose(); // Dispose of the Pixmap to release resources

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Texture backgroundTexture = ServiceLocator.getResourceService().getAsset("images/background/main_menu/main_menu_bg.png", Texture.class);
        Image title = new Image(backgroundTexture);
        title.setFillParent(true);
        table.addActorAt(0, title);
        title.setWidth(Gdx.graphics.getWidth());
        title.setHeight(Gdx.graphics.getHeight());
        title.setPosition(0, 0);

// Create a "Start" TextButton using the default style
        TextButton startBtn = ButtonFactory.createButton("Start");

// Create a "Help" TextButton using the default style
        TextButton helpBtn = ButtonFactory.createButton("Help");

// Create a "Settings" TextButton with a custom image
        TextButton settingsBtn =ButtonFactory.createButton("Settings");

// Create a "Quit" TextButton with a custom image
        TextButton exitBtn = ButtonFactory.createButton("Quit");

        // Triggers an event when the button is pressed
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("start");
                        clickSound.play();
                    }
                });

        helpBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Help button clicked");
                        entity.getEvents().trigger("help");
                        clickSound.play();
                    }
                });

        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                        clickSound.play();
                    }
                });

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                        clickSound.play();
                    }
                });

        // Proportional padding values based on original screen or background dimensions
        float originalScreenWidth = 1920;  // Replace with the original width if different
        float originalScreenHeight = 1080; // Replace with the original height if different

        float padTopStartBtn = 260f / originalScreenHeight * Gdx.graphics.getHeight();
        float padTopOtherBtns = 15f / originalScreenHeight * Gdx.graphics.getHeight();


        table.center();
        table.add(startBtn).padTop(250f).center().row();
        table.add(helpBtn).padTop(15f).center().row();
        table.add(settingsBtn).padTop(15f).center().row();
        table.add(exitBtn).padTop(15f).center().row();

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
        clickSound.dispose();
    }
}
