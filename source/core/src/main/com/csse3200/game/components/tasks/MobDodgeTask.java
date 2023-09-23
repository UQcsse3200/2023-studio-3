package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * This task runs the AI that adds a dodge mechanic/functionality for the mobs
 * in the MobsFactory.
 */
public class MobDodgeTask extends MobWanderTask {

  private final int priority; // active priority

  private GameTime timeSource;
  private long endTime;

  // Helps task wait between each interval.
  private final int DELAY_INTERVAL = 500;

  public MobDodgeTask(Vector2 wanderRange, float waitTime, int priority) {
    super(wanderRange, waitTime);
    this.priority = priority;

    timeSource = ServiceLocator.getTimeSource();
  }

  @Override
  public void start() {
    super.start();
    owner.getEntity().getEvents().trigger("wanderStart");

    endTime = timeSource.getTime() + (1 * DELAY_INTERVAL);
  }

  @Override
  public void update() {
    super.update();
    if (timeSource.getTime() >= endTime) {
      owner.getEntity().getEvents().trigger("dodgeProj", owner.getEntity().getCenterPosition());
      endTime = timeSource.getTime() + (1 * DELAY_INTERVAL);
    }

  }

  @Override
  public void stop() {
    super.stop();
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
