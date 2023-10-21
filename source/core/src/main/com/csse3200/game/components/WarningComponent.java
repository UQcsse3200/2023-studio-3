package com.csse3200.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.WarningFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/** A warning component to display and manage warning signals */
public class WarningComponent extends UIComponent {
    private long startTime = ServiceLocator.getTimeSource().getTime();
    private static final Logger logger = LoggerFactory.getLogger(WarningComponent.class);
    private Image warning;
    private Vector2 position;
    private Entity mob;
    private ParallelAction action;

    /** Create and display the warning */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /** Set up the warning. */
    private void addActors() {
        // Add the initial action.
        action = new ParallelAction(
                Actions.forever(new SequenceAction(
                        Actions.fadeIn(0.5f),
                        Actions.hide(),
                        Actions.fadeIn(0.5f),
                        Actions.show()
                ))
        );
        // Get mob size for stage.
        Vector2 mobSize = convert_coordinates(mob.getScale().x, mob.getScale().y);
        // Get the warning image.
        warning = new Image(new Texture("images/ui/Sprites/UI_Glass_Select_01a1.png"));
        // Scale the warning image.
        warning.setSize(mobSize.x, mobSize.x);
        // Position the warning image.
        warning.setPosition(position.x, position.y);
        // Display the warning image.
        stage.addActor(warning);
        // Add Show and Hide Actions.
        warning.addAction(action);
    }

    /** Get the mob target and its position */
    public void config(Entity mob) {
        this.mob = mob;
        this.position = mob.getPosition();
    }
    /** Set the position variable */
    private void setPosition(Vector2 position) {
        this.position = convert_coordinates(position.x, position.y);
    }

    /** Update the warning state */
    @Override
    public void update() {
        this.setPosition(mob.getPosition());
        action.addAction(
                Actions.moveTo(position.x, position.y)
        );
    }

    /** Clear the warning */
    @Override
    public void dispose() {
        this.warning.addAction(Actions.removeActor());
        super.dispose();
    }

    /**
     * Ported from CurrencyDisplay.
     *
     * Converts grid coordinates to map coordinates.
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
