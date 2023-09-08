package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.currency.Scrap;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * The CurrencyTask updates the in-game currency based on time intervals.
 */
public class CurrencyTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyTask.class);
    private final int priority;  // The active priority this task will have
    private final GameTime timeSource;
    private long endTime;
    private int interval;
    private final Scrap scrap = new Scrap(); // currency to update
    private final int currencyAmount = scrap.getAmount(); // amount of currency to update
    private static final String IDLE = "idleStartEco";
    private static final String MOVE = "moveStartEco";

    /**
     * @param priority Task priority for currency updates. Must be a positive integer.
     */
    public CurrencyTask(int priority, int interval) {
        this.priority = priority;
        this.interval = interval;
        this.timeSource = ServiceLocator.getTimeSource();
    }


    /**
     * Starts the Task running and initializes currency-related variables.
     */
    @Override
    public void start() {
        super.start();
        endTime = timeSource.getTime();
        owner.getEntity().getEvents().trigger(IDLE);
    }

    /**
     * The update method is what is run every time the TaskRunner in the AiTaskComponent calls update().
     * Updates the currency based on time intervals.
     */

    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            owner.getEntity().getEvents().trigger(MOVE);
            updateCurrency(); // update currency
            endTime = timeSource.getTime() + (30 * 1000); // reset end time

        }
    }

    /**
     * Updates the currency based on time intervals.
     */
    public void updateCurrency() {
        //logger.info("Updating currency");
        ServiceLocator.getCurrencyService().getScrap().modify(currencyAmount/2);
        ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats(); // update currency display

    }

    /**
     * For stopping the running task
     */
    @Override
    public void stop() {
        super.stop();
    }

    /**
     * Returns the current priority of the task.
     * @return active priority value
     */
    @Override
    public int getPriority() {
        return priority;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
