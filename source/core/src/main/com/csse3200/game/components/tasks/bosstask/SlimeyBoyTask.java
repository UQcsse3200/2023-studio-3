package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlimeyBoyTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 SLIMEY_SPEED = new Vector2(0.5f, 0.5f);

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(SlimeyBoyTask.class);
    private Entity slimey;
    private AnimationRenderComponent animation;
    private Vector2 currentPos;
    private SlimeState state = SlimeState.IDLE;
    private SlimeState prevState;

    private enum SlimeState {
        IDLE, MOVE, PROJECTILE_EXPLOSION, PROJECTILE_IDLE, TAKE_HIT, TRANSFORM
    }

    @Override
    public void start() {
        super.start();
        slimey = owner.getEntity();
        animation = owner.getEntity().getComponent(AnimationRenderComponent.class); // get animation
        currentPos = owner.getEntity().getPosition(); // get current position
        slimey.getComponent(PhysicsMovementComponent.class).setSpeed(SLIMEY_SPEED); // set speed
        changeState(SlimeState.IDLE);
    }

    /**
     * Changes the state of the demon
     * @param state state to be changed to
     */
    private void changeState(SlimeState state) {
        prevState = this.state;
        this.state = state;
    }

    /**
     * Changes the animation of the demon if a state change occurs
     */
    private void animate() {
        // Check if same animation is being called
        if (prevState.equals(state)) {
            return; // skip rest of function
        }

        switch (state) {
            case IDLE -> slimey.getEvents().trigger("idle");
            case MOVE -> slimey.getEvents().trigger("move");
            case PROJECTILE_EXPLOSION -> slimey.getEvents().trigger("projectile_explosion");
            case PROJECTILE_IDLE -> slimey.getEvents().trigger("projectile_idle");
            case TAKE_HIT -> slimey.getEvents().trigger("take_hit");
            case TRANSFORM -> slimey.getEvents().trigger("transform");
            default -> logger.debug("Slimey boy animation {state} not found");
        }
        prevState = state;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
