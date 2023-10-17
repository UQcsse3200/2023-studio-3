package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import static java.lang.Math.round;

/**
 * The DroidCombatTask runs the AI for the DroidTower class. The tower will scan for targets in a straight line
 * from its center point until a point at (x + maxRange, y), where x,y are the cooridinates of the tower's center
 * position. This component should be added to an AiTaskComponent attached to the tower instance.
 */
public class DroidCombatTask extends DefaultTask implements PriorityTask {
    // Constants
    private static final int INTERVAL = 1;  // time interval to scan for enemies in seconds
    private static final short TARGET = PhysicsLayer.NPC;  // The type of targets that the tower will detect
    // the following four constants are the event names that will be triggered in the state machine
    public static final String GO_UP = "goUpStart";
    public static final String GO_DOWN = "goDownStart";
    public static final String ATTACK_UP = "attackUpStart";
    public static final String ATTACK_DOWN = "attackDownStart";
    public static final String WALK = "walkStart";
    public static final String DEATH = "deathStart";
    public static final String IDLE = "idleStart";
    public static final String SHOOT_UP = "ShootUp";
    public static final String SHOOT_DOWN = "ShootDown";

    // class attributes
    private final int priority;  // The active priority this task will have
    private final float maxRange;
    private float fireRateInterval;
    private Vector2 towerPosition = new Vector2(10, 10); // initial placeholder value - will be overwritten
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();

    public enum STATE {
        IDLE, UP, DOWN, SHOOT_UP, SHOOT_DOWN, WALK, DIE
    }
    private STATE towerState = STATE.WALK;

    /**
     * @param priority Task priority when targets are detected (0 when nothing detected). Must be a positive integer.
     * @param maxRange Maximum effective range of the weapon tower. This determines the detection distance of targets
     */
    public DroidCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        this.fireRateInterval = 1;
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
        this.maxRangePosition.set(towerPosition.x + maxRange, towerPosition.y);
        // Default to idle mode
        owner.getEntity().getEvents().trigger(WALK);
        owner.getEntity().getEvents().addListener("addFireRate",this::changeFireRateInterval);
        endTime = timeSource.getTime() + (INTERVAL * 1000);
    }

    /**
     * The update method is what is run every time the TaskRunner in the AiTaskComponent calls update().
     * triggers events depending on the presence or otherwise of targets in the detection range
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateTowerState();
            if (towerState == STATE.SHOOT_UP || towerState == STATE.SHOOT_DOWN) {
                endTime = timeSource.getTime() + round(fireRateInterval * 1000);
            } else {
                endTime = timeSource.getTime() + (INTERVAL * 1000);
            }
        }
    }

    /**
     * Droid tower state machine. Updates tower state by scanning for mobs, and
     * triggers the appropriate events corresponding to the STATE enum.
     */
    public void updateTowerState() {
        // configure tower state depending on target visibility
        if (owner.getEntity().getComponent(CombatStatsComponent.class).getHealth() <= 0 && towerState != STATE.DIE) {
            owner.getEntity().getEvents().trigger(DEATH);
            towerState = STATE.DIE;
            return;
        }

        switch (towerState) {
            case WALK -> handleWalkState();
            case IDLE -> handleIdleState();
            case SHOOT_DOWN -> handleShootDownState();
            case SHOOT_UP -> handleShootUpState();
            case DOWN -> handleDownState();
            case UP -> handleUpState();
            default -> handleDieState();       // DIE
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
     * Function for setting the tower's state.
     * @param state The new state of this tower.
     */
    public void setState(STATE state) {
        this.towerState = state;
    }

    /**
     * Returns the current priority of the task.
     * @return active priority value if targets detected, inactive priority otherwise
     */
    @Override
    public int getPriority() {
        return isTargetVisible() ? priority : 0;
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

    private void changeFireRateInterval(int newInterval) {
        fireRateInterval = 1 / ((float) newInterval / 5);
    }

    /**
     * Function for getting the turret's fire rate.
     *
     * @return The fireRateInterval variable
     */
    public float getFireRateInterval() {
        return fireRateInterval;
    }

    /**
     * Function triggers walk and changes state from WALK to IDLE
     */
    private void handleWalkState() {
        owner.getEntity().getEvents().trigger(WALK);
        towerState = STATE.IDLE;
    }

    /**
     * Function triggers actions at IDLE state
     */
    private void handleIdleState() {
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(ATTACK_UP);
            owner.getEntity().getEvents().trigger(SHOOT_UP);
            towerState = STATE.DOWN;
        } else {
            owner.getEntity().getEvents().trigger(IDLE);
        }
    }

    /**
     * Function triggers actions at SHOOT_DOWN state, then switch to UP
     */
    private void handleShootDownState() {
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(ATTACK_DOWN);
            owner.getEntity().getEvents().trigger(SHOOT_DOWN);
        } else {
            owner.getEntity().getEvents().trigger(GO_UP);
        }

        towerState = STATE.UP;
    }

    /**
     * Function triggers actions at SHOOT_UP state, then switch to DOWN or IDLE
     */
    private void handleShootUpState() {
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(ATTACK_UP);
            owner.getEntity().getEvents().trigger(SHOOT_UP);
            towerState = STATE.DOWN;
        } else {
            owner.getEntity().getEvents().trigger(IDLE);
            towerState = STATE.IDLE;
        }
    }

    /**
     * Function triggers actions at DOWN state, then switch to SHOOT_DOWN or IDLE
     */
    private void handleDownState() {
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(GO_DOWN);
            towerState = STATE.SHOOT_DOWN;
        } else {
            owner.getEntity().getEvents().trigger(IDLE);
            towerState = STATE.IDLE;
        }
    }

    /**
     * Function triggers actions at UP state, then switch to SHOOT_UP or IDLE
     */
    private void handleUpState() {
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(GO_UP);
            towerState = STATE.SHOOT_UP;
        } else {
            owner.getEntity().getEvents().trigger(GO_UP);
            towerState = STATE.IDLE;
        }
    }

    /**
     * Function handles DIE state
     */
    private void handleDieState() {
        if (owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
            owner.getEntity().setFlagForDelete(true);
        }
    }
}