package com.csse3200.game.components.tasks.MobTask;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.components.tasks.bosstask.PatrickTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class MobMeleeTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    private static final Vector2 MELEE_MOB_SPEED = new Vector2(1f,1f);
    private static final Vector2 MELEE_RANGE_SPEED = new Vector2(0.7f,0.7f);

    // Private variables
    MobType mobType;
    State state = State.DEFAULT;
    State prevState;
    Entity mob;
    AnimationRenderComponent animation;
    MovementTask movementTask;
    boolean melee;

    // Enums
    private enum State {
        RUN, ATTACK, DEATH, DEFAULT
    }

    public MobMeleeTask(MobType mobType) {
        this.mobType = mobType;
    }

    @Override
    public void start() {
        super.start();
        mob = owner.getEntity();
        animation = mob.getComponent(AnimationRenderComponent.class);
        mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_MOB_SPEED);
        melee = mobType.isMelee();

        movementTask = new MovementTask(new Vector2(0f, mob.getPosition().y));
        movementTask.create(owner);
        movementTask.start();

        if (melee) {
            mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_MOB_SPEED);
        } else {
            mob.getComponent(PhysicsMovementComponent.class).setSpeed(MELEE_RANGE_SPEED);
        }
    }

    @Override
    public void update() {
        animate();

        switch (state) {
            case RUN -> {

            }
        }
    }

    private void animate() {
        switch (mobType) {
            case SKELETON -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("skeleton_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("skeleton_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("skeleton_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("skeleton_default");
                }
            }
            case WIZARD -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("wizard_run");
                    case ATTACK -> owner.getEntity().getEvents().trigger("wizard_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("wizard_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
            case WATER_QUEEN -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("water_queen_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("water_queen_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("water_queen_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
            case WATER_SLIME -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("water_slime_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("water_slime_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("water_slime_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
            case FIRE_WORM -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("fire_worm_walk");
                    case ATTACK -> owner.getEntity().getEvents().trigger("fire_worm_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("fire_worm_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
            case DRAGON_KNIGHT -> {
                switch (state) {
                    case RUN -> owner.getEntity().getEvents().trigger("dragon_knight_run");
                    case ATTACK -> owner.getEntity().getEvents().trigger("dragon_knight_attack");
                    case DEATH -> owner.getEntity().getEvents().trigger("dragon_knight_death");
                    case DEFAULT -> owner.getEntity().getEvents().trigger("default");
                }
            }
        }
    }

    private void changeState(State state) {
        prevState = this.state;
        this.state = state;
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
