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

/**
 * The FireTowerCombatTask runs the AI for the FireTower class. The tower implementing this task will scan for enemies
 * in a straight line from the current position to a maxRange, and change the state of the tower.
 */
public class FireTowerCombatTask extends DefaultTask  implements PriorityTask {
    //constants
    private static final int INTERVAL = 1; //time interval to scan for enemies in seconds
    private static final short TARGET = PhysicsLayer.NPC; //the type of targets this tower will detect
    //The constants are names of events that will be triggered in the state machine
    private static final String IDLE = "startIdle";
    private static final String PREP_ATTACK = "startAttackPrep";
    private static final String ATTACK = "startAttack";

    //Class attributes
    private final int priority;
    private final float maxRange;

    private Vector2 towerPosition = new Vector2(10, 10);
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();

    private enum STATE {
        IDLE, PREP_ATTACK, ATTACK
    }
    private STATE towerState = STATE.IDLE;

    /**
     * Starts the task running, triggers the initial 'IDLE' event
     */
    public FireTowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    @Override
    public void start()  {
        super.start();
        // get the tower coordinates
        this.towerPosition = owner.getEntity().getCenterPosition();
        this.maxRangePosition.set(towerPosition.x  + maxRange, towerPosition.y);
        //default to idle state
        owner.getEntity().getEvents().trigger(IDLE);

        endTime = timeSource.getTime() + (INTERVAL *  500);
    }

    @Override
    public void update()  {
        if  (timeSource.getTime() >= endTime) {
            updateTowerState();
            endTime = timeSource.getTime() + (INTERVAL * 1000);
        }
    }

    public void updateTowerState()  {
        switch (towerState) {
            case IDLE -> {
                if (isTargetVisible())  {
                    owner.getEntity().getEvents().trigger(PREP_ATTACK);
                    towerState = STATE.PREP_ATTACK;
                }
            }
            case PREP_ATTACK -> {
                if (isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(ATTACK);
                    towerState = STATE.ATTACK;
                } else {
                    owner.getEntity().getEvents().trigger(IDLE);
                    towerState = STATE.IDLE;
                }
            }
            case ATTACK -> {
                if (!isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(IDLE);
                    towerState = STATE.IDLE;
                } else {
                    owner.getEntity().getEvents().trigger(ATTACK);
                    Entity newProjectile = ProjectileFactory.createFireBall(owner.getEntity(),
                            new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f, 2f));
                    newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.75),
                            (float) (owner.getEntity().getPosition().y + 0.75));
                    ServiceLocator.getEntityService().register(newProjectile);
                }
            }
        }
    }

    public void stop() {
        super.stop();
        owner.getEntity().getEvents().trigger(IDLE);
    }

    public int getPriority() {
        return !isTargetVisible() ? 0 : priority;
    }

    private int getActivePriority() {
        return !isTargetVisible() ? 0 : priority;
    }

    private int getInactivePriority() {
        return isTargetVisible() ? priority : 0;
    }

    public boolean isTargetVisible() {
        return physics.raycast(towerPosition, maxRangePosition, TARGET, hit);
    }
}
