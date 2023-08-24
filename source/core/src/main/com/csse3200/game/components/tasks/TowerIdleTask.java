package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task for the Tower Idle State - specifically for the
 * Weapon Tower Entity that can move between combat and
 * idle states. Scans for enemy mobs but does nothing else.
 */
public class TowerIdleTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(MovementTask.class);
    private static final float SCAN_RANGE = 500;
    private static final int ACTIVE_PRIORITY = 0;
    private static final int INACTIVE_PRIORITY = -1;
    private final GameTime timeSource;
    private final float interval;
    private long endTime;
    private final PhysicsEngine physics;
    private final DebugRenderer debugRenderer;
    private final RaycastHit hit = new RaycastHit();

    /**
     * @param interval time between scanning for mobs, in seconds.
     */
    public TowerIdleTask(float interval) {
        timeSource = ServiceLocator.getTimeSource();
        physics = ServiceLocator.getPhysicsService().getPhysics();
        this.interval = interval;
        debugRenderer = ServiceLocator.getRenderService().getDebug();
        logger.debug("Weapon Tower idleTask started");
    }

    /**
     * Start waiting from now until interval has passed.
     */
    @Override
    public void start() {
        super.start();
        endTime = timeSource.getTime() + (int)(this.interval * 1000);
    }

    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            if (isTargetVisible()) {
                owner.getEntity().getEvents().trigger("deployStart");
                logger.debug("Idle Task update function: Detected a target!");
            }
            status = Status.FINISHED;
        }
    }

    /**
     * Scan for any mob entities in the lane. Triggers the 'mobsVisible' event
     **/
    private boolean isTargetVisible() {
        Vector2 from = owner.getEntity().getCenterPosition();
        Vector2 to = new Vector2(from.x + SCAN_RANGE, from.y);

        // If there is an obstacle in the path to the end of the tower scan range
        // must be mobs present.
        if (physics.raycast(from, to, PhysicsLayer.NPC, hit)) {
            debugRenderer.drawLine(from, hit.point);
            return true;
        }
        debugRenderer.drawLine(from, to);
        return false;
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return getActivePriority();
        }

        return getInactivePriority();
    }

    public int getActivePriority() {
        return this.ACTIVE_PRIORITY;
    }

    public int getInactivePriority() {
        return this.INACTIVE_PRIORITY;
    }

}