package com.csse3200.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.csse3200.game.screens.TowerType;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Displays a button to represent the remaining mobs left in the current wave and a button to skip to the next wave.
 */
public class UIElementsDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(UIElementsDisplay.class);
    private static final float Z_INDEX = 2f;
    private final Table buttonTable = new Table();
    private TextButton remainingMobsButton;
    private TextButton timerButton;
    private LevelProgressBar progressbar;
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

        remainingMobsButton = ButtonFactory.createButton("Mobs:"
                + ServiceLocator.getWaveService().getEnemyCount());
        buttonTable.top().right().padTop(160f).padRight(80f);

        buttonTable.setFillParent(true);

        buttonTable.add(remainingMobsButton).right();//.padTop(10f).padRight(10f);
        buttonTable.row();
        buttonTable.add(timerButton);//.padRight(10f);

        stage.addActor(buttonTable);

        progressbar = new LevelProgressBar(500, 10);
        progressbar.setPosition(500, Gdx.graphics.getHeight() - 200);
        stage.addActor(progressbar);

        createTimerButton();
    }

    public void updateLevelProgressBar() {
        float totalSecs = (ServiceLocator.getTimeSource().getTime() / 1000);
        progressbar.setValue(totalSecs);
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
        timerButton = ButtonFactory.createButton("Next wave in:"
                + (ServiceLocator.getWaveService().getNextWaveTime() / 1000));
        buttonTable.row();
        buttonTable.add(timerButton).padRight(10f);
    }

    /**
     * This method updates the text for timer button.
     */
    public void updateTimerButton() {
        if (!(ServiceLocator.getWaveService().getGamePaused())) {
            int totalSecs = (int) ((ServiceLocator.getWaveService().getNextWaveTime()
                    - ServiceLocator.getTimeSource().getTime()) / 1000);

            // TODO : THESE SHOULD BE REMOVED AND PLACED WHEREVER THE BOSS MOB GETS SPAWNED
            if (totalSecs % 20 == 0) {
                ServiceLocator.getMapService().shakeCameraMap();
                ServiceLocator.getMapService().shakeCameraGrid();
            }
            int seconds = totalSecs % 60;
            int minutes = (totalSecs % 3600) / 60;
            String finalTime = String.format("%02d:%02d", minutes, seconds);
            if (ServiceLocator.getTimeSource().getTime() < ServiceLocator.getWaveService().getNextWaveTime()) {
                if (!findActor(timerButton)) {
                    createTimerButton();
                }
                timerButton.setText("Next wave in: " + finalTime);
            } else {
                timerButton.addAction(new SequenceAction(Actions.fadeOut(1f), Actions.removeActor()));
                stage.act();
                stage.draw();
            }
        }
    }

    /**
     * This function checks if a certain actor is present in buttonTable.
     * @param actor the actor to find in buttonTable.
     * @return true if the actor is present and false otherwise.
     */
    public boolean findActor(Actor actor) {
        for (Actor actors: buttonTable.getChildren()) {
            if (actors == actor) {
                return true;
            }
        }
        return false;
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
        progressbar.clear();
    }
}
