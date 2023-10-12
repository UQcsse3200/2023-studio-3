package com.csse3200.game.components.pausemenu;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PauseMenuButtonComponent extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(PauseMenuButtonComponent.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table table2;
    private Table table3;
    private Table table4;
    private final GdxGame game;

    public PauseMenuButtonComponent(GdxGame screenSwitchHandle) {
        game = screenSwitchHandle;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Initialises the pause menu buttons
     * Positions them on the stage using a table
     */
    private void addActors() {
        //window = new Window("",);
        table = new Table();
        table.setFillParent(true);
        table2 = new Table();
        table2.setFillParent(true);
        table3 = new Table();
        table3.setFillParent(true);
        table4 = new Table();
        table4.setFillParent(true);
        TextButton continueBtn = ButtonFactory.createButton("Continue");
        TextButton settingsBtn = ButtonFactory.createButton("Settings");
        TextButton planetSelectBtn = ButtonFactory.createButton("Planet Select");
        TextButton mainMenuBtn = ButtonFactory.createButton("Main Menu");
        continueBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Continue button clicked");
                        entity.dispose();
                    }
                });
        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        game.setScreen(GdxGame.ScreenType.SETTINGS);
                    }
                });
        planetSelectBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Planet select button clicked");
                        game.setScreen(GdxGame.ScreenType.LEVEL_SELECT);
                    }
                });
        mainMenuBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Main menu button clicked");
                        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
                    }
                });
        table.add(continueBtn).padBottom(50f).padRight(200f);
        table2.add(settingsBtn).padBottom(50f).padLeft(200f);
        table3.add(planetSelectBtn).padTop(50f).padRight(200f);
        table4.add(mainMenuBtn).padTop(50f).padLeft(200f);

        stage.addActor(table);
        stage.addActor(table2);
        stage.addActor(table3);
        stage.addActor(table4);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // handled by stage
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
