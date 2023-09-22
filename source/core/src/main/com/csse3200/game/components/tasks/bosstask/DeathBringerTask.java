package com.csse3200.game.components.tasks.bosstask;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

public class DeathBringerTask extends DefaultTask implements PriorityTask {
    private static final int PRIORITY = 3;
    private PhysicsEngine physics;
    private GameTime gameTime;
    private  enum DeathBringerState {
        IDLE, WALK, ATTACK, HURT, DEATH, CAST, SPELL
    }

    public DeathBringerTask() {
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

    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
