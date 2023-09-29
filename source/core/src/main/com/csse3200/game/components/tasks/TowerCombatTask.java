package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
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

    // class attributes
    private final int priority;  // The active priority this task will have
    private float fireRateInterval; // time interval to fire projectiles at enemies in seconds
    private final float maxRange;
    private Vector2 towerPosition = new Vector2(10, 10); // initial placeholder value - will be overwritten
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();
  
    private enum STATE {
        IDLE, DEPLOY, FIRING, STOW
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
        // Set up listener to change firerate
        owner.getEntity().getEvents().addListener("addFireRate",this::changeFireRateInterval);

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
        switch (towerState) {
            case IDLE -> {
                // targets detected in idle mode - start deployment
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(DEPLOY);
                    towerState = STATE.DEPLOY;
                }
            }
            case DEPLOY -> {
                // currently deploying,
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(FIRING);
                    towerState = STATE.FIRING;
                } else {
                    owner.getEntity().getEvents().trigger(STOW);
                    towerState = STATE.STOW;
                }
            }
            case FIRING -> {
                // targets gone - stop firing
                if (!isTargetVisible()) {

                    owner.getEntity().getEvents().trigger(STOW);
                    towerState = STATE.STOW;
                } else {
                    owner.getEntity().getEvents().trigger(FIRING);
                    // this might be changed to an event which gets triggered everytime the tower enters the firing state

                    Entity newProjectile = ProjectileFactory.createFireBall(PhysicsLayer.NPC, new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f,2f));
                    newProjectile.setScale(1.1f, 0.8f);
                    newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.5), (float) (owner.getEntity().getPosition().y + 0.5));
                    ServiceLocator.getEntityService().register(newProjectile);
                    
                    // * TEMPRORARYYYYYYYY PLS DON'T DELETE THIS
                    // PIERCE FIREBALL
                    // Entity pierceFireball = ProjectileFactory.createPierceFireBall(PhysicsLayer.NPC, new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f,2f));
                    // pierceFireball.setPosition((float) (owner.getEntity().getPosition().x + 0), (float) (owner.getEntity().getPosition().y + 0.4));
                    // ServiceLocator.getEntityService().register(pierceFireball);

                    // RICOCHET FIREBALL
                    // Entity ricochetProjectile = ProjectileFactory.createRicochetFireball(PhysicsLayer.NPC, new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f,2f), 0);

                    // ricochetProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0), (float) (owner.getEntity().getPosition().y + 0.4));
                    // ServiceLocator.getEntityService().register(ricochetProjectile);

                    // SPLIT FIREWORKS FIREBALLL
                    // Entity splitFireWorksProjectile = ProjectileFactory.createSplitFireWorksFireball(PhysicsLayer.NPC, new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f,2f), 16);

                    // splitFireWorksProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.75), (float) (owner.getEntity().getPosition().y + 0.4));
                    // ServiceLocator.getEntityService().register(splitFireWorksProjectile);
                }
            }
            case STOW -> {
                // currently stowing
                if (isTargetVisible()) {

                    owner.getEntity().getEvents().trigger(DEPLOY);
                    towerState = STATE.DEPLOY;
                } else {
                    owner.getEntity().getEvents().trigger(IDLE);
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
        return physics.raycast(towerPosition, maxRangePosition, TARGET, hit);
    }

    /**
     * Increases the fireRateInterval, changing how frequently the turret fires. Will decrease if the argument is negative.
     *
     * @param perMinute The number of times per minute the turret's fire rate should increase
     */
    private void changeFireRateInterval(int perMinute) {
        float oldFireSpeed = 1/fireRateInterval;
        float newFireSpeed = oldFireSpeed + perMinute/60f;
        if (newFireSpeed == 0) {
            return;
        } else {
            fireRateInterval = 1 / newFireSpeed;
        }
    }

    /**
     * Function for getting the turret's fire rate.
     *
     * @return The fireRateInterval variable
     */
    public float getFireRateInterval() {
        return fireRateInterval;
    }
}
