package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.WarningComponent;
import com.csse3200.game.components.tasks.WarningTask;
import com.csse3200.game.entities.Entity;

public abstract class WarningFactory {
    public static Entity createWarning(Entity mob) {
        WarningComponent warningComponent = new WarningComponent();
        warningComponent.config(mob);
        return new Entity()
                .addComponent(new AITaskComponent().addTask(new WarningTask()))
                .addComponent(warningComponent);
    }
}
