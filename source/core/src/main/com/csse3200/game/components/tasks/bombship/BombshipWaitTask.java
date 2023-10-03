package com.csse3200.game.components.tasks.bombship;

import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

/**
 * Task that does nothing other than waiting for a given time. Status is Finished
 * after the time has passed.
 */
public class BombshipWaitTask extends DefaultTask {
  private final GameTime totalTime;
  private final float duration;
  private long endTime;

  /**
   * @param duration How long to wait for, in seconds.
   */
  public BombshipWaitTask(float duration) {
    totalTime = ServiceLocator.getTimeSource();
    this.duration = duration;
  }

  /**
   * Start waiting from now until duration has passed.
   */
  @Override
  public void start() {
    super.start();
    endTime = totalTime.getTime() + (int)(duration * 1000);
  }

  @Override
  public void update() {
    if (totalTime.getTime() >= endTime) {
      status = Status.FINISHED;
    }
  }
}
