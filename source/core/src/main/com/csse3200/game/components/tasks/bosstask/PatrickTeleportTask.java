package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatrickTeleportTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
    private Entity entity;
    private Vector2 location;
    private boolean isCasting = false;
    private PatrickState state;
    private AnimationRenderComponent animation;
    private Status status;
    private enum PatrickState {
        CAST, APPEAR, SPELL
    }

    public PatrickTeleportTask(Entity entity, Vector2 location) {
        this.entity = entity;
        this.location = location;
    }

    @Override
    public void start() {
        super.start();
        animation = owner.getEntity().getComponent(AnimationRenderComponent.class);
        owner.getEntity().getEvents().trigger("patrick_cast");
        state = PatrickState.CAST;
        status = Status.ACTIVE;
    }

    @Override
    public void update() {
        switch (state) {
            case CAST -> {
                if (animation.isFinished()) {
                    ServiceLocator.getEntityService().unregister(entity);
                    owner.getEntity().getEvents().trigger("patrick_spell");
                    state = PatrickState.SPELL;
                }
            }
            case SPELL -> {
                if (animation.isFinished()) {
                    ServiceLocator.getEntityService().register(entity);
                    owner.getEntity().getEvents().trigger("patrick_appear");
                    state = PatrickState.APPEAR;
                }
            }
            case APPEAR -> {
                if (animation.isFinished()) {
                    status = Status.FINISHED;
                }
            }
        }
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
