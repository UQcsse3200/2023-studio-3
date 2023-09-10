package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

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
    private Vector2 towerPosition = new Vector2(10, 10); // initial placeholder value - will be overwritten
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();

    public enum STATE {
        IDLE, UP, DOWN, SHOOT_UP, SHOOT_DOWN, WALK, DIE
    }
    public STATE towerState = STATE.WALK;

    /**
     * @param priority Task priority when targets are detected (0 when nothing detected). Must be a positive integer.
     * @param maxRange Maximum effective range of the weapon tower. This determines the detection distance of targets
     */
    public DroidCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
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
            case WALK -> {
                owner.getEntity().getEvents().trigger(WALK);
                towerState = STATE.IDLE;
            }
            case IDLE -> {
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(ATTACK_UP);
                    owner.getEntity().getEvents().trigger(SHOOT_UP);
                    towerState = STATE.DOWN;
                } else {
                    owner.getEntity().getEvents().trigger(IDLE);
                }
            }
            case SHOOT_DOWN -> {
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(ATTACK_DOWN);
                    owner.getEntity().getEvents().trigger(SHOOT_DOWN);
                    towerState = STATE.UP;
                } else {
                    owner.getEntity().getEvents().trigger(GO_UP);
                    towerState = STATE.UP;
                }
            }
            case SHOOT_UP -> {
                if (isTargetVisible()) {

                    owner.getEntity().getEvents().trigger(ATTACK_UP);
                    owner.getEntity().getEvents().trigger(SHOOT_UP);
                    towerState = STATE.DOWN;
                } else {
                    owner.getEntity().getEvents().trigger(IDLE);
                    towerState = STATE.IDLE;
                }
            }
            case DOWN -> {
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(GO_DOWN);
                    towerState = STATE.SHOOT_DOWN;
                } else {
                    owner.getEntity().getEvents().trigger(IDLE);
                    towerState = STATE.IDLE;
                }
            }
            case UP -> {
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(GO_UP);
                    towerState = STATE.SHOOT_UP;
                } else {
                    owner.getEntity().getEvents().trigger(GO_UP);
                    towerState = STATE.IDLE;


                }
            }
            case DIE -> {
                if (owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished())
                owner.getEntity().setFlagForDelete(true);
            }
        }
    }
    /**
     * For stopping the running task
     */
    @Override
    public void stop() {
        super.stop();
//        owner.getEntity().getEvents().trigger(STOW);
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
        return physics.raycast(towerPosition, maxRangePosition, TARGET, hit);
    }


}
