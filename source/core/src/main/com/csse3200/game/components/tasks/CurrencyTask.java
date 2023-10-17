package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.currency.Scrap;
import com.csse3200.game.rendering.AnimationRenderComponent;
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
    private final GameTime timeSource;
    private long endTime;
    private int interval;
    private final Scrap scrap = new Scrap(); // currency to update
    private final int currencyAmount = 10; // amount of currency to update
    private static final String IDLE = "idleStartEco";
    private static final String MOVE = "moveStartEco";
    private static final String DEATH = "deathStartEco";

    public enum STATE {
        IDLE, DEATH
    }
    public STATE towerState = STATE.IDLE;


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
        owner.getEntity().getEvents().addListener("addIncome",this::changeInterval);
        endTime = timeSource.getTime() + (interval * 1500L);
        owner.getEntity().getEvents().trigger(IDLE);
    }

    /**
     * The update method is what is run every time the TaskRunner in the AiTaskComponent calls update().
     * Updates the currency based on time intervals.
     */

    @Override
    public void update() {
        if (timeSource.getTime() >= endTime) {
            updateTowerState();
            logger.info(String.format("Interval: %d", interval));
            endTime = timeSource.getTime() + (interval * 1000L); // reset end time

        }
    }

    /**
     * This method acts is the state machine for IncomeTower. Relevant animations are triggered based on relevant state
     * of the game. If the tower runs out of health it dies.
     */
    public void updateTowerState() {
        if (owner.getEntity().getComponent(CombatStatsComponent.class).getHealth() <= 0 && towerState != STATE.DEATH) {
            owner.getEntity().getEvents().trigger(DEATH);
            towerState = STATE.DEATH;
        }

        switch (towerState) {
            case IDLE -> {
                owner.getEntity().getEvents().trigger(MOVE);
                updateCurrency(); // update currency
                towerState = STATE.IDLE;
            }
            case DEATH -> {
                if (owner.getEntity().getComponent(AnimationRenderComponent.class).isFinished()) {
                    owner.getEntity().setFlagForDelete(true);
                }
            }
        }
    }


    /**
     * Updates the currency based on time intervals.
     */
    public void updateCurrency() {
        logger.info("Updating currency");
        ServiceLocator.getCurrencyService().getScrap().modify(currencyAmount);

        Vector2 coordinates = this.owner.getEntity().getCenterPosition();
        ServiceLocator.getCurrencyService().getDisplay().currencyPopUp(coordinates.x, coordinates.y, currencyAmount, 25);

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

    public void changeInterval(int newInterval) {
        interval = newInterval;
        logger.info("Interval changed to: " + interval);
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return interval;
    }
}
