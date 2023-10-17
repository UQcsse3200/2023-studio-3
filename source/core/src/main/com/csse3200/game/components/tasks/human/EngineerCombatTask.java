package com.csse3200.game.components.tasks.human;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * The AI Task for the Engineer entity. The Engineer will scan for targets within its detection range
 * and trigger events to change its state accordingly. This task must be called once the Engineer has
 * appropiately moved into position.
 */
public class EngineerCombatTask extends DefaultTask implements PriorityTask {
    
    private static final int INTERVAL = 1; // The time interval for each target scan from the Engineer.
    private static final int PRIORITY = 3; // Default priority of the combat task when mobs are in range.
    private static final short TARGET = PhysicsLayer.NPC; // The type of targets that the Engineer will detect.
    
    // Animation event names for the Engineer's state machine.
    private static final String FIRING = "firingSingleStart";
    private static final String IDLE_RIGHT = "idleRight";
    private static final String ENGINEER_PROJECTILE_FIRED = "engineerProjectileFired";
    
    // The Engineer's attributes.
    private final float maxRange; // The maximum range of the Engineer's weapon.
    // weaponCapacity is the number of shots fired before the engineer has to reload
    private static final int WEAPON_CAPACITY = 10;
    private int shotsFired = 0;  // Tracks the number of shots fired in the current cycle

    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private long reloadTime;

    private static final Logger logger = LoggerFactory.getLogger(EngineerCombatTask.class);

    private ArrayList<RaycastHit> hits = new ArrayList<>();
    private final RaycastHit hit = new RaycastHit();
    private ArrayList<Vector2> targets = new ArrayList<>();
    
    /** The Engineer's states. */
    private enum STATE {
        IDLE_RIGHT, FIRING
    }
    private STATE engineerState = STATE.IDLE_RIGHT;
    
    /**
     * @param maxRange The maximum range of the Engineer's weapon.
     */
    public EngineerCombatTask(float maxRange) {
        this.maxRange = maxRange;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }
    
    /**
     * Runs the task and triggers Engineer's idle animation.
     */
    @Override
    public void start() {
        super.start();
        // Set the tower's coordinates
        // Placeholder value for the Engineer's position.
        Vector2 engineerPosition = owner.getEntity().getCenterPosition();
        this.maxRangePosition.set(engineerPosition.x + maxRange, engineerPosition.y);
        // Default to idle mode
        owner.getEntity().getEvents().trigger(IDLE_RIGHT);
        
        endTime = timeSource.getTime() + (INTERVAL * 500);
    }
    
    /**
     * The update method is what is run every time the TaskRunner in the AiTaskComponent calls update().
     * triggers events depending on the presence or otherwise of targets in the detection range
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateEngineerState();
            endTime = timeSource.getTime() + (INTERVAL * 1000);
        }
    }
    
    /**
     * Engineer state machine
     */
    public void updateEngineerState() {
        // configure engineer state depending on target visibility
        switch (engineerState) {
            case IDLE_RIGHT -> {
                // targets detected in idle mode - start deployment
                if (isTargetVisible()) {
                    combatState();
                }
            }
            case FIRING -> {
                // targets gone - stop firing
                if (!isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(IDLE_RIGHT);
                    engineerState = STATE.IDLE_RIGHT;
                } else {
                    if (shotsFired <= WEAPON_CAPACITY) {
                        owner.getEntity().getEvents().trigger(FIRING);
                        owner.getEntity().getEvents().trigger(ENGINEER_PROJECTILE_FIRED);
                        // this might be changed to an event which gets triggered everytime the tower enters the firing state
                        Entity newProjectile = ProjectileFactory.createEngineerBullet(PhysicsLayer.NPC,
                                new Vector2(100, owner.getEntity().getPosition().y),
                                new Vector2(4f, 4f));
                        newProjectile.setScale(0.8f, 0.8f);
                        newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.3),
                                (float) (owner.getEntity().getPosition().y + 0.15));
                        ServiceLocator.getEntityService().register(newProjectile);
                        shotsFired ++;
                        reloadTime = timeSource.getTime();
                    } else {
                        // engineer needs to reload
                        if (reloadTime < timeSource.getTime()) {
                            // engineer has reloaded
                            shotsFired = 0;
                            reloadTime = timeSource.getTime();
                        }
                    }
                }
            }
        }
    }

    /**
     * Puts the engineerCombatTask state into combat mode
     */
    private void combatState() {
        owner.getEntity().getEvents().trigger(FIRING);
        engineerState = STATE.FIRING;
    }
    /**
     * For stopping the running task
     */
    @Override
    public void stop() {
        super.stop();
    }

    /**
     * Simplified getPriority function, returns the priority of the task
     * @return priority as an integer value. If mobs are visible, return the current priority, otherwise return 0.
     */
    @Override
    public int getPriority() {
        return isTargetVisible() ? PRIORITY : 0;
    }

    /**
     * Uses a raycast to determine whether there are any targets in detection range. Performs multiple raycasts
     * to a range of points at x = engineer.x + maxRange, and a range of y values above and below current y position.
     * Allows the engineer entity to detect mobs in adjacent lanes.
     * @return true if a target is detected, false otherwise
     */
    public boolean isTargetVisible() {
        // If there is an obstacle in the path to the max range point, mobs visible.
        Vector2 position = owner.getEntity().getCenterPosition();
        hits.clear();
        targets.clear();
        for (int i = 5; i > -5; i--) {
            if (physics.raycast(position, new Vector2(position.x + maxRange, position.y + i), TARGET, hit)) {
                hits.add(hit);
                targets.add(new Vector2(position.x + maxRange, position.y + i));
            }
        }
        return !hits.isEmpty();
    }

    /**
     * Fetches the nearest target from the array of detected target positions created during the last call of
     * isTargetVisible
     * @return a Vector2 position of the nearest mob detected.
     */
    public Vector2 fetchTarget() {
        // Initial nearest position for comparison
        float currentClosest = Float.MAX_VALUE;

        Vector2 nearest = new Vector2(owner.getEntity().getCenterPosition().x,
                owner.getEntity().getCenterPosition().y);
        Vector2 enggPosition = owner.getEntity().getCenterPosition();

        for (Vector2 target : targets) {
            float distance = enggPosition.dst(target); // euclidean distance
            if (distance < currentClosest) {
                currentClosest = distance;
                nearest = target;
            }
        }
        return nearest;
    }
}
