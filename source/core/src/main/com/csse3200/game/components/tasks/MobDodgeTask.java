package com.csse3200.game.components.tasks;

import com.csse3200.game.components.tasks.MobTask.MobTask;
import com.csse3200.game.components.tasks.MobTask.MobType;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * This task runs the AI that adds a dodge mechanic/functionality for the mobs
 * in the MobsFactory. Inherits from the MobWanderTask that handles the death
 * and movement mechanics of the entity.
 * <p>
 * Relies heavily on the attached DodgingComponent
 * that adds the
 * "dodgeIncomingEntity" event to the attached entity.
 * </p>
 */
public class MobDodgeTask extends MobTask {

  private final int priority; // active priority

  private GameTime timeSource;
  private long endTime;

  // Helps task wait between each interval.
  private static final int DELAY_INTERVAL = 500;

  /**
   * Initialises a mob dodge task with a specified wander range, wait time, and
   * priority level.
   * 
   * @param mobType Distance in X and Y the entity can move from its position
   *                    when start() is called.
   * @param priority Priority level compared to other added tasks.
   */
  public MobDodgeTask(MobType mobType, int priority) {
    super(mobType);
    this.priority = priority;

    timeSource = ServiceLocator.getTimeSource();
  }

  /**
   * Start running the task. Usually called by the AI controller.
   */
  @Override
  public void start() {
    super.start();
    owner.getEntity().getEvents().trigger("mob_walk");

    // endTime = timeSource.getTime() + (1 * DELAY_INTERVAL);
    endTime = timeSource.getTime();
  }

  /**
   * Run a frame of the task. In this extension of the update(), the
   * "dodgeIncomingEntity" event will be detected and triggered on set intervals.
   */
  @Override
  public void update() {
    super.update();
    if (timeSource.getTime() >= endTime) {
      owner.getEntity().getEvents().trigger("dodgeIncomingEntity",
          owner.getEntity().getCenterPosition());
      

      endTime = timeSource.getTime() + DELAY_INTERVAL; // update time
    }
  }

  /**
   * Returns the priority level of this dodge task initialised in the constructor.
   */
  @Override
  public int getPriority() {
    return priority;
  }
}
