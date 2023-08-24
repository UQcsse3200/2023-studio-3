package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TowerCombatTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
    private final int priority;
    private final float maxRange;
    private Vector2 towerPosition = new Vector2(10,10);
    private final Vector2 maxRangePosition = new Vector2();
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();


    /**
     * @param priority Task priority when chasing (0 when not chasing).
     * @param maxRange Maximum effective range of the weapon tower.
     */
    public TowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        this.maxRangePosition.set(towerPosition.x + maxRange, towerPosition.y);
        physics = ServiceLocator.getPhysicsService().getPhysics();
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        logger.debug("TowerCombatTask started");
    }

    @Override
    public void start() {
        super.start();
        this.towerPosition = owner.getEntity().getCenterPosition();
        owner.getEntity().getEvents().trigger("deployStart");
    }

    @Override
    public void update() {
        if (status == Status.ACTIVE) {
            owner.getEntity().getEvents().trigger("firingStart");
            logger.debug("firing start event should have been triggered");
        } else {
            owner.getEntity().getEvents().trigger("stowStart");
            logger.debug("stow start event should have been triggered");
        }
    }

    @Override
    public void stop() {
        super.stop();
        owner.getEntity().getEvents().trigger("stowStart");
    }

    @Override
    public int getPriority() {
        if (isTargetVisible()) {
            return getActivePriority();
        }
        status = Status.FINISHED;
        return getInactivePriority();
    }

    private float getDistanceToTarget() {
        // TODO change layer to detect
        if (physics.raycast(towerPosition, maxRangePosition, PhysicsLayer.PLAYER, hit)) {
            return towerPosition.dst(hit.point.x, hit.point.y);
        };
        return -1;
    }

    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > maxRange || !isTargetVisible()) {
            return -1; // Too far, stop firing
        }
        return priority;
    }

    private int getInactivePriority() {
        float dst = getDistanceToTarget();
        if (dst < maxRange && isTargetVisible()) {
            return priority;
        }
        return -1;
    }

    private boolean isTargetVisible() {

        // If there is an obstacle in the path to the max range point, mobs visible.
        // TODO change layer to detect
        if (physics.raycast(towerPosition, maxRangePosition, PhysicsLayer.PLAYER, hit)) {
            debugRenderer.drawLine(towerPosition, hit.point);
            return true;
        }
        debugRenderer.drawLine(towerPosition, maxRangePosition);
        return false;
    }
}
