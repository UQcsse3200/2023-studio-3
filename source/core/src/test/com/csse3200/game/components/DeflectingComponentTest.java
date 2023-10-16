package com.csse3200.game.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.components.npc.DeflectingComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.events.listeners.EventListener2;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.rendering.DebugRenderer;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
class DeflectingComponentTest {
  Entity baseMob;
  private static final int DEFAULT_ATTACK = 10;
  private static final int DEFAULT_DEFENSE = 10;
  private static final int DEFAULT_DEFLECT_AMOUNT = 5;
  private static final int BASE_Y_COORD = 3;
  private static final float VALID_POSITION_X = 3;
  private static final float VALID_POSITION_Y = 3;

  private final String[] atlas = {
      "images/projectiles/basic_projectile.atlas"
  };

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

    baseMob = createDeflectMob(DEFAULT_DEFLECT_AMOUNT,
        VALID_POSITION_X,
        VALID_POSITION_Y);
  }

  @Test
  void shouldNotBeNull() {
    assertNotNull("Deflecting component does not exist",
        baseMob.getComponent(DeflectingComponent.class));
  }

  @Test
  void shouldNotBeDisposed() {
    Entity projectile = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);

    triggerCollisionStart(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertFalse("disposed flag should be false after collision start",
        projectile.getFlagForDelete());
  }

  @Test
  void shouldBeDisposedWhenDisabled() {
    Entity projectile = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);
    baseMob.getComponent(DeflectingComponent.class).setEnabled(false);

    triggerCollisionStart(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertFalse("collision set disposed flag to true",
        projectile.getFlagForDelete());
  }

  @Test
  void shouldInvokeDeflectProjEvent() {
    EventListener2<Fixture, Fixture> deflectProj = mock(EventListener2.class);
    Entity projectile = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);

    baseMob.getEvents().addListener("collisionStart", deflectProj);
    triggerCollisionStart(baseMob, projectile);

    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    verify(deflectProj).handle(baseMob.getComponent(ColliderComponent.class).getFixture(),
        projectile.getComponent(ColliderComponent.class).getFixture());
  }

  @Test
  void shouldInvokeXAmtTimes() {
    EventListener2<Fixture, Fixture> deflectProj = mock(EventListener2.class);
    Entity mob = createDeflectMob(3, VALID_POSITION_Y, VALID_POSITION_X);
    Entity projectile = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);
    Entity projectile2 = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);
    Entity projectile3 = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);

    projectile.getComponent(TouchAttackComponent.class)
        .setDisposeOnHit(false);

    mob.getEvents().addListener("collisionStart", deflectProj);

    triggerCollisionStart(mob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();
    triggerCollisionStart(mob, projectile2);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();
    triggerCollisionStart(mob, projectile3);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    verify(deflectProj, atLeastOnce()).handle(
        mob.getComponent(ColliderComponent.class).getFixture(),
        projectile.getComponent(ColliderComponent.class).getFixture());

    verify(deflectProj, atLeastOnce()).handle(
        mob.getComponent(ColliderComponent.class).getFixture(),
        projectile2.getComponent(ColliderComponent.class).getFixture());

    verify(deflectProj, atLeastOnce()).handle(
        mob.getComponent(ColliderComponent.class).getFixture(),
        projectile3.getComponent(ColliderComponent.class).getFixture());
  }

  @Test
  void shouldInvokeAtMostOnce() {
    EventListener2<Fixture, Fixture> deflectProj = mock(EventListener2.class);
    Entity mob = createDeflectMob(1, VALID_POSITION_Y, VALID_POSITION_X);
    Entity projectile = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);

    projectile.getComponent(TouchAttackComponent.class)
        .setDisposeOnHit(false);

    mob.getEvents().addListener("collisionStart", deflectProj);
    triggerCollisionStart(mob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();
    triggerCollisionStart(mob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    verify(deflectProj, atMostOnce()).handle(
        mob.getComponent(ColliderComponent.class).getFixture(),
        projectile.getComponent(ColliderComponent.class).getFixture());
  }

  @Test
  void shouldReverseProjScaleX() {
    Entity projectile = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);

    float initialX = projectile.getScale().x;

    triggerCollisionStart(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("image should be reversed horizontally",
        -initialX, projectile.getScale().x, 0.1f);
  }

  @Test
  void shouldRemainSameHealth() {
    Entity projectile = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);
    int health = baseMob.getComponent(CombatStatsComponent.class)
        .getHealth();

    triggerCollisionStart(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertEquals("Should have same health after collision", health,
        baseMob.getComponent(CombatStatsComponent.class).getHealth());
  }

  @Test
  void shouldNotChangeHealthWhenDisabled() {
    Entity projectile = createProjectile(VALID_POSITION_X, VALID_POSITION_Y);
    baseMob.getComponent(CombatStatsComponent.class).setHealth(100);
    int health = baseMob.getComponent(CombatStatsComponent.class).getHealth();
    baseMob.getComponent(DeflectingComponent.class).setEnabled(false);

    triggerCollisionStart(baseMob, projectile);
    ServiceLocator.getPhysicsService().getPhysics().update();
    ServiceLocator.getEntityService().update();

    assertNotEquals("Should not have same health after collision", health,
        baseMob.getComponent(CombatStatsComponent.class).getHealth());
  }

  Entity createDeflectMob(int amount, float posX, float posY) {
    Entity mob = NPCFactory.createRangedBaseNPC();
    mob.addComponent(new DeflectingComponent(PhysicsLayer.PROJECTILE,
        PhysicsLayer.TOWER, amount));
    mob.addComponent(new CombatStatsComponent(DEFAULT_ATTACK, DEFAULT_DEFENSE));

    mob.setPosition(posX, posY);
    ServiceLocator.getEntityService().register(mob);

    return mob;
  }

  Entity createProjectile(float posX, float posY) {
    Entity projectile = ProjectileFactory.createBaseProjectile(
        baseMob.getComponent(ColliderComponent.class).getLayer(),
        new Vector2(100, BASE_Y_COORD), new Vector2(2f, 2f));

    projectile.getComponent(PhysicsMovementComponent.class)
        .setTarget(new Vector2(100, BASE_Y_COORD));

    projectile.setPosition(posX, posY);
    ServiceLocator.getEntityService().register(projectile);
    return projectile;
  }

  void triggerCollisionStart(Entity mob, Entity projectile) {
    mob.getEvents().trigger("collisionStart",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());
  }

  void triggerCollisionEnd(Entity mob, Entity projectile) {
    mob.getEvents().trigger("collisionEnd",
        projectile.getComponent(HitboxComponent.class).getFixture(),
        mob.getComponent(HitboxComponent.class).getFixture());
  }

  void triggerCollision(Entity mob, Entity projectile) {
    triggerCollisionStart(mob, projectile);
    triggerCollisionEnd(mob, projectile);
  }
}
