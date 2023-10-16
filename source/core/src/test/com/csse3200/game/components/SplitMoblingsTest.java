package com.csse3200.game.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.npc.SplitMoblings;
import com.csse3200.game.components.tasks.MobTask.MobType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameEndService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.services.WaveService;

@ExtendWith(GameExtension.class)
class SplitMoblingsTest {
  private static final int BASE_Y_COORD = 3;
  private static final int BASE_AMOUNT = 5;
  private final String[] atlas = {
      "images/mobs/water_slime.atlas",
      "images/mobs/night_borne.atlas"
  };

  Entity baseMob;

  @BeforeEach
  public void setUp() {
    GameTime gameTime = mock(GameTime.class);
    when(gameTime.getDeltaTime()).thenReturn(0.02f);
    ServiceLocator.registerTimeSource(gameTime);

    ServiceLocator.registerPhysicsService(new PhysicsService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerGameEndService(new GameEndService());

    RenderService render = new RenderService();
    render.setDebug(mock(DebugRenderer.class));
    ServiceLocator.registerRenderService(render);

    ResourceService resourceService = new ResourceService();
    ServiceLocator.registerResourceService(resourceService);
    resourceService.loadTextureAtlases(atlas);
    resourceService.loadAll();

    WaveService waveService = mock(WaveService.class);
    ServiceLocator.registerWaveService(waveService);

    GameEndService gameEndService = new GameEndService();
    ServiceLocator.registerGameEndService(gameEndService);

    baseMob = createSplitMob(BASE_AMOUNT);
  }

  @Test
  void shouldNotBeNull() {
    Entity mob = createSplitMob(5);
    Assertions.assertNotNull(mob.getComponent(SplitMoblings.class), "Mobling components does not exists");
  }

  @Test
  void shouldHaveAsset() {
    Entity projectile = createDummyProjectile();

    baseMob.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);
    projectile.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);

    for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
      if (entity.equals(baseMob) || entity.equals(projectile))
        continue;

