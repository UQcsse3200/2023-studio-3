package com.csse3200.game.components;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.DodgingComponent;
import com.csse3200.game.components.tasks.MobDodgeTask;
import com.csse3200.game.components.tasks.MobWanderTask;
import com.csse3200.game.components.tasks.MobTask.MobTask;
import com.csse3200.game.components.tasks.MobTask.MobType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.events.listeners.EventListener1;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
class DodgingComponentTest {
  Entity baseMob, baseProjectile;
  private static final float VALID_POSITION_Y = 4;
  private static final float VALID_POSITION_X = 7;
  private static final float DEFAULT_RANGE_DETECTION = 1f;
  MobTask task;

  @BeforeEach
  public void setUp() {
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getDeltaTime()).thenReturn(0.02f);
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerPhysicsService(new PhysicsService());

    ServiceLocator.registerEntityService(new EntityService());

    RenderService render = new RenderService();
    render.setDebug(mock(DebugRenderer.class));
    ServiceLocator.registerRenderService(render);

    ResourceService resourceService = new ResourceService();
    ServiceLocator.registerResourceService(resourceService);

    baseMob = createDodgeMob(VALID_POSITION_X,
        VALID_POSITION_Y,
        DEFAULT_RANGE_DETECTION,
        1.75f);
    // task = new MobDodgeTask(new Vector2(2f, 2f), 2f, 5);
    // task = new MobDodgeTask(MobType.DRAGON_KNIGHT, 5);
    task = new MobTask(MobType.DRAGON_KNIGHT, true);
  }

  @Test
  void shouldNotBeNullComponent() {
    assertNotNull("Dodging combat component should not be null",
        baseMob.getComponent(DodgingComponent.class));
  }

  @Test
  void shouldNotBeNullTask() {
    assertNotNull("Mob dodging tasks should not be null",
        baseMob.getComponent(AITaskComponent.class));
  }

  @Test
  void shouldInvokeDodgeEvent() {
    EventListener1<Vector2> dodgeProj = mock(EventListener1.class);
    baseMob.getComponent(AITaskComponent.class).addTask(task);
    baseMob.getEvents().addListener(DodgingComponent.DODGE_EVENT, dodgeProj);
    Vector2 mobPos = baseMob.getCenterPosition();
    task.start();
    Entity projectile = createProjectile(VALID_POSITION_X -
        (DEFAULT_RANGE_DETECTION / 2),
        VALID_POSITION_Y);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();
    task.update();

    verify(dodgeProj).handle(mobPos);
  }

  Entity createDodgeMob(float posX, float posY) {
    Entity mob = NPCFactory.createRangedBaseNPC();
    mob.addComponent(new CombatStatsComponent(10, 10));
    mob.addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE));

    ServiceLocator.getEntityService().register(mob);
    mob.setPosition(posX, posY);
    return mob;
  }

  Entity createDodgeMob(float posX, float posY, float rangeDetection,
      float dodgeSpeed) {
    Entity mob = NPCFactory.createRangedBaseNPC();
    mob.addComponent(new CombatStatsComponent(10, 10));
    mob.addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE, rangeDetection, dodgeSpeed));

    ServiceLocator.getEntityService().register(mob);
    mob.setPosition(posX, posY);

    return mob;
  }

  Entity createProjectile(float posX, float posY) {
    Entity projectile = ProjectileFactory.createBaseProjectile(baseMob
        .getComponent(ColliderComponent.class).getLayer(),
        new Vector2(100, VALID_POSITION_Y), new Vector2(2f, 2f));

    projectile.addComponent(new CombatStatsComponent(10, 10));
    ServiceLocator.getEntityService().register(projectile);
    projectile.setPosition(posX, posY);

    return projectile;
  }
}
