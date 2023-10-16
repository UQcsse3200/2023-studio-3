package com.csse3200.game.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarningComponent extends UIComponent {
    long startTime = ServiceLocator.getTimeSource().getTime();
    private static final Logger logger = LoggerFactory.getLogger(WarningComponent.class);
    TextButton warning;
    long spawnTime;
    float severity;
    GridPoint2 position;
    boolean toggle = false;

    @Override
    public void create() {
        super.create();
        addActors();
    }
    public void addActors() {
        warning = new TextButton("ENEMY", getSkin());
        warning.pad(20f);
        warning.addAction(new SequenceAction(
                Actions.color(new Color(Color.RED)),
                Actions.parallel(Actions.fadeOut(2f), Actions.sizeBy(1000, 0, 2f)),
                Actions.removeActor()));
        warning.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        logger.info(String.format("%d, %d", position.x, position.y));

        stage.addActor(warning);
    }

    public void config(String mobType, GridPoint2 position) {
        this.position = position;
        this.severity = mobType.contains("Boss") ? 5f : 1.5f;
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
