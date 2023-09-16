package com.csse3200.game.components.tasks.bosstask;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemonBossMovementTask extends DefaultTask implements PriorityTask {

    private static final Logger logger = LoggerFactory.getLogger(DemonBossMovementTask.class);
    private int priority;
    @Override
    public int getPriority() {
        return this.priority;
    }
}
