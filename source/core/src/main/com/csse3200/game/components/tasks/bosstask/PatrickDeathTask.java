package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class PatrickDeathTask extends DefaultTask implements PriorityTask {

    private boolean startFlag = false;
    private static final int PRIORITY = 3;

    /**
     * What is run when patrick's death task is assigned
     */
    @Override
    public void start() {
        super.start();
        startFlag = true;
        owner.getEntity().getEvents().trigger("patrick_death");
    }

    /**
     * What is run every frame
     */
    @Override
    public void update() {
        if (startFlag && owner.getEntity().getComponent(AnimationRenderComponent.class).
                isFinished()) {
            owner.getEntity().setFlagForDelete(true);
            ServiceLocator.getGameEndService().updateEngineerCount();
            dropCurrency();
        }
    }

    /**
     * @return priority of task
     */
    @Override
    public int getPriority() {
        return PRIORITY;
    }

    private void dropCurrency() {
        // Create and register 5 crystal drops around the bossPosition
        for (int i = 0; i < 5; i++) {
            Entity crystal = DropFactory.createCrystalDrop();

            // Calculate positions around the bossPosition
            float offsetX = MathUtils.random(-1f, 1f); // Adjust the range as needed
            float offsetY = MathUtils.random(-1f, 1f);
            float dropX = owner.getEntity().getPosition().x + offsetX;
            float dropY = owner.getEntity().getPosition().y + offsetY;
            crystal.setPosition(dropX, dropY);
            ServiceLocator.getEntityService().register(crystal);
        }
    }
}
