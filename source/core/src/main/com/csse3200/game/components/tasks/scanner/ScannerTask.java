package com.csse3200.game.components.tasks.scanner;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.components.tasks.TowerCombatTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.EngineerFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.ArrayList;

import static java.lang.Math.round;

public class ScannerTask extends DefaultTask implements PriorityTask {

    private static final int SCAN_INTERVAL = 1 * 1000;
    private PhysicsEngine physics;
    private GameTime timeSource;
    private final RaycastHit hit = new RaycastHit();
    private Vector2 selfPosition;
    private long endTime;
    private boolean towers = false;
    private boolean mobs = false;
    private boolean engineers = false;
    private static final int maxEngineers = 3;
    private int engineerCount = 0;
    private ArrayList<Entity> engIds = new ArrayList<>();


    public ScannerTask() {
        physics = ServiceLocator.getPhysicsService().getPhysics();
        timeSource = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        endTime = timeSource.getTime() + (SCAN_INTERVAL);
        selfPosition = owner.getEntity().getCenterPosition();
    }

    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            // clear all presence bools
            towers = false;
            engineers = false;
            mobs = false;

            // carry out scan and behave accordingly
            scan();
            if (!towers && !engineers && mobs) {
                // spawn engineers now
                if (engineerCount < maxEngineers) {
                    Entity engineer = EngineerFactory.createEngineer();

                    engineer.setPosition(new Vector2((int)(selfPosition.x + 1),(int) selfPosition.y));
                    ServiceLocator.getEntityService().register(engineer);
                    engineerCount += 1;
                    engIds.add(engineer);
                }
            }
            endTime = timeSource.getTime() + SCAN_INTERVAL;
        }
    }

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

    @Override
    public int getPriority() {
        return 1;
    }
}
