package com.csse3200.game.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class RicochetComponentTest {
  Entity projectile;
  Entity mob;

  @BeforeEach
  void beforeEach() {
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getDeltaTime()).thenReturn(0.03f);
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerPhysicsService(new PhysicsService());
    ServiceLocator.registerEntityService(new EntityService());
    RenderService render = new RenderService();
    ServiceLocator.registerRenderService(render);
    ResourceService resourceService = new ResourceService();

    projectile = createProjectile(PhysicsLayer.NPC);
    mob = createMobTarget(PhysicsLayer.NPC);
    ServiceLocator.getEntityService().register(projectile);
    ServiceLocator.getEntityService().register(mob);
    ServiceLocator.registerResourceService(resourceService);
    resourceService.loadAll();
  }

  @Test
  void shouldHaveRicochetComponent() {
    assertNotNull(projectile.getComponent(RicochetComponent.class),
        "Projectile does not contain RicochetComponent");
  }

  @Test
  void shouldDisposeAferCollision() {
    projectile.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());

    assertTrue("projectile entity flag should be true after collision",
        projectile.getFlagForDelete());
  }

  // @Test
  @Ignore
  void shouldSpawnAnotherProjectile() {
    int currentEntities = ServiceLocator.getEntityService().getEntities().size;

    // projectile.setPosition(2, 2);
    projectile.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("Should spawn another ricochet projectile", currentEntities,
        ServiceLocator.getEntityService().getEntities().size);
  }

  Entity createProjectile(short targetLayer) {
    Entity projectile = new Entity();

    projectile
        .addComponent(new PhysicsComponent())
        .addComponent(new PhysicsMovementComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PROJECTILE))
        .addComponent(new CombatStatsComponent(0, 10))
        .addComponent(new TouchAttackComponent(targetLayer, 0f, true))
        .addComponent(new RicochetComponent(PhysicsLayer.NPC, 0));

    return projectile;
  }

  Entity createMobTarget(short layer) {
    Entity target = new Entity();

    target
        .addComponent(new CombatStatsComponent(100, 0))
        .addComponent(new PhysicsComponent())
        .addComponent(new HitboxComponent().setLayer(layer));

    return target;
  }
}
