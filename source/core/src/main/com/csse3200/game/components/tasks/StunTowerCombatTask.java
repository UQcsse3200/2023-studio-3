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


public class StunTowerCombatTask extends DefaultTask implements PriorityTask {
    //constants
    private static final int INTERVAL = 1;
    private static final short TARGET = PhysicsLayer.NPC;
    //Following constants are names of events that will be triggered in the state machine
    public static final String IDLE = "startIdle";
    public static final String ATTACK = "startAttack";

    //Following are the class constants
    private final int priority;
    private final float maxRange;
    private Vector2 towerPosition = new Vector2(10, 10);
    private final Vector2 maxRangePosition = new Vector2();
    private PhysicsEngine physics;
    private GameTime timeSource;
    private long endTime;
    private final RaycastHit hit = new RaycastHit();

    //enums for the state triggers
    private enum STATE {
        IDLE, ATTACK
    }
    private STATE towerState = STATE.IDLE;

    public StunTowerCombatTask(int priority, float maxRange) {
        this.priority = priority;
        this.maxRange = maxRange;
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        //get the tower coordinates
        this.towerPosition = owner.getEntity().getCenterPosition();
        this.maxRangePosition.set(towerPosition.x + maxRange, towerPosition.y);
        //set the default state to IDLE state
        owner.getEntity().getEvents().trigger(IDLE);

        endTime = timeSource.getTime() + (INTERVAL * 5000);
    }

    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateTowerState();
            endTime = timeSource.getTime() + (INTERVAL * 1000);
        }
    }

    public void updateTowerState() {
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
                    Entity newProjectile = ProjectileFactory.createFireBall(owner.getEntity(),
                            new Vector2(100, owner.getEntity().getPosition().y), new Vector2(2f, 2f));
                    newProjectile.setPosition((float) (owner.getEntity().getPosition().x + 0.25),
                            (float) (owner.getEntity().getPosition().y + 0.25));
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

    public int getActivePriority() {
        return !isTargetVisible() ? 0 : priority;
    }

    public int getInactivePriority() {
        return isTargetVisible() ? priority : 0;
    }

    public boolean isTargetVisible() {
        return physics.raycast(towerPosition, maxRangePosition, TARGET, hit);
    }
}
