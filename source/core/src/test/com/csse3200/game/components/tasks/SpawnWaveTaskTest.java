package com.csse3200.game.components.tasks;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class SpawnWaveTaskTest {

    @Test
    void shouldTriggerSpawning() {
        GameTime time = mock(GameTime.class);
        when(time.getTime()).thenReturn(11000L);
        ServiceLocator.registerTimeSource(time);
        SpawnWaveTask waveTask = new SpawnWaveTask();

        AITaskComponent aiTaskComponent = new AITaskComponent().addTask(waveTask);
        Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
        entity.create();

        // Register callbacks
        EventListener0 callback = mock(EventListener0.class);
        entity.getEvents().addListener("spawnWave", callback);

        waveTask.update();

        verify(callback).handle();
    }
}