package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Going forward with certain speed, and switching to another land
 * Requires an entity with a PhysicsMovementComponent.
 */
public class FinalBossMovementTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(FinalBossMovementTask.class);

    private final float switchTime;
    private Vector2 startPos;
    //    private WaitTask waitTask;
    private MovementTask moveForwardTask;
    private MovementTask switchLaneTask;
    private Task currentTask;

    /**
     * @param switchTime How long in seconds to wait between switching land.
     */
    public FinalBossMovementTask(float switchTime) {
        this.switchTime = switchTime;
    }

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();

//        waitTask = new WaitTask(switchTime);
//        waitTask.create(owner);

        switchLaneTask = new MovementTask(startPos.sub(0, 2));
        switchLaneTask.create(owner);

        moveForwardTask = new MovementTask(startPos.sub(2,0));
        moveForwardTask.create(owner);

        moveForwardTask.start();
        currentTask = moveForwardTask;

        this.owner.getEntity().getEvents().trigger("finalBossMovementStart");
    }

    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == moveForwardTask) {
                startSwitchingLane();
            } else {
                startMoving();
//                startWaiting();;
            }
        }
        currentTask.update();
    }

//    private void startWaiting() {
//        logger.debug("Starting waiting for switching lane");
//        swapTask(waitTask);
//    }

    private void startSwitchingLane() {
        logger.debug("Starting switching lane");
        switchLaneTask.setTarget(startPos.sub(0, 2));
        swapTask(switchLaneTask);
    }

    private void startMoving() {
        logger.debug("Starting moving forward");
        moveForwardTask.setTarget(startPos.sub(2,0));
        swapTask(moveForwardTask);
    }

    private void swapTask(Task newTask) {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = newTask;
        currentTask.start();
    }

}
