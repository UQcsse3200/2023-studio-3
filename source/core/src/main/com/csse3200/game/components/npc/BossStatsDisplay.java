package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class BossStatsDisplay extends UIComponent {

        Table table;
        private Image bossImage;
        private Label healthLabel;

        /**
         * Creates reusable ui styles and adds actors to the stage.
         */
        @Override
        public void create() {
            super.create();
            addActors();

            entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        }

        /**
         * Creates actors and positions them on the stage using a table.
         * @see Table for positioning options
         */
        private void addActors() {
            table = new Table();
            table.top().left();
            table.setFillParent(true);
            table.padTop(60f).padLeft(5f);

            // Heart image
            float bossSideLength = 30f;
            bossImage = new Image(ServiceLocator.getResourceService().getAsset("images/mobs/boss_health.png", Texture.class));

            // Health text
            int health = entity.getComponent(CombatStatsComponent.class).getHealth();
            CharSequence healthText = String.format("Health: %d", health);
            healthLabel = new Label(healthText, skin, "large");

            table.add(bossImage).size(bossSideLength).pad(5);
            table.add(healthLabel);
            stage.addActor(table);
        }

        @Override
        public void draw(SpriteBatch batch)  {
            // draw is handled by the stage
        }

        /**
         * Updates the player's health on the ui.
         * @param health player health
         */
        public void updatePlayerHealthUI(int health) {
            CharSequence text = String.format("Health: %d", health);
            healthLabel.setText(text);
        }

        @Override
        public void dispose() {
            super.dispose();
            bossImage.remove();
            healthLabel.remove();
        }
    }
