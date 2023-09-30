package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class UIElementsDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table buttonTable = new Table();
    private Table sliderTable = new Table();
    private Table timerTable = new Table();


    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        buttonTable.top().right();
        timerTable.top().right();
        sliderTable.top();

        buttonTable.setFillParent(true);
        timerTable.setFillParent(true);
        sliderTable.setFillParent(true);

        TextButton remainingMobsButton = new ButtonFactory().createButton("Mobs left:");
        TextButton timerButton = new ButtonFactory().createButton("Next wave:");
        TextButton testSlider = new ButtonFactory().createButton("Test slider");

        //Not sure if we need a listened for a label
//        // Triggers an event when the button is pressed.
//        remainingMobsButton.addListener(
//                new ChangeListener() {
//                    @Override
//                    public void changed(ChangeEvent changeEvent, Actor actor) {
//                        logger.debug("Wave counter button clicked");
//                        entity.getEvents().trigger("wave counter");
//                    }
//                });

        buttonTable.add(remainingMobsButton).padTop(20f).padRight(10f);
        timerTable.add(timerButton).padTop(70f).padRight(10f);
        sliderTable.add(testSlider).padTop(200f);

        stage.addActor(buttonTable);
        stage.addActor(timerTable);
        stage.addActor(sliderTable);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // drawing is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        buttonTable.clear();
        timerTable.clear();
        sliderTable.clear();
        super.dispose();
    }
}
