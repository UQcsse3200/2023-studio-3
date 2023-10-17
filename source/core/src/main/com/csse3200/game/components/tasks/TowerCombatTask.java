package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.round;

/**
 * The TowerCombatTask runs the AI for the WeaponTower class. The tower will scan for targets in a straight line
 * from its center point until a point at (x + maxRange, y), where x,y are the cooridinates of the tower's center
 * position. This component should be added to an AiTaskComponent attached to the tower instance.
 */
public class TowerCombatTask extends DefaultTask implements PriorityTask {
    // Constants
    private static final int INTERVAL = 1;  // time interval to scan for enemies in seconds
    private static final short TARGET = PhysicsLayer.NPC;  // The type of targets that the tower will detect
    // the following four constants are the event names that will be triggered in the state machine
    private static final String STOW = "stowStart";
    private static final String DEPLOY = "deployStart";
    private static final String FIRING = "firingStart";
    private static final String IDLE = "idleStart";
    private static final String DEATH = "deathStart";

    // class attributes
    private final int priority;  // The active priority this task will have
    private float fireRateInterval; // time interval to fire projectiles at enemies in seconds
    private final float maxRange;
    private Vector2 towerPosition = new Vector2(10, 10); // initial placeholder value - will be overwritten
    private final Vector2 maxRangePosition = new Vector2();
    private final PhysicsEngine physics;
    private final GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();
    private static final Logger logger = LoggerFactory.getLogger(TowerCombatTask.class);
    private boolean shoot = true;
  
    private enum STATE {
        IDLE, DEPLOY, FIRING, STOW, DEATH
    }
    private STATE towerState = STATE.IDLE;

    /**
     * @param priority Task priority when targets are detected (0 when nothing detected). Must be a positive integer.
     * @param maxRange Maximum effective range of the weapon tower. This determines the detection distance of targets
     */
    public TowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        this.fireRateInterval = 1;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * THIS IS REDUNDANT AND NOT USED
     * @param priority Task priority when targets are detected (0 when nothing detected). Must be a positive integer.
     * @param maxRange Maximum effective range of the weapon tower. This determines the detection distance of targets
     * @param fireRate The number of times per second this tower should fire its weapon
     */
    public TowerCombatTask(int priority, float maxRange, float fireRate) {
        this.priority = priority;
        this.maxRange = maxRange;
        this.fireRateInterval = 1/fireRate;
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
        owner.getEntity().getEvents().trigger(IDLE);
        // Set up listener to change fire rate
        owner.getEntity().getEvents().addListener("addFireRate",this::changeFireRateInterval);
        logger.info("TowerCombatTask started");
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
            if (towerState == STATE.FIRING) {
                endTime = timeSource.getTime() + round(fireRateInterval * 1000);
            } else {
                endTime = timeSource.getTime() + (INTERVAL * 1000);
            }
        }
    }

    /**
     * Weapon tower state machine. Updates tower state by scanning for mobs, and
     * triggers the appropriate events corresponding to the STATE enum.
     */
    public void updateTowerState() {
        // configure tower state depending on target visibility
        if (owner.getEntity().getComponent(CombatStatsComponent.class).getHealth() <= 0 && towerState != STATE.DEATH) {
            owner.getEntity().getEvents().trigger(DEATH);
            towerState = STATE.DEATH;
            return;
        }

        switch (towerState) {
            case IDLE -> handleIdleState();
            case DEPLOY -> handleDeployState();
            case FIRING -> handleFiringState();
            case STOW -> handleStowState();
            default -> handleDeathState();      // DEATH
        }
    }
  
    /**
     * For stopping the running task
     */
    @Override
    public void stop() {
        super.stop();
        owner.getEntity().getEvents().trigger(STOW);
    }

    /**
     * Returns the current priority of the task.
     * @return active priority value if targets detected, inactive priority otherwise
     */
    @Override
    public int getPriority() {
        return isTargetVisible() ? getActivePriority() : getInactivePriority();
    }

    /**
     * Fetches the active priority of the Task if a target is visible.
     * @return (int) active priority if a target is visible, -1 otherwise
     */
    private int getActivePriority() {
        return !isTargetVisible() ? 0 : priority;
    }

    /**
     * Fetches the inactive priority of the Task if a target is not visible.
     * @return (int) -1 if a target is not visible, active priority otherwise
     */
    private int getInactivePriority() {
        return isTargetVisible() ? priority : 0;
    }

    /**
     * Uses a raycast to determine whether there are any targets in detection range
     * @return true if a target is visible, false otherwise
     */
    private boolean isTargetVisible() {
        // If there is an obstacle in the path to the max range point, mobs visible.
        boolean top = physics.raycast(towerPosition.add(0f,0.4f), maxRangePosition.add(0f,0.4f), TARGET, hit);
        boolean bottom = physics.raycast(towerPosition.sub(0f,0.4f), maxRangePosition.sub(0f,0.4f), TARGET, hit);
        return top || bottom;
    }

    /**
     * Changes the tower's fire rate.
     *
     * @param newInterval The rate at which the tower should fire projectiles in shots per second.
     */
    private void changeFireRateInterval(int newInterval) {
        logger.info(String.format("Changing fire rate to: %d", newInterval));
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
     * Function triggers actions at IDLE state, then switch to DEPLOY
     */
    private void handleIdleState() {
        // targets detected in idle mode - start deployment
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(DEPLOY);
            towerState = STATE.DEPLOY;
        }
    }

    /**
     * Function triggers actions at DEPLOY state, then switch to FIRING or STOW
     */
    private void handleDeployState() {
        // currently deploying,
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(FIRING);
            towerState = STATE.FIRING;
        } else {
            owner.getEntity().getEvents().trigger(STOW);
            towerState = STATE.STOW;
        }
    }

    /**
     * Function triggers actions at FIRING state
     */
    private void handleFiringState() {
        if (shoot) {
            // targets gone - stop firing
            if (!isTargetVisible()) {
                owner.getEntity().getEvents().trigger(STOW);
                towerState = STATE.STOW;
            } else {
                owner.getEntity().getEvents().trigger(FIRING);
                // this might be changed to an event which gets triggered everytime the tower enters the firing state

                Entity newProjectile = ProjectileFactory.createFireBall(PhysicsLayer.NPC, new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f, 2f));
                newProjectile.setScale(1.1f, 0.8f);
                newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.5), (owner.getEntity().getPosition().y));
                ServiceLocator.getEntityService().register(newProjectile);
            }
        }

        shoot = !shoot;
    }

    /**
     * Function triggers actions at STOW state, then switch to DEPLOY or IDLE
     */
    private void handleStowState() {
        // currently stowing
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(DEPLOY);
            towerState = STATE.DEPLOY;
        } else {
            owner.getEntity().getEvents().trigger(IDLE);
            towerState = STATE.IDLE;
        }
    }

    /**
     * Function handle DEATH state
     */
    private void handleDeathState() {
        if (owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
            owner.getEntity().setFlagForDelete(true);
        }
    }
}