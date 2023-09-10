package com.csse3200.game.components;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
public class RicochetComponentTest {
  Entity projectile;
  Entity mob;

  private final String[] atlas = {
      "images/projectiles/mobProjectile.atlas",
      "images/projectiles/basic_projectile.atlas",
      "images/projectiles/mobKing_projectile.atlas",
      "images/projectiles/engineer_projectile.atlas"
  };

  @BeforeEach
  public void setUp() {
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getDeltaTime()).thenReturn(0.02f);
    ServiceLocator.registerTimeSource(gameTime);
    ServiceLocator.registerPhysicsService(new PhysicsService());
    RenderService render = new RenderService();
    render.setDebug(mock(DebugRenderer.class));
    ServiceLocator.registerRenderService(render);
    ResourceService resourceService = new ResourceService();
    ServiceLocator.registerResourceService(resourceService);
    resourceService.loadTextureAtlases(atlas);
    resourceService.loadAll();
    ServiceLocator.registerEntityService(new EntityService());

    // For the time being, NPC is treated as an enemy.
    projectile = createProjectile(PhysicsLayer.NPC);
    mob = createMobTarget(PhysicsLayer.NPC);
    ServiceLocator.getEntityService().register(projectile);
    ServiceLocator.getEntityService().register(mob);
  }

  @Test
  public void shouldHaveRicochetComponent() {
    assertNotNull(projectile.getComponent(RicochetComponent.class),
        "Projectile does not contain RicochetComponent");
  }

  @Test
  public void shouldDisposeAferCollision() {
    int currentEntities = ServiceLocator.getEntityService().getEntities().size;

    projectile.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());

    assertTrue("projectile entity flag should be true after collision",
        projectile.getFlagForDelete());

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("Projectile should be deleted after collision upon update", currentEntities - 1,
        ServiceLocator.getEntityService().getEntities().size);
  }

  // @Ignore
  @Test
  public void shouldSpawnAnotherProjectileWithinMapBounds() {
    projectile.setPosition(3, 3);
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

  @Test
  public void shouldNotSpawnAnotherProjectileOutOfMapBounds() {
    projectile.setPosition(-1, -1);
    int currentEntities = ServiceLocator.getEntityService().getEntities().size;

    projectile.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertNotEquals(currentEntities,
        ServiceLocator.getEntityService().getEntities().size,
        "Should not have spawned another projectile upon collision");
  }

  @Test
  public void testWithinRangeSpawnedProjectile() {
    projectile.setPosition(3, 3);
    mob.setPosition(3, 3);
    projectile.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    // For the time being, 2f seems to be the justifiable range
    // for the new projectile to be spawned.
    assertEquals("Projectile should be spawned within the range provided.", 1,
        ServiceLocator.getEntityService().getNearbyEntities(mob, 2f).size);
  }

  @Test
  public void testNotWithinRangeShouldNotSpawnProjectile() {
    projectile.setPosition(3, 3);
    mob.setPosition(3, 3);
    projectile.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("Projectile should not be spawned too close to the original (now disposed) projectile and mob", 0,
        ServiceLocator.getEntityService().getNearbyEntities(mob, 0.5f).size);
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
