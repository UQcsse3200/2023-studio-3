package com.csse3200.game.components.tasks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.utils.math.Vector2Utils;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class MobAttackTaskTest {
  /**
   * Class for testing MobAttackTask
   */
  GameTime gameTime;
  PhysicsEngine physics;

  @BeforeEach
  void setUp() {
    ServiceLocator.registerTimeSource(gameTime);
    RenderService renderService = new RenderService();
    renderService.setDebug(mock(DebugRenderer.class));
    ServiceLocator.registerRenderService(renderService);
    GameTime gameTime = mock(GameTime.class);
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerPhysicsService(new PhysicsService());
  }

  @Test
  void shouldStartAttack() {
    MobAttackTask mobAttackTask = new MobAttackTask(2, 40);

    AITaskComponent aiTaskComponent = new AITaskComponent().addTask(mobAttackTask);
    Entity entity = new Entity().addComponent(aiTaskComponent);
    entity.create();

    // Register callbacks
//    EventListener0 callback = mock(EventListener0.class);
//    entity.getEvents().addListener("shootStart", callback);

    mobAttackTask.start();

//    verify(callback).handle();
  }

  @Test
  void shouldShootProjectile() {

  }

  @Test
  void shouldMeleeAttack() {

  }
  @Test
  void shouldStopAttack() {

  }
}
