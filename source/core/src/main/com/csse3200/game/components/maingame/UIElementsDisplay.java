package com.csse3200.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TowerFactory;
import com.csse3200.game.screens.TowerType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Displays a button to represent the remaining mobs left in the current wave and a button to skip to the next wave.
 */
public class UIElementsDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private final Table buttonTable = new Table();
    private final Table towerTable = new Table();
    Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));
    private TextButton remainingMobsButton;
    private TextButton timerButton;
    private final int timer = 110;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * This method creates the buttons, adds them to the respective tables and draws them on the screen.
     */
    private void addActors() {
        remainingMobsButton = new ButtonFactory().createButton("Mobs:"
                + ServiceLocator.getWaveService().getEnemyCount());

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
        if (towers.isEmpty()) {
            towers.addAll(defaultTowers);
        }

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
                    }
                });

        // Triggers an event when the button is pressed.
        tower2.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 2 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(1));
                    }
                });

        tower3.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 3 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(2));
                    }
                });

        tower4.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 4 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(3));
                    }
                });

        tower5.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Tower 5 build button clicked");
                        ServiceLocator.getCurrencyService().setTowerType(towers.get(4));
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
        updateTimerButton();
    }

    /**
     * This method creates the timer button.
     */
    public void createTimerButton() {

        timerButton = new ButtonFactory().createButton("Next wave in:"
                + (ServiceLocator.getWaveService().getNextWaveTime() / 1000));
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
