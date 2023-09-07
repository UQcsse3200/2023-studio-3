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

import java.util.ArrayList;

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
    private static final String FIRING = "firingStart";
    private static final String IDLE_LEFT = "idleLeft";
    private static final String IDLE_RIGHT = "idleRight";
    private static final String DYING = "deathStart";
    
    // The Engineer's attributes.
    private final float maxRange; // The maximum range of the Engineer's weapon.
    // weaponCapacity is the number of shots fired before the engineer has to reload
    private static final int weaponCapacity = 10;
    private int shotsFired = 0;  // Tracks the number of shots fired in the current cycle
    
    private Vector2 engineerPosition = new Vector2(10, 50); // Placeholder value for the Engineer's position.
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private long reloadTime;

    private ArrayList<RaycastHit> hits = new ArrayList<>();
    private final RaycastHit hit = new RaycastHit();
    private ArrayList<Vector2> targets = new ArrayList<>();
    
    /** The Engineer's states. */
    private enum STATE {
        IDLE_LEFT, IDLE_RIGHT, DEPLOY, FIRING, STOW
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
        this.engineerPosition = owner.getEntity().getCenterPosition();
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
        // configure tower state depending on target visibility
        switch (engineerState) {
            case IDLE_LEFT -> {
                // targets detected in idle mode - start deployment
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(FIRING);
                    engineerState = STATE.FIRING;
                } else {

                }
            }
            case IDLE_RIGHT -> {
                // targets detected in idle mode - start deployment
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(FIRING);
                    engineerState = STATE.FIRING;
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

                    owner.getEntity().getEvents().trigger(IDLE_RIGHT);
                    engineerState = STATE.IDLE_RIGHT;
                } else {
                    if (shotsFired <= 10) {
                        owner.getEntity().getEvents().trigger(FIRING);
                        // this might be changed to an event which gets triggered everytime the tower enters the firing state
                        Entity newProjectile = ProjectileFactory.createFireBall(owner.getEntity(),
                                new Vector2(100, owner.getEntity().getPosition().y),
//                                fetchTarget(),
                                new Vector2(4f, 4f));
                        newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.75), (float) (owner.getEntity().getPosition().y + 0.4));
                        ServiceLocator.getEntityService().register(newProjectile);
                        shotsFired += 1;
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
     * For stopping the running task
     */
    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        return isTargetVisible() ? 3 : 0;
    }

    /**
     * Uses a raycast to determine whether there are any targets in detection range
     * @return true if a target is visible, false otherwise
     */
    public boolean isTargetVisible() {
        // If there is an obstacle in the path to the max range point, mobs visible.
        Vector2 position = owner.getEntity().getCenterPosition();

        for (int i = 5; i > -5; i--) {
            if (physics.raycast(position, new Vector2(position.x + maxRange, position.y + i), TARGET, hit)) {
                hits.add(hit);
                targets.add(new Vector2(position.x + maxRange, position.y + i));
            }
        }
        return !hits.isEmpty();
    }

    public Vector2 fetchTarget() {
        int lowest = 10;
        Vector2 nearest = new Vector2(owner.getEntity().getCenterPosition().x,
                owner.getEntity().getCenterPosition().y);
        for (Vector2 tgt : targets){
            if (Math.abs(tgt.y - nearest.y) < lowest) {
                lowest = (int)Math.abs(tgt.y - nearest.y);
                nearest = tgt;
            }
        }
        return nearest;
    }
}