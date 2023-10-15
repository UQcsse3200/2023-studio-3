package com.csse3200.game.components.maingame;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.factories.PauseMenuFactory;
import com.csse3200.game.screens.TowerType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains the player interactive buttons in the main game, positions them
 * on the screen, handles their behaviour and sound effects.
 */
public class MainGameDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameDisplay.class);
    private static final float Z_INDEX = 2f;
    private final Table towerTable = new Table();
    private final Table buttonTable = new Table();
    private String[] sounds = {
            "sounds/ui/click/click_01.ogg",
            "sounds/ui/open_close/open_01.ogg"
    };
    private Sound click;
    private Sound openSound;
    private GdxGame game;

    /**
     * The constructor for the display
     * @param screenSwitchHandle a handle back to the game entry point that manages screen switching
     */
    public MainGameDisplay(GdxGame screenSwitchHandle) {
        game = screenSwitchHandle;
    }

    /**
     * Creation method for the class
     */
    @Override
    public void create() {
        super.create();
        addActors();
        loadSounds();
    }

    /**
     * Adds all the UI element on screen actors. Button creation, addition to tables, and button listeners exist in here
     */
    private void addActors() {

        // Create and position the tables that will hold the buttons.

        // Contains the tower build menu buttons
        towerTable.top().padTop(80f);
        towerTable.setFillParent(true);

        // Contains other buttons (just pause at this stage)
        buttonTable.top().right().padTop(80f).padRight(80f);
        buttonTable.setFillParent(true);

        // Stores tower defaults, in case towers haven't been set in the tower select screen
        TowerType[] defaultTowers = {
                TowerType.TNT,
                TowerType.DROID,
                TowerType.INCOME,
                TowerType.WALL,
                TowerType.WEAPON
        };

        // Fetch the selected tower types if set
        Array<TowerType> towers = new Array<>();

        for (TowerType tower : ServiceLocator.getTowerTypes()) {
            towers.add(tower);
        }

        // If no towers set, populate with default towers
        if (towers.isEmpty() || towers.size < 5) {
            if (towers.isEmpty()) {
                towers.addAll(defaultTowers);
            } else {
                for (TowerType tower : defaultTowers) {
                    if (towers.size < 5 && !towers.contains(tower, false)) {
                        towers.add(tower);
                    }
                }
            }
        }

        // Update the centrally located towerTypes list -
        ServiceLocator.setTowerTypes(towers);

        // Create the buttons - TODO This needs overhauling to pretty buttons
        TextButton tower1 = ButtonFactory.createButton(towers.get(0).getTowerName());
        TextButton tower2 = ButtonFactory.createButton(towers.get(1).getTowerName());
        TextButton tower3 = ButtonFactory.createButton(towers.get(2).getTowerName());
        TextButton tower4 = ButtonFactory.createButton(towers.get(3).getTowerName());
        TextButton tower5 = ButtonFactory.createButton(towers.get(4).getTowerName());
        TextButton pauseBtn = ButtonFactory.createButton("Pause");

        // Spawns a pause menu when the button is pressed.
        pauseBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Pause button clicked");
                        openSound.play(0.4f);
                        PauseMenuFactory.createPauseMenu(game);
                    }
                });

        // Triggers an event when the button is pressed.
        tower1.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        TowerType selected = ServiceLocator.getCurrencyService().getTower();
                        if (selected == towers.get(0) ) {
                            ServiceLocator.getCurrencyService().setTowerType(null);
                        } else {
                            ServiceLocator.getCurrencyService().setTowerType(towers.get(0));
                        }
                        click.play(0.4f);
                    }
                });
        TextTooltip tower1Tooltip = new TextTooltip(towers.get(0).getDescription(), getSkin());
        tower1.addListener(tower1Tooltip);

        // Triggers an event when the button is pressed.
        tower2.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        TowerType selected = ServiceLocator.getCurrencyService().getTower();
                        if (selected == towers.get(1) ) {
                            ServiceLocator.getCurrencyService().setTowerType(null);
                        } else {
                            ServiceLocator.getCurrencyService().setTowerType(towers.get(1));
                        }
                        click.play(0.4f);
                    }
                });
        TextTooltip tower2Tooltip = new TextTooltip(towers.get(1).getDescription(), getSkin());
        tower2.addListener(tower2Tooltip);

        tower3.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 3 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(2));
                        click.play(0.4f);
                    }
                });
        TextTooltip tower3Tooltip = new TextTooltip(towers.get(3).getDescription(), getSkin());
        tower3.addListener(tower3Tooltip);

        tower4.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 4 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(3));
                        click.play(0.4f);
                    }
                });
        TextTooltip tower4Tooltip = new TextTooltip(towers.get(3).getDescription(), getSkin());
        tower4.addListener(tower4Tooltip);

        tower5.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 5 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(4));
                        click.play(0.4f);
                    }
                });
        TextTooltip tower5Tooltip = new TextTooltip(towers.get(4).getDescription(), getSkin());
        tower5.addListener(tower5Tooltip);

        // Add all buttons to their respective tables and position them
        towerTable.add(tower1).padRight(10f);
        towerTable.add(tower2).padRight(10f);
        towerTable.add(tower3).padRight(10f);
        towerTable.add(tower4).padRight(10f);
        towerTable.add(tower5).padRight(10f);
        buttonTable.add(pauseBtn);

        // Add tables to the stage
        stage.addActor(buttonTable);
        stage.addActor(towerTable);

        TooltipManager tm = TooltipManager.getInstance();
        tm.initialTime = 3;
        tm.hideAll();
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
        buttonTable.clear();
        towerTable.clear();
        super.dispose();
    }

    /**
     * Loads sound assets for use in the class
     */
    public void loadSounds() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        click = ServiceLocator.getResourceService().getAsset(sounds[0], Sound.class);
        openSound = ServiceLocator.getResourceService().getAsset(sounds[1], Sound.class);
    }
}
