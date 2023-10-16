package com.csse3200.game.components.tasks;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class MobWanderTaskTest {
  @Mock
  GameTime gameTime;

  @BeforeEach
  void beforeEach() {
    ServiceLocator.registerTimeSource(gameTime);
  }
  //TODO: Add some tests to this class.

//  @Test
//  void shouldTriggerEvent() {
//    MobWanderTask mobWanderTask = new MobWanderTask(Vector2Utils.ONE, 1f);
//
//    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(mobWanderTask);
//    Entity entity = new Entity().addComponent(aiTaskComponent).addComponent(new PhysicsMovementComponent());
//    entity.create();
//
//    // Register callbacks
//    EventListener0 callback = mock(EventListener0.class);
//    entity.getEvents().addListener("wanderStart", callback);
//
//    mobWanderTask.start();
//
//    verify(callback).handle();
//  }
}