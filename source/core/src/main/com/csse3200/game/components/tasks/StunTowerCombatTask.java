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
 * The StunTowerCombatTask runs the AI for the StunTower class. The tower scans for mobs and targets in a straight line
 * from its centre coordinate and executes the trigger phrases for animations depending on the current state of tower.
 */
public class StunTowerCombatTask extends DefaultTask implements PriorityTask {
    //constants
    private static final int INTERVAL = 1;
    private static final short TARGET = PhysicsLayer.NPC;
    //Following constants are names of events that will be triggered in the state machine
    public static final String IDLE = "startIdle";
    public static final String ATTACK = "startAttack";
    public static final String DEATH = "startDeath";

    //Following are the class constants
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

    //enums for the state triggers
    public enum STATE {
        IDLE, ATTACK, DIE
    }
    private STATE towerState = STATE.IDLE;

    /**
     * @param priority Task priority when targets are detected (0 when nothing is present)
     * @param maxRange Maximum effective range of the StunTower.
     */
    public StunTowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        this.fireRateInterval = 1;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * Starts the task running and starts the Idle animation
     */
    @Override
    public void start() {
        super.start();
        //get the tower coordinates
        this.towerPosition = owner.getEntity().getCenterPosition().sub(0.25f, 0.25f);
        this.maxRangePosition.set(towerPosition.x + maxRange, towerPosition.y);
        owner.getEntity().getEvents().addListener("addFireRate",this::changeFireRateInterval);
        //set the default state to IDLE state
        owner.getEntity().getEvents().trigger(IDLE);

        endTime = timeSource.getTime() + (INTERVAL * 5000);
    }

    /**
     * updates the current state of the tower based on the current state of the game. If enemies are detected, attack
     * state is activated and otherwise idle state remains.
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateTowerState();
            if (towerState == STATE.ATTACK) {
                endTime = timeSource.getTime() + round(fireRateInterval * 1000);
            } else {
                endTime = timeSource.getTime() + (INTERVAL * 1000);
            }
        }
    }

    /**
     * This method acts is the state machine for StunTower. Relevant animations are triggered based on relevant state
     * of the game. If enemies are detected, state of the tower is changed to attack state.
     */
    public void updateTowerState() {
        if (owner.getEntity().getComponent(CombatStatsComponent.class).getHealth() <= 0 &&
        towerState != STATE.DIE) {
            owner.getEntity().getEvents().trigger(DEATH);
            towerState = STATE.DIE;
            return;
        }

        switch (towerState) {
            case IDLE -> {
                if(isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(ATTACK);
                    towerState = STATE.ATTACK;
                }
            }
            case ATTACK -> {
                if (shoot) {
                    if (!isTargetVisible()) {
                        owner.getEntity().getEvents().trigger(IDLE);
                        towerState = STATE.IDLE;
                    } else {
                        owner.getEntity().getEvents().trigger(ATTACK);
//                    Entity newProjectile = ProjectileFactory.createFireBall(PhysicsLayer.NPC,
//                            new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f, 2f));
                        Entity newProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC,
                                new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f, 2f),
                                ProjectileEffects.STUN, false);
                        newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.25),
                                (owner.getEntity().getPosition().y));
                        ServiceLocator.getEntityService().register(newProjectile);
                        owner.getEntity().getEvents().trigger(IDLE);
                        towerState = STATE.IDLE;
                    }
                }

                shoot = !shoot;
            }
            default -> {       // DIE
                if (owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
                    owner.getEntity().setFlagForDelete(true);
                }
            }
        }
    }

    /**
     * Function for getting the tower's state
     *
     * @return The state of this tower
     */
    public STATE getState() {
        return this.towerState;
    }

    /**
     * Function for setting the tower's state
     * @param newState The new state of this tower
     */
    public void setState(STATE newState) {
        this.towerState = newState;
    }

    /**
     * stops the current animation and switches back the state of the tower to IDLE.
     */
    @Override
    public void stop() {
        super.stop();
        owner.getEntity().getEvents().trigger(IDLE);
    }

    /**
     * returns the current priority of the task
     * @return (int) active priority if target is visible and inactive priority otherwise
     */
    public int getPriority() {
        return !isTargetVisible() ? 0 : priority;
    }

    public int getActivePriority() {
        return !isTargetVisible() ? 0 : priority;
    }

    public int getInactivePriority() {
        return isTargetVisible() ? priority : 0;
    }

    /**
     * Searches for enemies/mobs in a straight line from the centre of the tower to maxRange in a straight line.
     * @return true if targets are detected, false otherwise
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
     * Function for getting the turret's fire rate.
     *
     * @return The fireRateInterval variable
     */
    public float getFireRateInterval() {
        return fireRateInterval;
    }
}
