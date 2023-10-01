package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
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

    @Override
    public void create() {
        super.create();
        addActors();
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
        table1 = new Table();
        table.setFillParent(true);
        table1.setFillParent(true);

        Image title =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/background/background1.png", Texture.class));
        title.setWidth(Gdx.graphics.getWidth());
        title.setHeight(Gdx.graphics.getHeight());
        title.setPosition(0, 0);

        // Create an instance of the ButtonFactory class
        ButtonFactory buttonFactory = new ButtonFactory();

// Create a "Start" TextButton using the default style
        TextButton startBtn = new TextButton("Start", new Skin(Gdx.files.internal("images/ui/buttons/glass.json")));
// Create a "Help" TextButton using the default style
        TextButton loadBtn = new TextButton("Help", new Skin(Gdx.files.internal("images/ui/buttons/glass.json")));
// Create a "Settings" TextButton with a custom image
        TextButton settingsBtn = new TextButton("Settings", new Skin(Gdx.files.internal("images/ui/buttons/glass.json")));
// Create a "Quit" TextButton with a custom image
        TextButton exitBtn = new TextButton("Quit", new Skin(Gdx.files.internal("images/ui/buttons/glass.json")));

        // Triggers an event when the button is pressed
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("start");
                    }
                });

        loadBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Load button clicked");
                        entity.getEvents().trigger("load");
                    }
                });

        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        table.add(title);
        table1.row();
        table1.add(startBtn).padTop(30f);
        table1.row();
        table1.add(loadBtn).padTop(15f);
        table1.row();
        table1.add(settingsBtn).padTop(15f);
        table1.row();
        table1.add(exitBtn).padTop(15f);

        stage.addActor(table);
        stage.addActor(table1);
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
