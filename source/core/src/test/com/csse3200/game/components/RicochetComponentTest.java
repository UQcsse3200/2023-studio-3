package com.csse3200.game.components;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
class RicochetComponentTest {
  Entity projectile;
  Entity mob;

  private final String[] atlas = {
      "images/projectiles/mobProjectile.atlas",
      "images/projectiles/basic_projectile.atlas",
      "images/projectiles/mobBoss_projectile.atlas",
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
    projectile = createProjectile(PhysicsLayer.NPC, 0);
    mob = createMobTarget(PhysicsLayer.NPC);
    ServiceLocator.getEntityService().register(projectile);
    ServiceLocator.getEntityService().register(mob);
  }

  @Test
  void shouldNotBeNull() {
    assertNotNull(projectile, "Ricochet projectile does not exist");
  }

  @Test
  void shouldHaveRicochetComponent() {
    assertNotNull(projectile.getComponent(RicochetComponent.class),
        "Projectile does not contain RicochetComponent");
  }

  @Test
  void shouldDisposeAferCollision() {
    int currentEntities = ServiceLocator.getEntityService().getEntities().size;

    triggerCollisionEnd(projectile, mob);

    assertTrue("projectile entity flag should be true after collision",
        projectile.getFlagForDelete());

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("Projectile should be deleted after collision upon update", currentEntities - 1,
        ServiceLocator.getEntityService().getEntities().size);
  }

  // @Ignore
  @Test
  void shouldSpawnAnotherProjWithinMapBounds() {
    projectile.setPosition(3, 3);
    int currentEntities = ServiceLocator.getEntityService().getEntities().size;

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("Should spawn another ricochet projectile within map bounds", currentEntities,
        ServiceLocator.getEntityService().getEntities().size);
  }

  @Test
  void shouldNotSpawnAnotherProjOutOfMapBounds() {
    projectile.setPosition(-1, -1);
    int currentEntities = ServiceLocator.getEntityService().getEntities().size;

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertNotEquals(currentEntities,
        ServiceLocator.getEntityService().getEntities().size,
        "Should not have spawned another projectile upon collision");
  }

  @Test
  void testWithinRangeSpawnedProjectile() {
    projectile.setPosition(3, 3);
    mob.setPosition(3, 3);

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    // For the time being, 2f seems to be the justifiable range
    // for the new projectile to be spawned.
    assertEquals("Projectile should be spawned within the range provided.", 1,
        ServiceLocator.getEntityService().getNearbyEntities(mob, 2f).size);
  }

  @Test
  void testNotWithinRangeShouldNotSpawnProjectile() {
    projectile.setPosition(3, 3);
    mob.setPosition(3, 3);
    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("Projectile should not be spawned too close to the original (now disposed) projectile and mob", 0,
        ServiceLocator.getEntityService().getNearbyEntities(mob, 0.5f).size);
  }

  @Test
  void shouldNotSpawnAnotherProjWithMaxBounceCount() {
    Entity newProjectile = createProjectile(PhysicsLayer.NPC, 3);
    ServiceLocator.getEntityService().register(newProjectile);
    int currentEntities = ServiceLocator.getEntityService().getEntities().size;

    newProjectile.setPosition(3, 3);
    mob.setPosition(3, 3);

    triggerCollisionEnd(newProjectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertNotEquals(currentEntities,
        ServiceLocator.getEntityService().getEntities().size,
        "Should not have spawned another projectile upon collision with a max bounce count");
  }

  Entity createProjectile(short targetLayer, int bounceCount) {
    Entity projectile = ProjectileFactory.createRicochetFireball(targetLayer, new Vector2(0.1f, 0.1f),
        new Vector2(2f, 2f), bounceCount);

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

  /**
   * Assumes both entity has hitbox components.
   * 
   * @param projectile
   * @param mob
   */
  void triggerCollisionEnd(Entity projectile, Entity mob) {
    projectile.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());
  }
}
