package com.csse3200.game.screens.HelpScreen;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The `TowerSelectionInstruct` class represents a UI component that displays instructions for tower selection in the game.
 */

public class TowerSelectionInstruct extends UIComponent{

    /**
     * Displays a button in the pause menu to resume the game and put away the pause menu.
     */
        private static final float Z_INDEX = 2f;
        private Table table1;
        private GdxGame game;
    /**
     * Creates the UI component and adds necessary actors.
     */

        @Override
        public void create() {
            super.create();
            addActors();
        }

        private void addActors() {

            table1 = new Table();
            table1.setFillParent(true);

            Label instructionsLabel = new Label("Select towers you want to use and place them on the " +
                    "tile you want...Then press continue", skin);
            table1.add(instructionsLabel);

            // Set alignment and padding for the Label within the cell (you can adjust this as needed)
            instructionsLabel.setAlignment(Align.center);

            stage.addActor(table1);
            table1.top().left();
            table1.padTop(345f).padLeft(80f);

        }

        @Override
        public void draw(SpriteBatch batch) {
            // draw is handled by the renderer component
        }

        @Override
        public float getZIndex() {
            return Z_INDEX;
        }

        @Override
        public void dispose() {
            table1.clear();
            super.dispose();
        }

    }

