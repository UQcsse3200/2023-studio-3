package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatrickTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 PATRICK_SPEED = new Vector2(1f, 1f);

    // Private variables
    private static final Logger logger = LoggerFactory.getLogger(PatrickTask.class);
    private Vector2 currentPos;
    private PhysicsEngine physics;
    private GameTime gameTime;
    private DeathBringerState state = DeathBringerState.IDLE;
    private DeathBringerState prevState;
    private AnimationRenderComponent animation;
    private Entity patrick;
    private  enum DeathBringerState {
        IDLE, WALK, ATTACK, HURT, DEATH, CAST, SPELL
    }

    public PatrickTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void update() {

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
            case IDLE -> {

            }
            default -> logger.debug("Patrick animation {state} not found");
        }
        prevState = state;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
