package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemonBossTask extends DefaultTask implements PriorityTask {

    private static final Logger logger = LoggerFactory.getLogger(DemonBossTask.class);
    private static final int PRIORITY = 3;
    private Vector2 currentPos;
    private MovementTask movementTask;
    private final PhysicsEngine physics;
    private static final Vector2 DEMON_JUMP_SPEED = new Vector2(2f, 2f);
    private long time;
    private Vector2 jumpPos;


    private enum STATE {
        WALK, JUMP, IDLE
    }
    private STATE demonState = STATE.IDLE;

    public DemonBossTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
    }

    @Override
    public void start() {
        super.start();
        this.currentPos = owner.getEntity().getPosition();
        jump(getJumpPos());
    }

    @Override
    public void update() {
        this.currentPos = owner.getEntity().getPosition();
        if (currentPos.equals(jumpPos)) {
            logger.debug("Demon jump completed");
        }
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    private void jump(Vector2 finalPos) {
        // Start animation
        owner.getEntity().getEvents().trigger("demon_walk");
        MovementTask jump = new MovementTask(finalPos);
        jump.create(owner);
        owner.getEntity().getComponent(PhysicsMovementComponent.class).setSpeed(DEMON_JUMP_SPEED);
        logger.debug("Demon jump starting");
        jump.start();
    }

    private Vector2 getJumpPos() {
        // check where demon can jump
        float jumpMinX = currentPos.x - 2;
        float jumpMaxX = currentPos.x + 2;
        float jumpMinY = currentPos.y - 2;
        float jumpMaxY = currentPos.y + 2;

        if (jumpMinX < 1) {
            jumpMinX = 1;
        } else if (jumpMinX > 19) {
            jumpMinX = 19;
        } else if (jumpMinY < 1) {
            jumpMinX = 1;
        } else if (jumpMinY > 7) {
            jumpMinX = 7;
        }

        // generate random jump pos
        float randomX = MathUtils.random(jumpMinX, jumpMaxX);
        float randomY = MathUtils.random(jumpMinY, jumpMaxY);
        return jumpPos = new Vector2(randomX, randomY);
    }

    private long getTime() {
        return TimeUtils.millis();
    }
}
