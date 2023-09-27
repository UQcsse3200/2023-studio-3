package com.csse3200.game.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.SplitMoblings;
import com.csse3200.game.components.tasks.MobWanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.events.listeners.EventListener0;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
public class SplitMoblingsTest {
  private static final int BASE_Y_COORD = 3;
  private static final int BASE_AMOUNT = 5;
  private final String[] atlas = {
      "images/mobs/xenoGrunt.atlas",
      "images/mobs/water_slime.atlas",
  };

  Entity baseMob;

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
    resourceService.loadTextureAtlases(atlas);
    resourceService.loadAll();

    baseMob = createSplitMob(BASE_AMOUNT);
  }

  @Test
  public void shouldNotBeNull() {
    Entity mob = createSplitMob(5);
    assertNotNull("Mobling components does not exists", mob.getComponent(SplitMoblings.class));
  }

  @Test
  public void shouldBeDisposedAfterDeath() {
    Entity projectile = createDummyProjectile();
    projectile.getComponent(CombatStatsComponent.class).setBaseAttack(20);

    baseMob.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);
    projectile.setPosition(SplitMoblings.MIN_X_BOUNDS + 2, SplitMoblings.MIN_Y_BOUNDS + 2);

    triggerCollision(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("mob health should be 0", 0, baseMob.getComponent(CombatStatsComponent.class).getHealth());

    assertTrue("Mob should be dead with a health of 0", baseMob.getComponent(CombatStatsComponent.class).isDead());

    assertFalse("Mob's deletion flag should be true", baseMob.getFlagForDelete());
  }

  @Test
  public void shouldInvokeDieStartEventAfterDeath() {
    EventListener0 dieStart = mock(EventListener0.class);
    baseMob.getComponent(CombatStatsComponent.class).setHealth(0);

    // mob is dead
    assertTrue("mob is not dead when health is set to 0", baseMob.getComponent(CombatStatsComponent.class).isDead());

    baseMob.getEvents().addListener(SplitMoblings.DIE_START_EVENT, dieStart);
    ServiceLocator.getEntityService().update();

    // Verify dieStart event handler
    verify(dieStart).handle();
  }

  @Test
  public void shouldNotInvokeDieStartEventAfterDeath() {
    EventListener0 dieStart = mock(EventListener0.class);

    assertFalse("mob is dead when health is not 0", baseMob.getComponent(CombatStatsComponent.class).isDead());

    baseMob.getEvents().addListener(SplitMoblings.DIE_START_EVENT, dieStart);

    verifyNoInteractions(dieStart);
  }

  @Test
  public void shouldSplitCorrectAmount() {
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

    assertEquals("amount of mobs spawn do not match the correct anount initialised", allEntities + BASE_AMOUNT,
        ServiceLocator.getEntityService().getEntities().size);
  }

  @Test
  public void shouldNotSplitCorrectAmountOutOfBounds() {
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

  Entity createSplitMob(int amount) {
    Entity mob = NPCFactory.createRangedBaseNPC();
    mob.addComponent(new CombatStatsComponent(10, 10));
    mob.addComponent(new SplitMoblings(amount));
    ServiceLocator.getEntityService().register(mob);
    return mob;
  }

  Entity createSplitMob(int amount, int scale) {
    Entity mob = NPCFactory.createRangedBaseNPC();
    mob.addComponent(new SplitMoblings(amount, scale));
    ServiceLocator.getEntityService().register(mob);
    return mob;
  }

  Entity createSplitMob(int amount, int scaleX, int scaleY) {
    Entity mob = NPCFactory.createRangedBaseNPC();
    mob.addComponent(new SplitMoblings(amount, scaleX, scaleY));
    ServiceLocator.getEntityService().register(mob);
    return mob;
  }

  Entity createDummyProjectile() {
    Entity projectile = ProjectileFactory.createBaseProjectile(baseMob.getComponent(ColliderComponent.class).getLayer(),
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
