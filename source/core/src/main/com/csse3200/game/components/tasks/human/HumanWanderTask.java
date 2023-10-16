package com.csse3200.game.components.tasks.human;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * HumanWanderTask is the entry point for the engineer entity's behaviour. Instantiates subtasks HumanWaitTask,
 * HumanMovementTask and EngineerCombatTask, and manages transitions between the tasks. Engineer damage and death
 * handled in this class.
 */
public class HumanWanderTask extends DefaultTask implements PriorityTask {
  private static final int TOLERANCE = 1;
  private static final float STOP_DISTANCE = 0.5f;
  private static final int DEFAULT_PRIORITY = 1;
  private static final String DEATH_EVENT = "deathStart";
  private static final String IDLE_EVENT = "idleRight";
  private AnimationRenderComponent animator;
  private final float maxRange;
  private final float waitTime;
  private HumanMovementTask movementTask;
  private HumanWaitTask waitTask;
  private EngineerCombatTask combatTask;
  private Task currentTask;
  private boolean   isDead = false;

  private boolean isSelected = false;

  private boolean hasDied = false;

  /**
   * Constructor of HumanWanderTask
   *
   * @param waitTime How long in seconds to wait between wandering.
   * @param maxRange Maximum detection and fighting range of the entity
   */
  public HumanWanderTask(float waitTime, float maxRange) {
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
   * Starts the HumanWanderTask instance and instantiates subtasks (HumanWaitTask, HumanWanderTask, EngineerCombatTask).
   */
  @Override
  public void start() {
    super.start();
    Vector2 startPos = owner.getEntity().getCenterPosition();
    waitTask = new HumanWaitTask(waitTime);
    waitTask.create(owner);

    movementTask = new HumanMovementTask(startPos, STOP_DISTANCE);
    movementTask.create(owner);
    movementTask.start();

    combatTask = new EngineerCombatTask(maxRange);
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
    hasDied =  owner.getEntity().getComponent(CombatStatsComponent.class).isDead();
    // Check if engineer has died since last update
    if (!isDead && hasDied) {
      startDying();
    }

    boolean justDied = owner.getEntity().getComponent(CombatStatsComponent.class).isDead();
    // Check if engineer has died since last update
    if (!isDead && justDied) {
      startDying();
    } else if (isDead && animator.isFinished()) {
      owner.getEntity().setFlagForDelete(true);
      // Decrement the engineer count
      ServiceLocator.getGameEndService().updateEngineerCount();
    }

    // otherwise doing engineer things since engineer is alive
    else if (!isDead){
      doEngineerThings();
      currentTask.update();
    }
  }

  private void doEngineerThings() {
    if (currentTask.getStatus() == Status.ACTIVE) {
      return;
    }

    // if the engineer is in move state and update has been called, engineer has arrived at destination
    if (currentTask == movementTask) {
      startWaiting();
      owner.getEntity().getEvents().trigger(IDLE_EVENT);
    } else if (combatTask.isTargetVisible()) {
      float engY = owner.getEntity().getCenterPosition().y;
      float targetY = combatTask.fetchTarget().y;
      // if the engineer is positioned within the tolerance range of the mob's y position, enter combat state
      if (engY < targetY + TOLERANCE &&
              engY > targetY - TOLERANCE) {
        startCombat();

        // move into position for targeting mob
      } else if (!this.isSelected()) {
          Vector2 newPos = new Vector2(owner.getEntity().getPosition().x, combatTask.fetchTarget().y);
          startMoving(newPos);
      }
    }
  }

  /**
   * Handle the dying phase of the entity. Triggers an event to play the appropriate media,
   * sets HitBox and Collider components to ignore contact (stops the body being pushed around)
   * and stops the current task.
   */
  private void startDying() {
    owner.getEntity().getEvents().trigger(DEATH_EVENT);
    owner.getEntity().getComponent(ColliderComponent.class).setLayer(PhysicsLayer.NONE);
    owner.getEntity().getComponent(HitboxComponent.class).setLayer(PhysicsLayer.NONE);
    currentTask.stop();
    isDead = true;
  }

  /**
   * Starts the wait task.
   */
  public void startWaiting() {
    swapTask(waitTask);
  }

  /**
   * Starts the movement task, to a particular destination
   * @param destination the Vector2 position to which the entity needs to move
   */
  public void startMoving(Vector2 destination) {
    movementTask.setTarget(destination);
    swapTask(movementTask);
  }

  /**
   * Starts the combat task.
   */
  public void startCombat() {
    swapTask(combatTask);
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

  private boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }
}
