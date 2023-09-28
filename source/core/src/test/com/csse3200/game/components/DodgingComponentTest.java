package com.csse3200.game.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
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
public class DodgingComponentTest {
  Entity baseMob, baseProjectile;
  private static final float VALID_POSITION_Y = 4;
  private static final float VALID_POSITION_X = 7;

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

    baseMob = createDodgeMob(VALID_POSITION_X, VALID_POSITION_Y);
    baseProjectile = createProjectile(VALID_POSITION_X - 0.2f, VALID_POSITION_Y);
  }

  @Test
  public void shouldNotBeNullComponent() {
    assertNotNull("Dodging combat component should not be null", baseMob.getComponent(DodgingComponent.class));
  }

  @Test
  public void shouldNotBeNullTask() {
    assertNotNull("Mob dodging tasks should not be null", baseMob.getComponent(AITaskComponent.class));
  }

  Entity createDodgeMob(float posX, float posY) {
    Entity mob = NPCFactory.createRangedBaseNPC();
    mob.getComponent(AITaskComponent.class).addTask(new MobDodgeTask(new Vector2(2f, 2f), 2f, 5));
    mob.addComponent(new CombatStatsComponent(10, 10));
    mob.addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE));
    mob.setPosition(posX, posY);

    return mob;
  }

  Entity createDodgeMob(float posX, float posY, float rangeDetection, float dodgeSpeed) {
    Entity mob = NPCFactory.createRangedBaseNPC();
    mob.getComponent(AITaskComponent.class).addTask(new MobDodgeTask(new Vector2(2f, 2f), 2f, 5));
    mob.addComponent(new CombatStatsComponent(10, 10));
    mob.addComponent(new DodgingComponent(PhysicsLayer.PROJECTILE, rangeDetection, dodgeSpeed));

    ServiceLocator.getEntityService().register(mob);
    mob.setPosition(posX, posY);

    return mob;
  }

  Entity createProjectile(float posX, float posY) {
    Entity projectile = ProjectileFactory.createBaseProjectile(baseMob.getComponent(ColliderComponent.class).getLayer(),
        new Vector2(100, VALID_POSITION_Y), new Vector2(2f, 2f));
    projectile.addComponent(new CombatStatsComponent(10, 10));
    ServiceLocator.getEntityService().register(projectile);
    projectile.setPosition(posX, posY);

    return projectile;
  }
}
