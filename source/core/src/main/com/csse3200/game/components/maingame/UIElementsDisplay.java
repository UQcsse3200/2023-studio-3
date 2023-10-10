package com.csse3200.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.screens.TowerType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Displays a button to represent the remaining mobs left in the current wave and a button to skip to the next wave.
 */
public class UIElementsDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private final Table buttonTable = new Table();
    private final Table towerTable = new Table();
    Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));
    private String[] sounds = {
            "sounds/ui/Click/NA_SFUI_Vol1_Click_01.ogg",
            "sounds/ui/Hover/NA_SFUI_Vol1_hover_01.ogg"
    };
    private Sound click;
    private Sound hover;
    private TextButton remainingMobsButton;
    private TextButton timerButton;
    private final int timer = 110;

    @Override
    public void create() {
        super.create();
        addActors();
        loadSounds();
    }

    /**
     * This method creates the buttons, adds them to the respective tables and draws them on the screen.
     */
    private void addActors() {
//        remainingMobsButton = new ButtonFactory().createButton("Mobs:"
//                + ServiceLocator.getWaveService().getEnemyCount());
        remainingMobsButton = new TextButton("Mobs:"
                + ServiceLocator.getWaveService().getEnemyCount(), skin);
        buttonTable.top().right();
        towerTable.top();

        buttonTable.setFillParent(true);
        towerTable.setFillParent(true);

        towerTable.setDebug(true);
        towerTable.padTop(50f);

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
        TextButton tower1 = new TextButton(towers.get(0).getTowerName(), skin);
        TextButton tower2 = new TextButton(towers.get(1).getTowerName(), skin);
        TextButton tower3 = new TextButton(towers.get(2).getTowerName(), skin);
        TextButton tower4 = new TextButton(towers.get(3).getTowerName(), skin);
        TextButton tower5 = new TextButton(towers.get(4).getTowerName(), skin);

        // Triggers an event when the button is pressed.
        tower1.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 1 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(0));
                        click.play(0.4f);
                    }
                });

        // Triggers an event when the button is pressed.
        tower2.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 2 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(1));
                        click.play(0.4f);
                    }
                });

        tower3.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 3 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(2));
                        click.play(0.4f);
                    }
                });

        tower4.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 4 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(3));
                        click.play(0.4f);
                    }
                });

        tower5.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 5 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(4));
                        click.play(0.4f);
                    }
                });

        buttonTable.add(remainingMobsButton).padTop(10f).padRight(10f);
        buttonTable.row();
        buttonTable.add(timerButton).padRight(10f);

        towerTable.add(tower1).padRight(10f);
        towerTable.add(tower2).padRight(10f);
        towerTable.add(tower3).padRight(10f);
        towerTable.add(tower4).padRight(10f);
        towerTable.add(tower5).padRight(10f);

        stage.addActor(buttonTable);
        stage.addActor(towerTable);

        createTimerButton();
    }

    /**
     * This method updates the mob count button as mobs die in the game
     */
    public void updateMobCount() {
        remainingMobsButton.setText("Mobs:" + ServiceLocator.getWaveService().getEnemyCount());
    }

    /**
     * This method creates the timer button.
     */
    public void createTimerButton() {

//        timerButton = new ButtonFactory().createButton("Next wave in:"
//                + (ServiceLocator.getWaveService().getNextWaveTime() / 1000));
        timerButton = new TextButton("Next wave in:"
                + (ServiceLocator.getWaveService().getNextWaveTime() / 1000), skin);
        buttonTable.row();
        buttonTable.add(timerButton).padRight(10f);
    }

    /**
     * This method updates the text for timer button.
     */
    public void updateTimerButton() {
        int totalSecs = (int) (timer - (ServiceLocator.getTimeSource().getTime() / 1000));
        int seconds = totalSecs % 60;
        int minutes = (totalSecs % 3600) / 60;
        String finalTime = String.format("%02d:%02d", minutes, seconds);
        timerButton.setText("Next wave in:" + finalTime);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // drawing is handled by the stage
    }

    /**
     * @return returns the Z_INDEX for this display
     */
    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    private void loadSounds() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        click = ServiceLocator.getResourceService().getAsset(sounds[0], Sound.class);
        hover = ServiceLocator.getResourceService().getAsset(sounds[1], Sound.class);
    }

    /**
     * Disposes off the tables and buttons created using this display
     */
    @Override
    public void dispose() {
        super.dispose();
        buttonTable.clear();
        towerTable.clear();
    }
}
