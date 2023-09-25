package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
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
public class MobDodgeTask extends MobWanderTask {

  private final int priority; // active priority

  private GameTime timeSource;
  private long endTime;

  // Helps task wait between each interval.
  private final int DELAY_INTERVAL = 500;

  /**
   * Initialises a mob dodge task with a specified wander range, wait time, and
   * priority level.
   * 
   * @param wanderRange Distance in X and Y the entity can move from its position
   *                    when start() is
   *                    called.
   * @param waitTime    How long in seconds to wait between wandering.
   * @param priority    Priority level compared to other added tasks.
   */
  public MobDodgeTask(Vector2 wanderRange, float waitTime, int priority) {
    super(wanderRange, waitTime);
    this.priority = priority;

    timeSource = ServiceLocator.getTimeSource();
  }

  /**
   * Start running the task. Usually called by the AI controller.
   */
  @Override
  public void start() {
    super.start();
    owner.getEntity().getEvents().trigger("wanderStart");

    endTime = timeSource.getTime() + (1 * DELAY_INTERVAL);
  }

  /**
   * Run a frame of the task. In this extension of the update(), the
   * "dodgeIncomingEntity" event will be detected and triggered on set intervals.
   */
  @Override
  public void update() {
    super.update();
    if (timeSource.getTime() >= endTime) {
      owner.getEntity().getEvents().trigger("dodgeIncomingEntity", owner.getEntity().getCenterPosition());
      endTime = timeSource.getTime() + (1 * DELAY_INTERVAL);
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
