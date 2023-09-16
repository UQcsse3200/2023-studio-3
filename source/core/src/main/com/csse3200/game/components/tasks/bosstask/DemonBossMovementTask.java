package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemonBossMovementTask extends DefaultTask implements PriorityTask {

    private static final Logger logger = LoggerFactory.getLogger(DemonBossMovementTask.class);
    private static final int PRIORITY = 3;
    private Vector2 currentPos;
    private MovementTask movementTask;
    private final PhysicsEngine physics;
    private static final Vector2 DEMON_JUMP_SPEED = new Vector2(2f, 2f);

    private enum STATE {
        WALK, JUMP, IDLE
    }
    private STATE demonState = STATE.IDLE;

    public DemonBossMovementTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
    }

    @Override
    public void start() {
        super.start();
        this.currentPos = owner.getEntity().getPosition();
    }

    @Override
    public void update() {

    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    private void jump(Vector2 finalPos) {
        MovementTask jump = new MovementTask(finalPos);
        jump.create(owner);
        owner.getEntity().getComponent(PhysicsMovementComponent.class).setSpeed(DEMON_JUMP_SPEED);
        jump.start();
    }
}
