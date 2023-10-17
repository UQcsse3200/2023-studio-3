package com.csse3200.game.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.WarningFactory;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarningComponent extends UIComponent {
    private long startTime = ServiceLocator.getTimeSource().getTime();
    private static final Logger logger = LoggerFactory.getLogger(WarningComponent.class);
    private Image warning;
    private Vector2 position;
    private Entity mob;
    private ParallelAction action = new ParallelAction(
            Actions.forever(new SequenceAction(
                    Actions.fadeIn(0.5f),
                    Actions.hide(),
                    Actions.fadeIn(0.5f),
                    Actions.show()
            ))
    );
    boolean toggle = false;

    @Override
    public void create() {
        super.create();
        addActors();
    }
    public void addActors() {
        Vector2 mobSize = convert_coordinates(mob.getScale().x, mob.getScale().y);
        warning = new Image(new Texture("images/ui/Sprites/UI_Glass_Select_01a1.png"));
        warning.setSize(mobSize.x, mobSize.x);
        warning.setPosition(position.x, position.y);
        stage.addActor(warning);
        warning.addAction(action);
    }

    public void config(Entity mob) {
        this.mob = mob;
        this.position = mob.getPosition();
    }
    private void setPosition(Vector2 mobPosition) {
        this.position = convert_coordinates(mobPosition.x, mobPosition.y);
    }

    @Override
    public void update() {
        this.setPosition(mob.getPosition());
        action.addAction(
                Actions.moveTo(position.x, position.y)
        );
    }

    @Override
    public void dispose() {
        this.warning.addAction(Actions.removeActor());
        super.dispose();
    }

    /**
     * Ported from CurrencyDisplay.
     */
    private Vector2 convert_coordinates(float x, float y) {
        Vector3 entityCoordinates = new Vector3(x, y, 0);
        Vector3 entityScreenCoordinate = WarningFactory.camera.project(entityCoordinates);
        Vector2 stageCoordinates = stage.screenToStageCoordinates(
                new Vector2(entityScreenCoordinate.x, entityScreenCoordinate.y));
        stage.getViewport().unproject(stageCoordinates);
        return stageCoordinates;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Implemented by parent class
    }
}
