package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.components.tasks.WaitTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class RangeBossTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(RangeBossTask.class);

    private final float waitTime;
    private Vector2 currentPos;
    private MovementTask movementTask;
    private WaitTask waitTask;
    private Task currentTask;
    /** Animation event names */
    private static final String START = "startMobBoss";
    private static final String FINAL = "startMobBossFinal";
    private enum STATE {
        START, FINAL
    }
    private PhysicsEngine physics;
    private static final short TARGET = PhysicsLayer.TOWER;
    private final RaycastHit hit = new RaycastHit();
    private STATE bossBallState = STATE.START;

    /**
     * @param waitTime    How long in seconds to wait between wandering.
     */
    public RangeBossTask(float waitTime) {
        this.waitTime = waitTime;
        physics = ServiceLocator.getPhysicsService().getPhysics();
    }

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        currentPos = owner.getEntity().getPosition();

        waitTask = new WaitTask(waitTime);
        waitTask.create(owner);
        movementTask = new MovementTask(currentPos.sub(2,0));
        movementTask.create(owner);

        movementTask.start();
        owner.getEntity().getEvents().trigger("walkStart");
        currentTask = movementTask;

        this.owner.getEntity().getEvents().trigger("rangeBossMovementStart");
    }

    public void switchMobBossBallState() {
        if (bossBallState == STATE.START) {
            owner.getEntity().getEvents().trigger(FINAL);
            bossBallState = STATE.FINAL;
        }
    }

    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == movementTask) {
                if (towerAhead() || engineerAhead()) {
                    owner.getEntity().getEvents().trigger("chargingStart");
                    Entity newProjectile = ProjectileFactory.createBossBall(PhysicsLayer.HUMANS, new Vector2(0,currentPos.y), new Vector2(2f,2f));
                    newProjectile.setScale(-1.3f, 0.82f);
                    newProjectile.setPosition((currentPos.x), (currentPos.y));
                    ServiceLocator.getEntityService().register(newProjectile);
                    this.owner.getEntity().getEvents().trigger("attack1Start");
                }
                owner.getEntity().getEvents().trigger(START);
                switchMobBossBallState();
                startWaiting();
            } else {
                startMoving();
            }
        }
        currentTask.update();
    }

    private void startWaiting() {
        logger.debug("Starting waiting");
        owner.getEntity().getEvents().trigger("idleStart");
        swapTask(waitTask);
    }

    private void startMoving() {
        logger.debug("Starting moving");
        owner.getEntity().getEvents().trigger("walkStart");
        owner.getEntity().getEvents().trigger("attack1Start");
        movementTask.setTarget(currentPos.sub(2,0));
        swapTask(movementTask);
    }

    private void swapTask(Task newTask) {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = newTask;
        currentTask.start();
    }

    private boolean towerAhead() {
        return physics.raycast(currentPos, new Vector2(0, currentPos.y), TARGET, hit);
    }
    private boolean engineerAhead() {
        return physics.raycast(currentPos, new Vector2(0, currentPos.y), PhysicsLayer.ENGINEER, hit);
    }
}
