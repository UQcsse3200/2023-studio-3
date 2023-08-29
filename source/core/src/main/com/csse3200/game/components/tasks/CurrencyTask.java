package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CurrencyTask updates the in-game currency based on time intervals.
 */
public class CurrencyTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyTask.class);
    private final int priority;  // The active priority this task will have
    private final int INTERVAL = 5;  // time interval to update currency in seconds
    private final GameTime timeSource;
    private long endTime;
    private int interval;
    // private int currencyAmount = ServiceLocator.getCurrencyService().getScrap().getAmount();
     private int currencyAmount = 0;  // Current currency amount

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
        currencyAmount = 0;
        endTime = timeSource.getTime() + (INTERVAL * 1000);
    }

    /**
     * The update method is what is run every time the TaskRunner in the AiTaskComponent calls update().
     * Updates the currency based on time intervals.
     */

    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateCurrency();
            endTime = timeSource.getTime() + (interval * 1000);

            // Print the currency value to the console
            int currencyValue = 5; // Replace this with the actual currency value
            logger.info("{}", currencyValue);
        }
    }


    /**
     * Updates the currency based on time intervals.
     */
    public void updateCurrency() {
        // Update currency logic here
        currencyAmount += 50;  // Example: Add 10 currency units every interval
        logger.info("Currency updated. Current amount: " + currencyAmount);
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
