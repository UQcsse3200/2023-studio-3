package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarningComponent extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(WarningComponent.class);
    TextButton warning;
    long spawnTime;
    float severity;
    GridPoint2 position;
    boolean toggle = false;
    public WarningComponent(String mobType, GridPoint2 position) {
        System.out.println("Warned");
        this.position = position;
        this.spawnTime = ServiceLocator.getTimeSource().getTime();
        this.severity = mobType.contains("Boss") ? 5f : 1.5f;
//        this.warning = new Label("!", getSkin());
        this.warning = ButtonFactory.createButton("Next wave in:"
                + (ServiceLocator.getWaveService().getNextWaveTime() / 1000));
        this.warning.setPosition(position.x - 10, position.y);
    }
    @Override
    public void update() {
        System.out.println("hi");
        if (ServiceLocator.getTimeSource().getTime() > this.spawnTime + 333) {
            this.warning.setVisible(toggle);
            toggle = !toggle;
        }
        if (ServiceLocator.getTimeSource().getTime() > this.spawnTime + 2000) {
            this.dispose();
        }
    }
    @Override
    public void dispose() {
        this.warning.clear();
        super.dispose();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Implemented by parent class
    }
}
