package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.MobBossFactory;
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
        }
    }

    /**
     * @return priority of task
     */
    @Override
    public int getPriority() {
        return PRIORITY;
    }
}