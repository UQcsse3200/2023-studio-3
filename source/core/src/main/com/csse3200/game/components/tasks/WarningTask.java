package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.WarningComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarningFlashTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(WarningFlashTask.class);
    long startTime;
    @Override
    public void start() {
        super.start();
        logger.info("WarningFlashTask started.");
        startTime = ServiceLocator.getTimeSource().getTime();
    }

    @Override
    public void update() {
        owner.getEntity().getComponent(WarningComponent.class).update();
        if (ServiceLocator.getTimeSource().getTime() > startTime + 2000) {
            stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
