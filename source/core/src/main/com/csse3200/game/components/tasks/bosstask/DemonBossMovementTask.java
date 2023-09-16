package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.tasks.MovementTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemonBossMovementTask extends DefaultTask implements PriorityTask {

    private static final Logger logger = LoggerFactory.getLogger(DemonBossMovementTask.class);
    private static final int PRIORITY = 3;
    private Vector2 currentPos;
    private MovementTask movementTask;

    private enum STATE {
        WALK, JUMP, IDLE
    }
    private STATE demonState = STATE.IDLE;

    public DemonBossMovementTask() {

    }
    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
