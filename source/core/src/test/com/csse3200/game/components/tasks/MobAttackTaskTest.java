package com.csse3200.game.components.tasks;

import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.services.GameTime;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class MobAttackTaskTest {
  @Mock
  GameTime gameTime;

  //TODO: Add some tests to this class.

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
