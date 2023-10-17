package com.csse3200.game.components.tasks.scanner;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EngineerFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameEndService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * ScannerTask implements the behaviour of GapScannerEntities that detect the
 * conditions to trigger engineer spawning, i.e., No towers, no engineers, mobs within
 * a certain distance.
 */
public class ScannerTask extends DefaultTask implements PriorityTask {

    private static final int SCAN_INTERVAL = 1000;  // how often to scan, in milliseconds
    private final PhysicsEngine physics;
    private final GameTime timeSource;
    private final RaycastHit hit = new RaycastHit();
    private Vector2 selfPosition;
    private long endTime;

    // booleans to track presence of towers, engineers and mobs
    private boolean towers = false;
    private boolean engineers = false;
    private boolean mobs = false;

    // track the number of engineers spawned.
    private static final int MAX_ENGINEERS = ServiceLocator.getGameEndService().getEngineerCount();


    /**
     * ScannerTask Constructor
     */
    public ScannerTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    /**
     * Start method for the ScannerTask
     */
    @Override
    public void start() {
        super.start();
        endTime = timeSource.getTime() + (SCAN_INTERVAL);
        selfPosition = owner.getEntity().getCenterPosition();
    }

    /**
     * Update method for the scanner task. Implements the scanning and spawning logic
     * for populating the game area with engineers.
     */
    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            // clear all presence booleans
            towers = false;
            engineers = false;
            mobs = false;

            // carry out scan and behave accordingly
            scan();
            if (!towers && !engineers && mobs) {
                // spawn engineers now
                if (ServiceLocator.getGameEndService().getNumSpawnedEngineers() < MAX_ENGINEERS) {
                    Entity engineer = EngineerFactory.createEngineer();

                    engineer.setPosition(new Vector2((int)(selfPosition.x + 1),(int) selfPosition.y));
                    ServiceLocator.getEntityService().register(engineer);
                    ServiceLocator.getGameEndService().incrementNumSpawnedEngineers();
                }
            }
            endTime = timeSource.getTime() + SCAN_INTERVAL;
        }
    }

    /**
     * Scanning method that detects the presence of towers/engineers/mobs.
     * Sets the tracking booleans for each of the entity types
     */
    private void scan() {

        if (physics.raycast(selfPosition,
                new Vector2(selfPosition.x + 10, selfPosition.y),
                PhysicsLayer.TOWER,
                hit)) {
            towers = true;
        } else if (physics.raycast(selfPosition,
                new Vector2(selfPosition.x + 10, selfPosition.y),
                PhysicsLayer.ENGINEER,
                hit)) {
            engineers = true;
        } else if (physics.raycast(selfPosition,
                new Vector2(selfPosition.x + 10, selfPosition.y),
                PhysicsLayer.NPC,
                hit)) {
            mobs = true;
        }
    }

    /**
     * Return the priority of the task.
     * @return the default priority of this task (a fixed value - no other tasks to run)
     */
    @Override
    public int getPriority() {
        return 1;
    }
}
