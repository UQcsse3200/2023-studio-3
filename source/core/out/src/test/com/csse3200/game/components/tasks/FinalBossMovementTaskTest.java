package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.bosstask.FinalBossMovementTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class FinalBossMovementTaskTest {
    @Mock
    GameTime gameTime;

    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }

    @Test
    void shouldTriggerEvent() {
        FinalBossMovementTask FBMTask = new FinalBossMovementTask(1f, 2);

        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(FBMTask);
        Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
        entity.create();

        // Register callbacks
        EventListener0 callback = mock(EventListener0.class);
        entity.getEvents().addListener("finalBossMovementStart", callback);

        FBMTask.start();

        verify(callback).handle();
    }
}