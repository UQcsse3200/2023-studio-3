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
 * The FireworksTowerCombatTask runs the AI for the FireworksTower class. The tower scans for mobs and targets in a
 * straight line from its centre coordinate and executes the trigger phrases for animations depeending on the current
 * state of tower.
 */
public class FireworksTowerCombatTask extends DefaultTask implements PriorityTask {
    // constants
    // Time interval (in seconds) to scan for enemies
    private static final int INTERVAL = 1;
    // The type of targets this tower will detect
    private static final short TARGET = PhysicsLayer.NPC;
    private static final String IDLE = "startIdle";
    public static final String PREP_ATTACK = "startAttackPrep";
    public static final String ATTACK = "startAttack";
    public static final String DEATH = "startDeath";

    // Class attributes
    private final int priority;
    private final float maxRange;
    private Vector2 towerPosition = new Vector2(10, 10);
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();

    public enum STATE {
        IDLE, ATTACK, DEATH
    }
    public STATE towerState = STATE.IDLE;

    /**
     * @param priority Task priority when targets are detected (0 when nothing is present)
     * @param maxRange Maximum effective range of the StunTower.
     */
    public FireworksTowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * Starts the task running and starts the Idle animation
     */
    @Override
    public void start() {
        super.start();
        // Get the tower coordinates
        this.towerPosition = owner.getEntity().getCenterPosition();
        this.maxRangePosition.set(towerPosition.x + maxRange, towerPosition.y);
        // Set the default state to IDLE state
        owner.getEntity().getEvents().trigger(IDLE);

        endTime = timeSource.getTime() + (INTERVAL * 5000);
    }

    /**
     * updates the current state of the tower based on the current state of the game. If enemies are detected, attack
     * state is activated and otherwise idle state remains.
     */
    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateTowerState();
            endTime = timeSource.getTime() + (INTERVAL * 1000);
        }
    }

    /**
     * This method acts is the state machine for StunTower. Relevant animations are triggered based on relevant state
     * of the game. If enemies are detected, state of the tower is changed to attack state.
     */
    public void updateTowerState() {

        if (owner.getEntity().getComponent(CombatStatsComponent.class).getHealth() <= 0 && towerState != STATE.DEATH) {
            owner.getEntity().getEvents().trigger(DEATH);
            towerState = STATE.DEATH;
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
                if (!isTargetVisible()) {
                    owner.getEntity().getEvents().trigger(IDLE);
                    towerState = STATE.IDLE;
                } else {
                    owner.getEntity().getEvents().trigger(ATTACK);
                    Entity newProjectile = ProjectileFactory.createSplitFireWorksFireball(PhysicsLayer.NPC,
                            // double check if this is the correct projectile to call
                            new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f, 2f),
                            3);
                    newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.25),
                            (float) (owner.getEntity().getPosition().y + 0.25));
                    ServiceLocator.getEntityService().register(newProjectile);
                }
            }
            case DEATH -> {
                if (owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
                    owner.getEntity().setFlagForDelete(true);
                }
            }
        }
    }

    /**
     * Returns the state that the tower is currently in
     * @return this.towerState
     */
    public STATE getState() {
        return this.towerState;
    }

    /**
     * stops the current animation and switches back the state of the tower to IDLE.
     */
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

    /**
     * Searches for enemies/mobs in a straight line from the centre of the tower to maxRange in a straight line.
     * @return true if targets are detected, false otherwise
     */
    public boolean isTargetVisible() {
        return physics.raycast(towerPosition, maxRangePosition, TARGET, hit);
    }
}
