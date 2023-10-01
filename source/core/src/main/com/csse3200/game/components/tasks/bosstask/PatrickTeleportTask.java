package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatrickTeleportTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
    private Entity patrick;
    private final Vector2 location;
    private PatrickState state = PatrickState.IDLE;
    private PatrickState prevState;
    private AnimationRenderComponent animation;
    private Status status = Status.INACTIVE;
    private CombatStatsComponent combatStats;
    private int health;
    private enum PatrickState {
        CAST, APPEAR, SPELL, IDLE
    }

    public PatrickTeleportTask(Entity patrick, Vector2 location) {
        this.patrick = patrick;
        this.location = location;
    }

    @Override
    public void start() {
        super.start();
        animation = owner.getEntity().getComponent(AnimationRenderComponent.class);
        combatStats = owner.getEntity().getComponent(CombatStatsComponent.class);
        health = combatStats.getHealth();
        changeState(PatrickState.CAST);
    }

    @Override
    public void update() {
        animate();

        switch (state) {
            case CAST -> {
                if (animation.isFinished()) {
                    patrick.setPosition(location);
                    changeState(PatrickState.SPELL);
                }
            }
            case SPELL -> {
                if (animation.isFinished()) {
                    changeState(PatrickState.APPEAR);
                }
            }
            case APPEAR -> {
                if (animation.isFinished()) {
                    combatStats.setHealth(health); // set health to health before teleport
                    status = Status.FINISHED;
                }
            }
        }
    }

    /**
     * Changes the state of patrick
     * @param state state to be changed to
     */
    private void changeState(PatrickState state) {
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
            case SPELL -> owner.getEntity().getEvents().trigger("patrick_spell");
            case APPEAR -> owner.getEntity().getEvents().trigger("patrick_death");
            case CAST -> owner.getEntity().getEvents().trigger("patrick_cast");
            default -> logger.debug("Patrick animation {state} not found");
        }
        prevState = state;
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
