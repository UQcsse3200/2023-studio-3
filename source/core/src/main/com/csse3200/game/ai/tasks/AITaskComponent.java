package com.csse3200.game.ai.tasks;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.ComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Task-based AI component. Given a list of tasks with priorities, the AI component will run the
 * highest priority task each frame. Tasks can be made up of smaller sub-tasks. A negative priority
 * indicates that the task should not be run.
 *
 * <p>This is a simple implementation of Goal-Oriented Action Planning (GOAP), a common AI decision
 * algorithm in games that's more powerful than Finite State Machines (FSMs) (State pattern).
 */
public class AITaskComponent extends Component implements TaskRunner {
  private static final Logger logger = LoggerFactory.getLogger(AITaskComponent.class);

  private final List<PriorityTask> priorityTasks = new ArrayList<>(2);
  private final List<PriorityTask> priorityTasksToBeRestored = new ArrayList<>(2);
  private PriorityTask currentTask;
  
  public boolean freezed = false;
  /**
   * Add a priority task to the list of tasks. This task will be run only when it has the highest
   * priority, and can be stopped to run a higher priority task.
   *
   * @param task Task to add
   * @return self
   */
  public AITaskComponent addTask(PriorityTask task) {
    logger.debug("{} Adding task {}", this, task);
    priorityTasks.add(task);
    task.create(this);

    return this;
  }

  /**
   * Get a task from the list of tasks. This can be used to get a reference to
   * a task to modify it. This is inspired from Entity.getComponent().
   *
   * @param task The task to get
   * @return A reference to a task with the given class
   * @param <T> The type of task to get
   */
  public <T extends PriorityTask> T getTask(Class<T> task) {
    for (PriorityTask priorityTask : priorityTasks) {
      if (task.isInstance(priorityTask)) {
        return task.cast(priorityTask);
      }
    }
    logger.info("Task {} not found", task);
    return null;
  }

  /**
   * On update, run the current highest priority task. If it's a different one, stop the old one and
   * start the new one. If the highest priority task has negative priority, no task will be run.
   */
  @Override
  public void update() {
    PriorityTask desiredtask = getHighestPriorityTask();
    if (desiredtask == null || desiredtask.getPriority() < 0) {
      return;
    }

    if (desiredtask != currentTask && !freezed) {
      changeTask(desiredtask);
    }
	if(currentTask!=null)
		currentTask.update();
  }

  @Override
  public void dispose() {
    if (currentTask != null) {
      currentTask.stop();
    }
  }

  /**
   * Empties the priorityTasks List. Disposes all of the entity's tasks.
   */
  public void disposeAll() {
    currentTask = null;
    priorityTasksToBeRestored.addAll(priorityTasks);
    priorityTasks.clear();
  }

  /**
   * Restores the priorityTasks List. Adds all of the entity's disposed tasks 
   * back into priorityTasks.
   */
  public void restore() {
    priorityTasks.addAll(priorityTasksToBeRestored);
    priorityTasksToBeRestored.clear();
    this.update();
  }

  public PriorityTask getHighestPriorityTask() {
    try {
      return Collections.max(priorityTasks, Comparator.comparingInt(PriorityTask::getPriority));
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public void changeTask(PriorityTask desiredTask) {
    logger.debug("{} Changing to task {}", this, desiredTask);
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = desiredTask;
    if (desiredTask != null) {
      desiredTask.start();
    }
  }
}
