package com.csse3200.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.event.ChangeEvent;
import java.security.Provider;


/**
 * Displays a button to represent the remaining mobs left in the current wave and a button to skip to the next wave.
 */
public class UIElementsDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(UIElementsDisplay.class);
    private static final float Z_INDEX = 2f;
    private final Table buttonTable = new Table();
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

        remainingMobsButton = ButtonFactory.createButton("Mobs:"
                + ServiceLocator.getWaveService().getEnemyCount());

        remainingMobsButton.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 230f);
        remainingMobsButton.addAction(new SequenceAction(Actions.moveTo(Gdx.graphics.getWidth() - 218f,
                Gdx.graphics.getHeight() - 230f, 1f, Interpolation.fastSlow)));

        buttonTable.top().right().padTop(130f).padRight(80f);
        buttonTable.setFillParent(true);

        buttonTable.add(remainingMobsButton).right();
        buttonTable.row();
        buttonTable.add(timerButton);

        stage.addActor(buttonTable);

        createTimerButton();
    }

    /**
     * This method updates the mob count button as mobs die in the game
     */
    public void updateMobCount() {
        remainingMobsButton.setText("Mobs:" + ServiceLocator.getWaveService().getEnemyCount());
        remainingMobsButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        ServiceLocator.getWaveService().toggleDelay();
                    }
                }
        );
    }

    /**
     * This method creates the timer button.
     */
    public void createTimerButton() {

        timerButton = ButtonFactory.createButton("Next wave in:"
                + (ServiceLocator.getWaveService().getNextWaveTime() / 1000));
        timerButton.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 300f);
        timerButton.addAction(new SequenceAction(Actions.moveTo(Gdx.graphics.getWidth() - 445f,
                Gdx.graphics.getHeight() - 300f, 1f, Interpolation.fastSlow)));
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
    }
}
