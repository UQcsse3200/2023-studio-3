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

public class TowerCombatTask extends DefaultTask implements PriorityTask {
    private final int priority;
    private final float maxRange;
    private final Vector2 towerPosition = owner.getEntity().getCenterPosition();
    private final Vector2 maxRangePosition = new Vector2();
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();
//    private DeployTask deployTask;

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
    }

    @Override
    public void start() {
        super.start();

//        deployTask.create(owner);
//        deployTask.start();

        this.owner.getEntity().getEvents().trigger("towerDeployStart");
    }

    @Override
    public void update() {
//        deployTask.update();
//        if (deployTaskTask.getStatus() != Status.ACTIVE) {
//            deployTask.start();
//        }
    }

    @Override
    public void stop() {
        super.stop();
//        deployTask.stop();
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    private float getDistanceToTarget() {
        if (physics.raycast(towerPosition, maxRangePosition, PhysicsLayer.OBSTACLE, hit)) {
            return towerPosition.dst(hit.point.x, hit.point.y);
        };
        return -1;
    }

    private int getActivePriority() {
        float dst = getDistanceToTarget();
        if (dst > maxRange || !isTargetVisible()) {
            return -1; // Too far, stop chasing
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

        // If there is an obstacle in the path to the player, not visible.
        if (physics.raycast(towerPosition, maxRangePosition, PhysicsLayer.OBSTACLE, hit)) {
            debugRenderer.drawLine(towerPosition, hit.point);
            return true;
        }
        debugRenderer.drawLine(towerPosition, maxRangePosition);
        return false;
    }
}
