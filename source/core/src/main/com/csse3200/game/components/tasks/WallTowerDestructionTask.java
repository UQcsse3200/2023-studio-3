package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
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
public class WallTowerDestructionTask extends DefaultTask implements PriorityTask {
    // constants

    // The type of targets this tower will detect
    private static final short TARGET = PhysicsLayer.NPC;
    //Following constants are names of events that will be triggered in the state machine
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
    public WallTowerDestructionTask(int priority, float maxRange) {
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

        endTime = timeSource.getTime() + (5000);
    }

    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateTowerState();
            endTime = timeSource.getTime() + (1000);
        }
    }

    public void updateTowerState() {

        if (owner.getEntity().getComponent(CombatStatsComponent.class).getHealth() <= 0 && towerState != STATE.DEATH) {
            owner.getEntity().getEvents().trigger(DEATH);
            if (owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
                owner.getEntity().setFlagForDelete(true);
            }
        }
    }
    public int getPriority() {
        return !isTargetVisible() ? 0 : priority;
    }
    public boolean isTargetVisible() {
        return physics.raycast(towerPosition, maxRangePosition, TARGET, hit);
    }
}
