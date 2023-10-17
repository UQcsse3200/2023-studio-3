package com.csse3200.game.components.tasks.bombship;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * BombshipWanderTask is the entry point for the engineer entity's behaviour. Instantiates subtasks HumanWaitTask,
 * BombshipMovementTask and BombshipCombatTask, and manages transitions between the tasks. Bombship damage and destruction is
 * handled in this class.
 */
public class BombshipWanderTask extends DefaultTask implements PriorityTask {
  private static final float STOP_DISTANCE = 0.5f;
  private static final int DEFAULT_PRIORITY = 1;
  private static final String START = "start";
  private static final String DESTROY = "destroy";
  private static final String IDLE = "idle";
  private AnimationRenderComponent animator;
  private final float maxRange;
  private final float waitTime;
  private BombshipMovementTask movementTask;
  private BombshipWaitTask waitTask;
  private BombshipCombatTask combatTask;
  private Task currentTask;
  private boolean isDestroyed = false;

  /**
   * Constructor of BombshipWanderTask
   *
   * @param waitTime How long in seconds to wait between wandering.
   * @param maxRange Maximum of the entity to fight
   */
  public BombshipWanderTask(float waitTime, float maxRange) {
    this.waitTime = waitTime;
    this.maxRange = maxRange;
  }

  /**
   * Fetches the priority of this task.
   * @return current priority of this task. Priority for this task is a set value and does not change.
   */
  @Override
  public int getPriority() {
    return DEFAULT_PRIORITY; // Low priority task
  }

  /**
   * Starts the BombshipWanderTask instance and instantiates subtasks (BombshipWaitTask, BombshipWanderTask, BombshipCombatTask).
   */
  @Override
  public void start() {
    super.start();
    Vector2 startPos = owner.getEntity().getCenterPosition();
    waitTask = new BombshipWaitTask(waitTime);
    waitTask.create(owner);

    movementTask = new BombshipMovementTask(startPos, STOP_DISTANCE);
    movementTask.create(owner);
    movementTask.start();

    combatTask = new BombshipCombatTask(maxRange);
    combatTask.create(owner);
    combatTask.start();

    currentTask = movementTask;

    animator = owner.getEntity().getComponent(AnimationRenderComponent.class);
  }

  /**
   * Operates the main logic of the entity in this task. All calls to switch to particular states are determined during
   * the update phase.
   * The logical flow is:
   *      - Check if the entity has died since last update
   *      - Check if the entity has finished dying
   *      - If not dead
   */
  @Override
  public void update() {
    // Check if bombship has destroyed since last update
    if (!isDestroyed) {
      doBombshipThings();
      startDestroying();;
      currentTask.update();
    } else if (isDestroyed && animator.isFinished()) {
      owner.getEntity().setFlagForDelete(true);
    }
  }

  private void doBombshipThings() {
    if (currentTask.getStatus() != Status.ACTIVE) {
      // if the engineer is in move state and update has been called, engineer has arrived at destination
      if (currentTask == movementTask) {
        startWaiting();
        owner.getEntity().getEvents().trigger(IDLE);

      } else if (combatTask.isEngineerDied()) {
        owner.getEntity().getEvents().trigger(START);
      }
    }
  }

  /**
   * Handle the dying phase of the entity. Triggers an event to play the appropriate media,
   * sets HitBox and Collider components to ignore contact (stops the body being pushed around)
   * and stops the current task.
   */
  private void startDestroying() {
    owner.getEntity().getEvents().trigger(DESTROY);
    owner.getEntity().getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NONE);
    owner.getEntity().getComponent(HitboxComponent.class).setLayer(PhysicsLayer.NONE);
    currentTask.stop();
    isDestroyed = true;
  }

  /**
   * Starts the wait task.
   */
  private void startWaiting() {
    swapTask(waitTask);
  }

  /**
   * Allows manual switching of tasks, from the current task to the supplied newTask.
   * @param newTask the task being switched to.
   */
  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }

    currentTask = newTask;
    currentTask.start();
  }
}
