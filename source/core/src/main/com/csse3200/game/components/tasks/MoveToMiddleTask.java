package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;

public class MoveToMiddleTask extends DefaultTask implements PriorityTask {
    private final Vector2 targetPosition;
    private final float speed;
    private boolean hasReachedTarget = false;

    public MoveToMiddleTask(Vector2 targetPosition, float speed) {
        this.targetPosition = targetPosition;
        this.speed = speed;
    }

    @Override
    public int getPriority() {
        return 1; // Adjust priority as needed
    }

    @Override
    public void start() {
        super.start();
        hasReachedTarget = false;
    }

    @Override
    public void update() {
        Entity entity = owner.getEntity();
        Vector2 currentPosition = entity.getPosition();

        if (!hasReachedTarget) {
            // Calculate the movement direction towards the target
            Vector2 moveDirection = targetPosition.cpy().sub(currentPosition).nor();

            // Calculate the distance to move this frame based on speed
            float distanceToMove = speed;

            // Move the entity towards the target
            Vector2 newPosition = currentPosition.cpy().add(moveDirection.scl(distanceToMove));
            entity.setPosition(newPosition);

            // Check if the entity has reached the target
            if (currentPosition.dst2(targetPosition) < 0.1f) {
                hasReachedTarget = true;
            }
        }

        // Stop the task if the entity has reached the target
        if (hasReachedTarget) {
            setStatus();
        }
    }

    private void setStatus() {
        status = Status.COMPLETED;
    }
}

