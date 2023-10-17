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

import static java.lang.Math.round;

/**
 * The FireTowerCombatTask runs the AI for the FireTower class. The tower implementing this task will scan for enemies
 * in a straight line from the current position to a maxRange, and change the state of the tower.
 */
public class FireTowerCombatTask extends DefaultTask  implements PriorityTask {
    //constants
    private static final int INTERVAL = 1; //time interval to scan for enemies in seconds
    private static final short TARGET = PhysicsLayer.NPC; //the type of targets this tower will detect
    //The constants are names of events that will be triggered in the state machine
    public static final String IDLE = "startIdle";
    public static final String PREP_ATTACK = "startAttackPrep";
    public static final String ATTACK = "startAttack";
    public static final String DEATH = "startDeath";


    //Class attributes
    private final int priority;
    private final float maxRange;

    private float fireRateInterval;
    private Vector2 towerPosition = new Vector2(10, 10);
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();
    private boolean shoot = true;

    public enum STATE {
        IDLE, PREP_ATTACK, ATTACK, DEATH
    }
    private STATE towerState = STATE.IDLE;

    /**
     * Starts the task running, triggers the initial 'IDLE' event
     */
    public FireTowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        this.fireRateInterval = 1;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * starts this task and triggers the IDLE animation
     */
    @Override
    public void start()  {
        super.start();
        // get the tower coordinates
        this.towerPosition = owner.getEntity().getCenterPosition().sub(0.125f,0.125f);
        this.maxRangePosition.set(towerPosition.x  + maxRange, towerPosition.y);
        owner.getEntity().getEvents().addListener("addFireRate",this::changeFireRateInterval);
        //default to idle state
        owner.getEntity().getEvents().trigger(IDLE);

        endTime = timeSource.getTime() + (INTERVAL *  500);
    }

    /**
     * this method is called everytime state of the tower needs to be changed.
     */
    @Override
    public void update()  {
        if  (timeSource.getTime() >= endTime) {
            updateTowerState();
            if (towerState == STATE.ATTACK) {
                endTime = timeSource.getTime() + round(fireRateInterval * 1000);
            } else
                endTime = timeSource.getTime() + (INTERVAL * 1000);
        }
    }

    /**
     * finite state machine for the FireTower. Detects mobs in a straight line and changes the state of the tower.
     */
    public void updateTowerState()  {
        if (owner.getEntity().getComponent(CombatStatsComponent.class).getHealth() <= 0 && towerState != STATE.DEATH) {
            owner.getEntity().getEvents().trigger(DEATH);
            towerState = STATE.DEATH;
            return;
        }

        switch (towerState) {
            case IDLE -> handleIdleState();
            case PREP_ATTACK -> handlePrepAttackState();
            case ATTACK -> handleAttackState();
            default -> handleDeathState();     // DEATH
        }
    }

    /**
     * stops the current animation.
     */
    @Override
    public void stop() {
        super.stop();
        owner.getEntity().getEvents().trigger(IDLE);
    }

    /**
     * @return returns the current state of the tower
     */
    public STATE getState() {
        return this.towerState;
    }

    /**
     * gets the priority for the current task.
     * @return (int) active priority if target is visible and inactive priority otherwise
     */
    public int getPriority() {
        return !isTargetVisible() ? 0 : priority;
    }

    /**
     * detects targets from the centre of the tower to maxRange in a straight line.
     * @return true if mobs are present and false otherwise.
     */
    public boolean isTargetVisible() {
        boolean top = physics.raycast(towerPosition.add(0f,0.4f), maxRangePosition.add(0f,0.4f), TARGET, hit);
        boolean bottom = physics.raycast(towerPosition.sub(0f,0.4f), maxRangePosition.sub(0f,0.4f), TARGET, hit);
        return top || bottom;
    }

    private void changeFireRateInterval(int newInterval) {
        fireRateInterval = 1 / ((float) newInterval / 5);
    }

    /**
     * Function for getting the tower's state.
     *
     * @return The tower's state
     */
    public STATE getTowerState() {
        return this.towerState;
    }

    /**
     * Function for setting the tower's state
     *
     * @param newState The new state of this tower
     */
    public void setTowerState(STATE newState) {
        this.towerState = newState;
    }

    /**
     * Function triggers actions at IDLE state, then switch to PREP_ATTACK
     */
    private void handleIdleState() {
        if (isTargetVisible())  {
            owner.getEntity().getEvents().trigger(PREP_ATTACK);
            towerState = STATE.PREP_ATTACK;
        }
    }

    /**
     * Functions triggers actions at PREP_ATTACH state, then switch to ATTACK or IDLE
     */
    private void handlePrepAttackState() {
        if (isTargetVisible()) {
            owner.getEntity().getEvents().trigger(ATTACK);
            towerState = STATE.ATTACK;
        } else {
            owner.getEntity().getEvents().trigger(IDLE);
            towerState = STATE.IDLE;
        }
    }

    /**
     * Functions trigger actions at ATTACK state
     */
    private void handleAttackState() {
        if (shoot) {
            if (!isTargetVisible()) {
                owner.getEntity().getEvents().trigger(IDLE);
                towerState = STATE.IDLE;
            } else {
                owner.getEntity().getEvents().trigger(ATTACK);
                Entity newProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC,
                        new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f, 2f), ProjectileEffects.BURN, false);
                newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.25),
                        (owner.getEntity().getPosition().y));
                ServiceLocator.getEntityService().register(newProjectile);
            }
        }

        shoot = !shoot;
    }

    /**
     * Functions triggers actions at DEATH state
     */
    private void handleDeathState() {
        if (owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
            owner.getEntity().setFlagForDelete(true);
        }
    }
}
