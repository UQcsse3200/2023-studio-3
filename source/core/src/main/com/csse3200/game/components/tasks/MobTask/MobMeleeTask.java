package com.csse3200.game.components.tasks.MobTask;

import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.bosstask.PatrickTask;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class MobMeleeTask extends DefaultTask implements PriorityTask {

    // Constants
    private static final int PRIORITY = 3;
    MobType mobType;
    State state;
    private enum State {
        RUN, ATTACK, DEATH, DEFAULT
    }

    public MobMeleeTask(MobType mobType) {
        this.mobType = mobType;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

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

    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
