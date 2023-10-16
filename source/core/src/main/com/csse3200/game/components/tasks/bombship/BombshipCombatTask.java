package com.csse3200.game.components.tasks.bombship;


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
public class BombshipCombatTask extends DefaultTask implements PriorityTask {

    private static final int INTERVAL = 1; // The time interval for each target scan from the Engineer.
    private static final int PRIORITY = 3; // Default priority of the combat task when mobs are in range.
    private static final short TARGET1 = PhysicsLayer.BOSS; // The type of targets that the Engineer will detect.
    private static final short TARGET2 = PhysicsLayer.XENO;

    // Animation event names for the Engineer's state machine.
    private static final String START = "start";
    private static final String IDLE = "idle";
    private static final String DESTROY = "destroy";

    // The Engineer's attributes.
    private final float maxRange; // The maximum range of the bombship.

    private Vector2 bombShipPosition = new Vector2(0, 0); // Placeholder value for the Bombship's position.
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private long reloadTime;
/**
    private ArrayList<RaycastHit> hits = new ArrayList<>();
    private final RaycastHit hit = new RaycastHit();
    private ArrayList<Vector2> targets = new ArrayList<>();
*/
    /** The Engineer's states. */
    private enum STATE {
        IDLE, START , DESTROY
    }
    private STATE bombshipState = STATE.IDLE;

    public BombshipCombatTask(float maxRange) {
        this.maxRange = maxRange;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * Runs the task and triggers Bombship's idle animation.
     */
    @Override
    public void start() {
        super.start();
        this.bombShipPosition = owner.getEntity().getCenterPosition();
        this.maxRangePosition.set(bombShipPosition.x + maxRange, bombShipPosition.y);
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
            updateBombshipState();
            endTime = timeSource.getTime() + (INTERVAL * 1000);
        }
    }

    /**
     * Bombship state machine
     */
    public void updateBombshipState() {
        // configure engineer state depending on target visibility
        switch (bombshipState) {
            case IDLE -> {
                // targets detected in idle mode - start deployment
                if (isEngineerDied()) {
                    combatState();
                }
            }
            case START -> {
                // targets gone - stop firing
                if (!isEngineerDied()) {
                    owner.getEntity().getEvents().trigger(IDLE);
                    bombshipState = STATE.IDLE;
                } else {
                    owner.getEntity().getEvents().trigger(START);
                }
            }
            case DESTROY -> {
                owner.getEntity().getEvents().trigger(DESTROY);
            }
        }
    }

    /**
     * Puts the BombshipCombatTask state into combat mode
     */
    private void combatState() {
        owner.getEntity().getEvents().trigger(START);
        bombshipState = STATE.START;
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
        return isEngineerDied() ? PRIORITY : 0;
    }

    /**
     * Uses a raycast to determine whether there are any targets in detection range. Performs multiple raycasts
     * to a range of points at x = engineer.x + maxRange, and a range of y values above and below current y position.
     * Allows the bombship entity to detect mobs in adjacent lanes.
     * @return true if a target is detected, false otherwise
     */
    public boolean isEngineerDied() {
        //if (engineerCount < maxEngineers) {
            return true;
        //}
    }

    /**
     * Fetches the nearest target from the array of detected target positions created during the last call of
     * this could be done in the next sprint , the scan doesnt work as of now !
     * @return a Vector2 position of the nearest mob detected.
     */
   /** public Vector2 fetchTarget() {
        // Initial nearest position for comparison
        int lowest = 10;

        Vector2 nearest = new Vector2(owner.getEntity().getCenterPosition().x,
                owner.getEntity().getCenterPosition().y);

        // Find the nearest target from the array of targets
        for (Vector2 tgt : targets){
            if (Math.abs(tgt.y - nearest.y) < lowest) {
                lowest = (int)Math.abs(tgt.y - nearest.y);
                nearest = tgt;
            }
        }
        return nearest;
    }
    */
}

