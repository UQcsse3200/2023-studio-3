package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
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
    private float jumpMinX;
    private float jumpMinY;
    private float jumpMaxX;
    private float jumpMaxY;
    private long time;


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
        System.out.println(currentPos);
        jump(getJumpPos());
        System.out.println(owner.getEntity().getPosition());
    }

//    @Override
//    public void update() {
//
//    }

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

    private Vector2 getJumpPos() {
        // check where demon can jump
        jumpMinX = currentPos.x - 2;
        jumpMaxX = currentPos.x + 2;
        jumpMinY = currentPos.y - 2;
        jumpMaxY = currentPos.y + 2;
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
        return new Vector2(randomX, randomY);
    }

    private long getTime() {
        return TimeUtils.millis();
    }
}
