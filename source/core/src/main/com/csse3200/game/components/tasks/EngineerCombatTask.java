package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;git pu
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * The AI Task for the Engineer entity. The Engineer will scan for targets within its detection range
 * and trigger events to change its state accordingly. This task must be called once the Engineer has
 * appropiately moved into position.
 */
public class EngineerCombatTask extends DefaultTask implements PriorityTask {
    
    private static final int INTERVAL = 1; // The time interval for each target scan from the Engineer.
    private static final short TARGET = PhysicsLayer.NPC; // The type of targets that the Engineer will detect.
    
    // Animation event names for the Engineer's state machine.
    private static final String STOW = "";
    private static final String DEPLOY = "";
    private static final String FIRING = "";
    private static final String IDLE = "";
    private static final String DYING = "";
    
    // The Engineer's attributes.
    private final int priority;  // The priority of this task within the task list.
    private final float maxRange; // The maximum range of the Engineer's weapon.
    
    private Vector2 engineerPosition = new Vector2(10, 50); // Placeholder value for the Engineer's position.
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();
    
    /** The Engineer's states. */
    private enum STATE {
        IDLE, DEPLOY, FIRING, STOW
    }
    private STATE engineerState = STATE.IDLE;
    
    /**
     * @param priority The Engineer's combat task priority in the list of tasks.
     * @param maxRange The maximum range of the Engineer's weapon.
     */
    public EngineerCombatTask(int priority, float maxRange) {
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
        this.engineerPosition = owner.getEntity().getCenterPosition();
        this.maxRangePosition.set(engineerPosition.x + maxRange, engineerPosition.y);
        // Default to idle mode
        owner.getEntity().getEvents().trigger(IDLE);
        
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
        // configure tower state depending on target visibility
        switch (engineerState) {
            case IDLE -> {
                // targets detected in idle mode - start deployment
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(DEPLOY);
                    engineerState = STATE.DEPLOY;
                }
            }
            case DEPLOY -> {
                // currently deploying,
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(FIRING);
                    engineerState = STATE.FIRING;
                } else {
                    owner.getEntity().getEvents().trigger(STOW);
                    engineerState = STATE.STOW;
                }
            }
            case FIRING -> {
                // targets gone - stop firing
                if (!isTargetVisible()) {
                    
                    owner.getEntity().getEvents().trigger(STOW);
                    engineerState = STATE.STOW;
                } else {
                    owner.getEntity().getEvents().trigger(FIRING);
                    // this might be changed to an event which gets triggered everytime the tower enters the firing state
                    Entity newProjectile = ProjectileFactory.createFireBall(owner.getEntity(), new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f,2f));
                    newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.75), (float) (owner.getEntity().getPosition().y + 0.75));
                    ServiceLocator.getEntityService().register(newProjectile);
                }
            }
            case STOW -> {
                // currently stowing
                if (isTargetVisible()) {
                    
                    owner.getEntity().getEvents().trigger(DEPLOY);
                    engineerState = STATE.DEPLOY;
                } else {
                    owner.getEntity().getEvents().trigger(IDLE);
                    engineerState = STATE.IDLE;
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
        return physics.raycast(engineerPosition, maxRangePosition, TARGET, hit);
    }
}
