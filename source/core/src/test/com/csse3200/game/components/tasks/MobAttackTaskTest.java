package com.csse3200.game.components.tasks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class MobAttackTaskTest {
  @Mock
  GameTime gameTime;

//  @BeforeEach
//  void beforeEach() {
//    ServiceLocator.registerTimeSource(gameTime);
//    // To Do
//  }
//
//  @Test
//  void shouldShootProjectile() {
//    Entity target = new Entity();
//    target.setPosition(2f, 2f);
//
//    AITaskComponent ai = new AITaskComponent().addTask(new MobAttackTask(target, 10, 5, 10));
//    Entity entity = makePhysicsEntity().addComponent(ai);
//    entity.create();
//    entity.setPosition(0f, 0f);
//
//    // To Do
//  }
}
