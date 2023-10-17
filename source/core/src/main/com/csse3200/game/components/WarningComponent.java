package com.csse3200.game.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.entities.Entity;
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
    private TextButton warning;
    private Vector2 position;
    private Camera camera;
    boolean toggle = false;

    @Override
    public void create() {
        super.create();
        addActors();
    }
    public void addActors() {
        warning = new TextButton("ENEMY", getSkin());
        warning.setDisabled(true);
        warning.addAction(new SequenceAction(
                Actions.color(new Color(Color.RED)),
                Actions.parallel(Actions.fadeOut(2f), Actions.sizeBy(-1000, 0, 2f)),
                Actions.removeActor()));
        Vector2 mobPosition = convert_coordinates(position.x, position.y, camera);
        warning.setPosition(mobPosition.x, mobPosition.y);
        stage.addActor(warning);
    }

    public void config(Entity mob, Camera camera) {
        this.position = mob.getPosition();
        this.camera = camera;
    }
    @Override
    public void dispose() {
        this.warning.clear();
        super.dispose();
    }

    /**
     * Ported from CurrencyDisplay.
     */
    private Vector2 convert_coordinates(float x, float y, Camera camera) {
        Vector3 entityCoordinates = new Vector3(x, y, 0);
        Vector3 entityScreenCoordinate = camera.project(entityCoordinates);
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
