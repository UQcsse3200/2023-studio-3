package com.csse3200.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
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
    Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    private TextButton remainingMobsButton;
    private TextButton timerButton;

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

        TextButton tower1 = new TextButton("Tower 1", skin);
        TextButton tower2 = new TextButton("Tower 2", skin);
        TextButton tower3 = new TextButton("Tower 3", skin);
        TextButton tower4 = new TextButton("Tower 4", skin);
        TextButton tower5 = new TextButton("Tower 5", skin);

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
    }

    /**
     * This method updates the mob count button as mobs die in the game
     */
    public void updateMobCount() {
        remainingMobsButton.setText("Mobs:" + ServiceLocator.getWaveService().getEnemyCount());
        if (ServiceLocator.getTimeSource().getTime() < ServiceLocator.getWaveService().getNextWaveTime()) {
            createTimerButton();
        }
    }

    public void createTimerButton() {
        timerButton = new ButtonFactory().createButton("Next wave in:"
                + (ServiceLocator.getWaveService().getNextWaveTime() / 1000));
        buttonTable.row();
        buttonTable.add(timerButton).padRight(10f);
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