      Assertions.assertTrue(ServiceLocator.getResourceService().containsAsset(atlas[0], entity.getClass()), "moblings does not contain the right asset");

    }
  }

  @Test
  void shouldBeDisposedAfterDeath() {
    Entity projectile = createDummyProjectile();
    projectile.getComponent(CombatStatsComponent.class).setBaseAttack(20);

    baseMob.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);
    projectile.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);

    triggerCollision(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("mob health should be 0", 0,
        baseMob.getComponent(CombatStatsComponent.class).getHealth());

    assertTrue("Mob should be dead with a health of 0",
        baseMob.getComponent(CombatStatsComponent.class).isDead());

    assertFalse("Mob's deletion flag should be true",
        baseMob.getFlagForDelete());
  }

  @Test
  void shouldInvokeDieStartEventAfterDeath() {
    EventListener0 dieStart = mock(EventListener0.class);
    baseMob.getComponent(CombatStatsComponent.class).setHealth(0);

    // mob is dead
    assertTrue("mob is not dead when health is set to 0",
        baseMob.getComponent(CombatStatsComponent.class).isDead());

    baseMob.getEvents().addListener(SplitMoblings.DIE_START_EVENT, dieStart);
    ServiceLocator.getEntityService().update();

    // Verify dieStart event handler
    verify(dieStart).handle();
  }

  @Test
  void shouldNotInvokeDieStartEventNoDeath() {
    EventListener0 dieStart = mock(EventListener0.class);

    assertFalse("mob is dead when health is not 0",
        baseMob.getComponent(CombatStatsComponent.class).isDead());

    baseMob.getEvents().addListener(SplitMoblings.DIE_START_EVENT, dieStart);

    verifyNoInteractions(dieStart);
  }

  @Test
  void shouldSplitCorrectAmount() {
    Entity projectile = createDummyProjectile();

    int allEntities = ServiceLocator.getEntityService().getEntities().size;

    projectile.getComponent(CombatStatsComponent.class).setBaseAttack(10);
    baseMob.getComponent(CombatStatsComponent.class).setHealth(10);

    // Valid bounds for mob spawn
    baseMob.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);
    projectile.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);

    triggerCollision(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("amount of mobs spawn do not match the correct anount initialised",
        allEntities + BASE_AMOUNT,
        ServiceLocator.getEntityService().getEntities().size);
  }

  @Test
  void shouldNotSplitCorrectAmountOutOfBounds() {
    Entity projectile = createDummyProjectile();

    int allEntities = ServiceLocator.getEntityService().getEntities().size;

    projectile.getComponent(CombatStatsComponent.class).setBaseAttack(10);
    baseMob.getComponent(CombatStatsComponent.class).setHealth(10);

    // Valid bounds, but spawned projectiles should be a lesser amount
    baseMob.setPosition(SplitMoblings.MIN_X_BOUNDS, SplitMoblings.MIN_Y_BOUNDS);
    projectile.setPosition(SplitMoblings.MIN_X_BOUNDS, SplitMoblings.MIN_Y_BOUNDS);

    triggerCollision(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertNotEquals("amount of mobs spawn matches the amount initialised when spawned location is out of bounds",
        allEntities + BASE_AMOUNT,
        ServiceLocator.getEntityService().getEntities().size);
  }

  @Test
  void shouldSpawnWithinRangeAmountOne() {
    Entity mob = createSplitMob(1);
    Entity projectile = createDummyProjectile();

    mob.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);
    projectile.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);

    triggerCollision(mob, projectile);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    // Should spawn with a default offset distance to the left.
    Entity mobling = ServiceLocator.getEntityService().getEntityAtPosition(
        SplitMoblings.MIN_X_BOUNDS + 2 - (float) SplitMoblings.OFFSET_DISTANCE,
        SplitMoblings.MIN_Y_BOUNDS + 2);

    assertNotNull("mobling failed to spawn within range",
        mobling);
  }

  @Test
  void shouldSpawnWithinRangeMultipleAmount() {
    Entity projectile = createDummyProjectile();
    Entity mobThree = createSplitMob(3);
    Entity mobSeven = createSplitMob(7);

    ArrayList<Entity> initialEntities = new ArrayList<Entity>(
        Arrays.asList(mobThree, baseMob, mobSeven, projectile));

    int nearbyEntities = ServiceLocator.getEntityService().getNearbyEntities(projectile,
        (float) SplitMoblings.OFFSET_DISTANCE).size;

    // assertEquals(1, nearbyEntities);

    for (Entity entity : initialEntities) {
      entity.setPosition(
          SplitMoblings.MIN_X_BOUNDS + 5, SplitMoblings.MIN_Y_BOUNDS + 3);
    }

    projectile.getComponent(TouchAttackComponent.class)
        .setDisposeOnHit(false);

    for (Entity entity : initialEntities.subList(0,
        initialEntities.size() - 1)) {
      triggerCollision(entity, projectile);
    }

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("incorrect number of moblings spawned within range",
        nearbyEntities + 3 + 5 + 7,
        ServiceLocator.getEntityService().getNearbyEntities(projectile,

            // 0.2f is delta float consideration
            (float) SplitMoblings.OFFSET_DISTANCE + 0.2f).size);
  }

  @Test
  void shouldScaleBasedOnParamsSingleAmt() {
    float scale = 1.5f;
    Entity mob = createSplitMob(1, scale);
    Entity projectile = createDummyProjectile();

    float initialScaleX = mob.getScale().x;
    float initialScaleY = mob.getScale().y;

    mob.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);
    projectile.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);

    triggerCollision(mob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    Entity mobling = ServiceLocator.getEntityService().getEntityAtPosition(
        SplitMoblings.MIN_X_BOUNDS + 2 - (float) SplitMoblings.OFFSET_DISTANCE,
        SplitMoblings.MIN_Y_BOUNDS + 2);

    assertEquals("Scaling X does not match based on params (1.5f)",
        initialScaleX * scale, mobling.getScale().x, 0.1);
    assertEquals("Scaling Y does not match based on params (1.5f)",
        initialScaleY * scale, mobling.getScale().y, 0.1);
  }

  @Test
  void shouldScaleXAndYbasedOnParamsMultiAmt() {
    float scaleX = 0.5f;
    float scaleY = 1.75f;
    Entity mob = createSplitMob(5, scaleX, scaleY);
    Entity projectile = createDummyProjectile();

    float initialScaleX = mob.getScale().x;
    float initialScaleY = mob.getScale().y;

    mob.setPosition(SplitMoblings.MIN_X_BOUNDS + 3, SplitMoblings.MIN_Y_BOUNDS + 3);
    projectile.setPosition(SplitMoblings.MIN_X_BOUNDS + 3, SplitMoblings.MIN_Y_BOUNDS + 3);

    triggerCollision(mob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    for (Entity mobling : ServiceLocator.getEntityService().getNearbyEntities(mob,
        (float) SplitMoblings.OFFSET_DISTANCE + 0.5f)) {
      if (mobling.equals(projectile))
        continue;

      assertEquals("Scaling X does not match based on params (0.5f)",
          initialScaleX * scaleX,
          mobling.getScale().x,
          0.1);

      assertEquals("Scaling Y does not match based on params (1.75f)",
          initialScaleY * scaleY,
          mobling.getScale().y,
          0.1);
    }
  }

  // For now water slimes will be moblings spawned
  Entity createSplitMob(int amount) {
    Entity mob = NPCFactory.createBaseWaterSlime(10);
    mob.addComponent(new CombatStatsComponent(10, 10));
    mob.addComponent(new SplitMoblings(MobType.WATER_SLIME, amount));
    ServiceLocator.getEntityService().register(mob);
    return mob;
  }

  Entity createSplitMob(int amount, float scale) {
    Entity mob = NPCFactory.createBaseWaterSlime(10);
    mob.addComponent(new SplitMoblings(MobType.WATER_SLIME, amount, scale));
    mob.addComponent(new CombatStatsComponent(10, 10));
    ServiceLocator.getEntityService().register(mob);
    return mob;
  }

  Entity createSplitMob(int amount, float scaleX, float scaleY) {
    Entity mob = NPCFactory.createBaseWaterSlime(10);
    mob.addComponent(new SplitMoblings(MobType.WATER_SLIME, amount, scaleX, scaleY));
    mob.addComponent(new CombatStatsComponent(10, 10));
    ServiceLocator.getEntityService().register(mob);
    return mob;
  }

  Entity createDummyProjectile() {
    Entity projectile = ProjectileFactory.createBaseProjectile(baseMob
        .getComponent(ColliderComponent.class).getLayer(),
        new Vector2(100, BASE_Y_COORD), new Vector2(2f, 2f));

    ServiceLocator.getEntityService().register(projectile);
    return projectile;
  }

  void triggerCollisionEnd(Entity mob, Entity projectile) {
    mob.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());
  }

  void triggerCollisionStart(Entity mob, Entity projectile) {
    mob.getEvents().trigger("collisionStart",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());
  }

  void triggerCollision(Entity mob, Entity projectile) {
    triggerCollisionStart(mob, projectile);
    triggerCollisionEnd(mob, projectile);
  }
}
