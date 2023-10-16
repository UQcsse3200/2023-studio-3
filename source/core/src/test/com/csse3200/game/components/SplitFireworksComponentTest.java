package com.csse3200.game.components;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
class SplitFireworksComponentTest {
  Entity projectile;
  Entity mob;
  static double OFFSET_X = 1.75;

  private final String[] atlas = {
      "images/projectiles/mobProjectile.atlas",
      "images/projectiles/basic_projectile.atlas",
      "images/projectiles/mobBoss_projectile.atlas",
      "images/projectiles/engineer_projectile.atlas",
      "images/projectiles/firework_anim.atlas"
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
    projectile = createSplitFireworkProjectile(PhysicsLayer.NPC, 3);
    mob = createMobTarget(PhysicsLayer.NPC);
    ServiceLocator.getEntityService().register(projectile);
    ServiceLocator.getEntityService().register(mob);
  }

  @Test
  void shouldNotBeNull() {
    assertNotNull(projectile, "Ricochet projectile does not exist");
  }

  @Test
  void shouldHaveSplitFireworksComponent() {
    assertNotNull(projectile.getComponent(SplitFireworksComponent.class),
        "Projectile does not contain SplitFireworksComponent");
  }

  @Test
  void shouldDisposeAferCollision() {
    triggerCollisionEnd(projectile, mob);

    assertTrue("original projectile entity flag should be true after collision",
        projectile.getFlagForDelete());
  }

  @Test
  void shouldSpawnCorrectNumberOfProjs() {
    projectile.setPosition(3, 3);

    int initialNumEntities = ServiceLocator.getEntityService().getEntities().size;

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    // initialNumEntities + 2 to account for the dispose of the original projectile.
    assertEquals("Should spawn correct number of projectiles after collision based on amount given",
        initialNumEntities + 2, ServiceLocator.getEntityService().getEntities().size);
  }

  @Test
  void shouldSpawnMultProjWithinMapBounds() {
    projectile.setPosition(3, 3);
    mob.setPosition(3, 3);

    int initialNumEntities = ServiceLocator.getEntityService().getEntities().size;

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertTrue("SplitFireWorks projectile should spawn multiple projectile out of map bounds",
        ServiceLocator.getEntityService().getEntities().size > initialNumEntities);
  }

  @Test
  void shouldNotSpawnMultProjOutOfMapBounds() {
    projectile.setPosition(22, 22);
    mob.setPosition(22, 22);

    int initialNumEntities = ServiceLocator.getEntityService().getEntities().size;

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertFalse(ServiceLocator.getEntityService().getEntities().size > initialNumEntities,
        "SplitFireWorks projectile should not spawn multiple projectile out of map bounds");
  }

  @Test
  void testWithinRangeSpawnedProjectiles() {
    projectile.setPosition(3, 3);
    mob.setPosition(3, 3);

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("Projectiles should be spawned within the range provided.", 3,
        ServiceLocator.getEntityService().getNearbyEntities(mob, 2f).size);
  }

  @Test
  void testTooCloseRangeSpawnedProjectiles() {
    projectile.setPosition(3, 3);
    mob.setPosition(3, 3);

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertNotEquals(3,
        ServiceLocator.getEntityService().getNearbyEntities(mob, 0.5f).size,
        "Projectiles should not be spawned too close upon impact.");
  }

  @Test
  void shouldSpawnAtSpecifiedLocation() {
    projectile.setPosition(3, 3);
    mob.setPosition(3, 3);
    float currPosition = projectile.getPosition().x;

    triggerCollisionEnd(projectile, mob);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    float newXPosition = (float) (currPosition + OFFSET_X);

    Array<Entity> allEntities = ServiceLocator.getEntityService().getEntities();

    for (Entity entity : allEntities) {
      if (entity == mob)
        continue;

      assertEquals("Projectiles were not spawned at the right offset x placement", newXPosition, entity.getPosition().x,
          0.02);
    }
  }

  Entity createSplitFireworkProjectile(short targetLayer, int amount) {
    Entity projectile = ProjectileFactory.createSplitFireWorksFireball(targetLayer, new Vector2(100, 3),
        new Vector2(2f, 2f), amount);

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
