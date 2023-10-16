package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * A component that splits the projectile into multiple mini projectiles.
 * Assumes projectile has a disposesOnHit functionality.
 */
public class SplitFireworksComponent extends Component {
  private short targetLayer;
  private HitboxComponent hitboxComponent;
  private int amount;
  private static final int TOTAL_RANGE = 450;
  private static final double SPAWN_OFFSET_X = 1.75;

  /**
   * Initialises a component that splits the projectile into multiple fireballs
   * upon collision on a specified target layer.
   * The spawned projectiles will be spawned just before original projectile
   * and spread out in multiple direction set by a certain range.
   * Assumes amount of split projectiles is greater or equal than 2.
   * 
   * @param targetLayer Target layer upon collision.
   * @param amount      Amount of projectiles that is split after collision event.
   */
  public SplitFireworksComponent(short targetLayer, int amount) {
    this.targetLayer = targetLayer;
    this.amount = amount;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
  }

  private void onCollisionEnd(Fixture me, Fixture other) {
    if (hitboxComponent.getFixture() != me
        || !PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)
        || amount < 2) // Amount of split projectiles must be >= 2
      return;

    Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;

    for (int i = 0; i < amount; i++) {
      int newDirection = (i * TOTAL_RANGE) / (amount - 1);

      // Boundaries
      float newXPosition = (float) (projectile.getPosition().x + SPAWN_OFFSET_X);
      if (newXPosition >= 18 || newXPosition <= 1)
        return;

      // * RIGHT NOW TARGET IS NPC, SUBJECT TO CHANGE
      // Speed is a bit faster than normal but can change.
      Entity newProjectile = ProjectileFactory.createFireworks(PhysicsLayer.NPC,
          new Vector2(100, (float) (projectile.getPosition().y + (newDirection - ((double) TOTAL_RANGE) / 2))), new Vector2(3f, 3f));

      newProjectile.setPosition(newXPosition, projectile.getPosition().y);

      newProjectile.setScale(0.5f, 0.5f);

      ServiceLocator.getEntityService().register(newProjectile);
    }
  }
}
