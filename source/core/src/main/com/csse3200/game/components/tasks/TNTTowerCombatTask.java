package com.csse3200.game.components.tasks;


import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * The TNTTowerCombatTask runs the AI for the TNTTower class. The tower will scan for targets in a straight line
 * from its center point until a point at (x + maxRange, y), where x,y are the coordinates of the tower's center
 * position. This component should be added to an AiTaskComponent attached to the tower instance.
 */
public class TNTTowerCombatTask extends DefaultTask implements PriorityTask {
    // Constants
    private static final int INTERVAL = 1;  // time interval to scan for enemies in seconds
    private static final short TARGET = PhysicsLayer.NPC;  // The type of targets that the tower will detect
    // the following four constants are the event names that will be triggered in the state machine
    public static final String DIG = "digStart";
    public static final String EXPLOSION = "explodeStart";
    public static final String DEFAULT = "defaultStart";
    public static final String DAMAGE = "TNTDamageStart";

    // class attributes
    private final int priority;  // The active priority this task will have
    private final float maxRange;
    private Vector2 towerPosition = new Vector2(10, 10); // initial placeholder value - will be overwritten
    private final Vector2 maxRangePosition = new Vector2();
    private final PhysicsEngine physics;
    private final GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();
    private boolean readToDelete = false;

    public enum STATE {
        IDLE, EXPLODE, REMOVE
    }
    private STATE towerState = STATE.IDLE;

    /**
     * @param priority Task priority when targets are detected (0 when nothing detected). Must be a positive integer.
     * @param maxRange Maximum effective range of the weapon tower. This determines the detection distance of targets
     */
    public TNTTowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * Starts the Task running, triggers the initial "defaultStart" event.
     */
    @Override
    public void start() {
        super.start();
        // Set the tower's coordinates
        this.towerPosition = owner.getEntity().getCenterPosition();
        this.maxRangePosition.set(towerPosition.x + maxRange, towerPosition.y);
        // Default mode
        owner.getEntity().getEvents().trigger(DEFAULT);

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
     * TNT tower state machine. Updates tower state by scanning for mobs, and
     * triggers the appropriate events corresponding to the STATE enum.
     */
    public void updateTowerState() {
        // configure tower state depending on target visibility
        switch (towerState) {
            case IDLE -> {
                // targets detected in idle mode - start deployment
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(DIG);
                    towerState = STATE.EXPLODE;
                } else {
                    owner.getEntity().getEvents().trigger(DEFAULT);
                }
            }
            case EXPLODE -> {
                owner.getEntity().getEvents().trigger(EXPLOSION);
                owner.getEntity().getEvents().trigger(DAMAGE);
                towerState = STATE.REMOVE;
            }
            default -> readToDelete = true;   // REMOVE
        }
    }

    /**
     * Returns the current priority of the task.
     * @return active priority value if targets detected, inactive priority otherwise
     */
    @Override
    public int getPriority() {
        if (isReadyToDelete()) {
            owner.getEntity().setFlagForDelete(true);
            return -1;
        } else {
            return priority;
        }
    }

    /**
     * Returns the current state of the tower.
     *
     * @return the current state of the tower.
     */
    public STATE getState() {
        return this.towerState;
    }

    /**
     * Uses a raycast to determine whether there are any targets in detection range
     * @return true if a target is visible, false otherwise
     */
    public boolean isTargetVisible() {
        // If there is an obstacle in the path to the max range point, mobs visible.
        boolean top = physics.raycast(towerPosition.add(0f,0.4f), maxRangePosition.add(0f,0.4f), TARGET, hit);
        boolean bottom = physics.raycast(towerPosition.sub(0f,0.4f), maxRangePosition.sub(0f,0.4f), TARGET, hit);
        return top || bottom;
    }

    public boolean isReadyToDelete() {
        return readToDelete;
    }
}