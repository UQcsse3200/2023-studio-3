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

/**
 * Displays a button to represent the remaining mobs left in the current wave and a button to skip to the next wave.
 */
public class UIElementsDisplay extends UIComponent {
    private static final float Z_INDEX = 2f;
    private final Table buttonTable = new Table();
    private TextButton remainingMobsButton;
    private TextButton timerButton;
    private long time = 0;

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
        remainingMobsButton.addAction(new SequenceAction(Actions.moveTo(Gdx.graphics.getWidth() - 217f,
                Gdx.graphics.getHeight() - 230f, 1f, Interpolation.fastSlow)));

        buttonTable.top().right().padTop(160f).padRight(80f);
        buttonTable.setFillParent(true);

        buttonTable.add(remainingMobsButton).right();


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
        timerButton.addAction(new SequenceAction(Actions.moveTo(Gdx.graphics.getWidth() - 435f,
                Gdx.graphics.getHeight() - 300f, 1f, Interpolation.fastSlow)));
        timerButton.setDisabled(true);
        buttonTable.row();
        buttonTable.add(timerButton);
    }

    /**
     * This method updates the text for timer button.
     */
    public void updateTimerButton() {
        if (!(ServiceLocator.getWaveService().getGamePaused())) {
            int totalSecs = (int) ((ServiceLocator.getWaveService().getNextWaveTime()
                    - ServiceLocator.getTimeSource().getTime()) / 1000);

            int seconds = totalSecs % 60;
            int minutes = (totalSecs % 3600) / 60;
            String finalTime = String.format("%02d:%02d", minutes, seconds);
            if (ServiceLocator.getTimeSource().getTime() < ServiceLocator.getWaveService().getNextWaveTime()) {
                if (!findActor(timerButton)) {
                    remainingMobsButton.setDisabled(false);
                    createTimerButton();
                }
                timerButton.setText("Next wave in: " + finalTime);
                time = ServiceLocator.getTimeSource().getTime();
            } else {
                if (ServiceLocator.getTimeSource().getTime() <  time + 2000) {
                    ServiceLocator.getMapService().shakeCameraMap();
                    ServiceLocator.getMapService().shakeCameraGrid();
                }
                remainingMobsButton.setDisabled(true);
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
