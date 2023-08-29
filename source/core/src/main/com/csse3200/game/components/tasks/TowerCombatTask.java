package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The TowerCombatTask runs the AI for the WeaponTower class. The tower will scan for targets in a straight line
 * from its center point until a point at (x + maxRange, y), where x,y are the cooridinates of the tower's center
 * position. This component should be added to an AiTaskComponent attached to the tower instance.
 */
public class TowerCombatTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(TowerCombatTask.class);
    private final int priority;  // The active priority this task will have
    private Vector2 towerPosition = new Vector2(10,10);
    private final Vector2 maxRangePosition = new Vector2();
    private final PhysicsEngine physics;
    private final int INTERVAL = 1;  // time interval to scan for enemies in seconds
    private final GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();

    private static final short TARGET = PhysicsLayer.NPC;  // The type of targets that the tower will detect

    private  enum STATE {
        IDLE, DEPLOY, FIRING, STOW
    }
    private STATE towerState = STATE.IDLE;

    /**
     * @param priority Task priority when targets are detected (0 when nothing detected). Must be a positive integer.
     * @param maxRange Maximum effective range of the weapon tower. This determines the detection distance of targets
     */
    public TowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRangePosition.set(towerPosition.x + maxRange, towerPosition.y);
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * Starts the Task running, triggers the initial "idleStart" event.
     */
    @Override
    public void start() {
        super.start();
        // Set the tower's coordinates
        this.towerPosition = owner.getEntity().getCenterPosition();

        // Default to idle mode
        owner.getEntity().getEvents().trigger("idleStart");

        endTime = timeSource.getTime() + (INTERVAL * 500);
    }

    /**
     * The update method is what is run every time the TaskRunner in the AiTaskComponent calls update().
     * triggers events depending on the presence or otherwise of targets in the detection range
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateTowerState();
            endTime = timeSource.getTime() + (INTERVAL * 1000);
        }
    }

    /**
     * Weapon tower state machine. Updates tower state by scanning for mobs, and
     * triggers the appropriate events corresponding to the STATE enum
     */
    public void updateTowerState() {
        // configure tower state depending on target visibility
        switch (towerState) {
            case IDLE -> {
                // targets detected in idle mode - start deployment
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger("deployStart");
                    towerState = STATE.DEPLOY;
                }
            }
            case DEPLOY -> {
                // currently deploying,
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger("firingStart");
                    towerState = STATE.FIRING;
                } else {
                    owner.getEntity().getEvents().trigger("stowStart");
                    towerState = STATE.STOW;
                }
            }
            case FIRING -> {
                // targets gone - stop firing
                if (!isTargetVisible()) {
                    owner.getEntity().getEvents().trigger("stowStart");
                    towerState = STATE.STOW;
                } else {
                    owner.getEntity().getEvents().trigger("firingStart");
                }
            }
            case STOW -> {
                // currently stowing
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger("deployStart");
                    towerState = STATE.DEPLOY;
                } else {
                    owner.getEntity().getEvents().trigger("idleStart");
                    towerState = STATE.IDLE;
                }
            }
        }
    }
    /**
     * For stopping the running task
     */
    @Override
    public void stop() {
        super.stop();
        owner.getEntity().getEvents().trigger("stowStart");
    }

    /**
     * Returns the current priority of the task.
     * @return active priority value if targets detected, inactive priority otherwise
     */
    @Override
    public int getPriority() {
        if (isTargetVisible()) {
            return getActivePriority();
        }
        return getInactivePriority();
    }

    /**
     * Finds the distance to the nearest target, if any in range.
     * @return (float) distance to nearest target, otherwise 0 if nothing in range.
     */
    private float getDistanceToTarget() {
        if (physics.raycast(towerPosition, maxRangePosition, TARGET, hit)) {
            return towerPosition.dst(hit.point.x, hit.point.y);
        };
        return 0;
    }

    /**
     * Fetches the active priority of the Task if a target is visible.
     * @return (int) active priority if a target is visible, -1 otherwise
     */
    private int getActivePriority() {
        if (!isTargetVisible()) {
            return 0; // Too far, stop firing
        }
        return priority;
    }

    /**
     * Fetches the inactive priority of the Task if a target is not visible.
     * @return (int) -1 if a target is not visible, active priority otherwise
     */
    private int getInactivePriority() {
        if (isTargetVisible()) {
            return priority;
        }
        return 0;
    }

    /**
     * Uses a raycast to determine whether there are any targets in detection range
     * @return true if a target is visible, false otherwise
     */
    private boolean isTargetVisible() {

        // If there is an obstacle in the path to the max range point, mobs visible.
        if (physics.raycast(towerPosition, maxRangePosition, TARGET, hit)) {
            return true;
        }
        return false;
    }
}