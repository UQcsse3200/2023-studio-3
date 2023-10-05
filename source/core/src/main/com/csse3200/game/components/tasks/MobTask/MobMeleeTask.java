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

                }
            }
        }
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
