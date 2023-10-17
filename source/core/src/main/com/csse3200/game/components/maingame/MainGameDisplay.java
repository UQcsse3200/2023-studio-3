package com.csse3200.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
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
    private static final String SHORTCUT_FONT = "small";
    private final Table towerTable = new Table();
    private final Table buttonTable = new Table();
    private final Table progressTable = new Table();
    private final Table levelNameTable = new Table();
    private final String[] sounds = {
            "sounds/ui/click/click_01.ogg",
            "sounds/ui/open_close/open_01.ogg"
    };
    private String level;
    private static final String[] levels = {
            "Desert Planet",
            "Ice Planet",
            "Lava Planet"
    };
    private Sound click;
    private Sound openSound;
    private final GdxGame game;
    private Array<TowerType> towers = new Array<>();
    private final Array<ImageButton> towerButtons = new Array<>();
    private ImageButton tower1;
    private ImageButton tower2;
    private ImageButton tower3;
    private ImageButton tower4;
    private ImageButton tower5;
    private LevelProgressBar progressbar;
    private Entity pauseMenu;

    /**
     * The constructor for the display
     * @param screenSwitchHandle a handle back to the game entry point that manages screen switching
     */
    public MainGameDisplay(GdxGame screenSwitchHandle, int level) {
        game = screenSwitchHandle;
        this.level = levels[level];
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

        progressTable.top().center().setWidth(500f);
        progressTable.setFillParent(true);

        levelNameTable.center().top().padLeft(20f).padTop(20f).pad(20f);
        levelNameTable.setFillParent(true);

        // set the towerTypes if they aren't already
        setTowers();

        // Update the centrally located towerTypes list -
        ServiceLocator.setTowerTypes(towers);

        // create the tower buttons, pause button, and their associated listeners
        createTowerButtons();
        TextButton pauseBtn = createPauseButton();

        progressbar = new LevelProgressBar(500, 10);

        levelNameTable.setSkin(getSkin());
        levelNameTable.add(this.level, "default");

        // Scale all the tower build buttons down
        // Add all buttons to their respective tables and position them
        towerTable.setSkin(getSkin());
        towerTable.add(tower1).padRight(10f);
        towerTable.add(tower2).padRight(10f);
        towerTable.add(tower3).padRight(10f);
        towerTable.add(tower4).padRight(10f);
        towerTable.add(tower5).padRight(10f);
        towerTable.row();
        towerTable.add("1", SHORTCUT_FONT);
        towerTable.add("2", SHORTCUT_FONT);
        towerTable.add("3", SHORTCUT_FONT);
        towerTable.add("4", SHORTCUT_FONT);
        towerTable.add("5", SHORTCUT_FONT);
        towerTable.row().colspan(5).pad(20f);
        towerTable.add(progressbar).fillX();

        buttonTable.add(pauseBtn);

        // Add tables to the stage

        stage.addActor(buttonTable);
        stage.addActor(towerTable);
        stage.addActor(levelNameTable);

        // Animate the tower select buttons
        int tower1Gap = Gdx.graphics.getWidth() /2 + (int) towerTable.getX()/2 + 400;
        int tower2Gap = Gdx.graphics.getWidth() /2 + (int) towerTable.getX()/2 + 240;
        int tower3Gap = Gdx.graphics.getWidth() /2 + (int) towerTable.getX()/2 + 80;
        int tower4Gap = Gdx.graphics.getWidth() /2 + (int) towerTable.getX()/2 - 80;
        int tower5Gap = Gdx.graphics.getWidth() /2 + (int) towerTable.getX()/2 - 240;
        animateTowerButton(tower1, tower1Gap, 230);
        animateTowerButton(tower2, tower2Gap, 230);
        animateTowerButton(tower3, tower3Gap, 230);
        animateTowerButton(tower4, tower4Gap, 230);
        animateTowerButton(tower5, tower5Gap, 230);

        TooltipManager tm = TooltipManager.getInstance();
        tm.initialTime = 3;
        tm.hideAll();
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
        towerUpdate();
    }

    private void setTowers() {
        // Stores tower defaults, in case towers haven't been set in the tower select screen
        TowerType[] defaultTowers = {
                TowerType.TNT,
                TowerType.DROID,
                TowerType.INCOME,
                TowerType.WALL,
                TowerType.WEAPON
        };

        // Fetch the selected tower types if set
        towers = new Array<>();

        for (TowerType tower : ServiceLocator.getTowerTypes()) {
            towers.add(tower);
        }

        // If no towers set, populate with default towers
        if (towers.isEmpty() || towers.size < 5) {
//            if (towers.isEmpty()) {
//                towers.addAll(defaultTowers);
//            } else {
            for (TowerType tower : defaultTowers) {
                if (towers.size < 5 && !towers.contains(tower, true)) {
                    towers.add(tower);
                }
            }
        }
        ServiceLocator.setTowerTypes(towers);
    }

    private void createTowerButtons() {
        tower1 = new ImageButton(skin, towers.get(0).getSkinName());
        towerButtons.add(tower1);
        tower2 = new ImageButton(skin, towers.get(1).getSkinName());
        towerButtons.add(tower2);
        tower3 = new ImageButton(skin, towers.get(2).getSkinName());
        towerButtons.add(tower3);
        tower4 = new ImageButton(skin, towers.get(3).getSkinName());
        towerButtons.add(tower4);
        tower5 = new ImageButton(skin, towers.get(4).getSkinName());
        towerButtons.add(tower5);

        int i = 0;
        for (ImageButton button : towerButtons) {
            // Triggers an event when the button is pressed.
            int finalI = i;
            button.addListener(
                    new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            TowerType selected = ServiceLocator.getCurrencyService().getTower();
                            if (selected == towers.get(finalI) ) {
                                ServiceLocator.getCurrencyService().setTowerType(null);

                                towerToggle(null);

                            } else {
                                ServiceLocator.getCurrencyService().setTowerType(towers.get(finalI));

                                towerToggle(button);

                            }
                            click.play(0.4f);
                        }
                    });
            TextTooltip tower1Tooltip = new TextTooltip(towers.get(i).getDescription(), getSkin());
            button.addListener(tower1Tooltip);
            i++;
        }
    }

    private TextButton createPauseButton() {
        TextButton pauseBtn = ButtonFactory.createButton("Pause");

        // Starting animation for pause button
        pauseBtn.setPosition((float)Gdx.graphics.getWidth(), (Gdx.graphics.getHeight() - 150f));
        pauseBtn.addAction(new SequenceAction(Actions.moveTo(Gdx.graphics.getWidth() - 200f,
                Gdx.graphics.getHeight() - 150f, 1f, Interpolation.fastSlow)));

        // Spawns a pause menu when the button is pressed.
        pauseBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Pause button clicked");
                        openSound.play(0.4f);
                        pauseMenu = PauseMenuFactory.createPauseMenu(game);
                        ServiceLocator.getTimeSource().setPaused(true);
                    }
                });


        // Pause menu escape key opening listener
        stage.addListener(
                new InputListener() {
                    @Override
                    public boolean keyUp(InputEvent event, int keycode) {
                        if ((keycode == Input.Keys.ESCAPE) && !ServiceLocator.getTimeSource().getPaused()) {
                            openSound.play(0.4f);
                            pauseMenu = PauseMenuFactory.createPauseMenu(game);
                            ServiceLocator.getTimeSource().setPaused(true);
                            return true;
                        } else if ((keycode == Input.Keys.ESCAPE) && ServiceLocator.getTimeSource().getPaused()) {
                            pauseMenu.dispose();
                            return false;
                        }
                        return false;
                    }
                });
        return pauseBtn;
    }

    /**
     * Update the level progress bar value
     */
    public void updateLevelProgressBar() {
        int totalSecs = ServiceLocator.getWaveService().totalMobs() - ServiceLocator.getWaveService().remainingMobsForLevel();
        progressbar.setValue(totalSecs);
    }

    /**
     * Update function for the tower build menu buttons that is called with every draw, updates button states
     * depending on button selection and currency balance
     */
    private void towerUpdate() {
        // Check for small tower array
        if (towers.size < 5) {
            setTowers();
        }
        // no tower selected, set all to off
        if (ServiceLocator.getCurrencyService().getTower() == null) {
            // toggle all buttons to off
            towerToggle(null);
        } else {
            // for handling shortcut key selection of tower build buttons
            for (int i = 0; i < towerButtons.size; i++) {
                if (ServiceLocator.getCurrencyService().getTower() == towers.get(i)) {
                    towerToggle(towerButtons.get(i));
                }
            }
        }
        // update button state based on currency balance
        int balance = ServiceLocator.getCurrencyService().getScrap().getAmount();
        for (int i = 0; i < towerButtons.size; i++) {
            towerButtons.get(i).setDisabled(Integer.parseInt(towers.get(i).getPrice()) > balance);
        }
        updateLevelProgressBar();
    }

    /**
     * Helper method for toggling tower build buttons. The expected behaviour is that if one button is pressed,
     * all other buttons should be set to off. If a null button value is passed in then all buttons are set to off.
     * <p></p>
     * @param towerButton the ImageButton which is being set to on, all others will be toggled off. If null, all buttons
     *                    will be set to off
     */
    private void towerToggle(ImageButton towerButton) {
        if (towerButton == null) {
            // set all buttons to off, disable if isDisabled
            for (ImageButton button : towerButtons) {
                button.setChecked(false);
            }
        } else {
            // set the button corresponding to towerButton to on, all others to off
            for (ImageButton button : towerButtons) {
                button.setChecked(button == towerButton);
            }
        }
    }
    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        buttonTable.clear();
        towerTable.clear();
        unloadSounds();
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

    public void unloadSounds() {
        ServiceLocator.getResourceService().unloadAssets(sounds);
    }

    public void animateTowerButton(ImageButton button, float x, float y) {
        button.setPosition(Gdx.graphics.getWidth() - x, Gdx.graphics.getHeight());
        button.addAction(new SequenceAction(Actions.moveTo(Gdx.graphics.getWidth() - x,
                Gdx.graphics.getHeight() - y, 1f, Interpolation.fastSlow)));
    }
}