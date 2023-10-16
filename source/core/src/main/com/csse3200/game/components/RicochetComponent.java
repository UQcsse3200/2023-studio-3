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
 * Ricochet based on target layers.
 * A bouncing effect that continues to bounce off desired entities.
 */
public class RicochetComponent extends Component {
  private short targetLayer;
  private HitboxComponent hitBoxComponent;
  private int bounceCount;
  private static final int MAX_BOUNCE_Y_DIRECTION = 250;
  private static final int MIN_BOUNCE_Y_DIRECTION = -250;

  /**
   * Initialise a RicochetComponent that spawns another projectile upon collision.
   * Projectile has a chance to head upwards or downwards and upon spawning,
   * it will be slighlty up or down in respect to original disappearance.
   * @param targetLayer Target layer upon collision
   * @param bounceCount Keeps track of the bounce count upon initial collision
   *  Stops self-spawning when bounce count is greater or equal than two.
   */
  public RicochetComponent(short targetLayer, int bounceCount) {
    this.targetLayer = targetLayer;
    this.bounceCount = bounceCount;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
    hitBoxComponent = entity.getComponent(HitboxComponent.class);
  }

  /**
   * After collision ends, make another fireball that spawns just before the
   * original one. This assumes
   * that the original fireball is already deleted. Set TouchAttackComponent
   * disposeOnHit to true.
   * 
   * @param me
   * @param other
   */
  private void onCollisionEnd(Fixture me, Fixture other) {
    if (hitBoxComponent.getFixture() != me
        || !PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)
        || bounceCount >= 3) // BounceCount base case of 3
      return;

    Entity projectile = ((BodyUserData) me.getBody().getUserData()).entity;

    // Projectile heads upwards or downwards.
    int randomDirection = getRandomNumFrom(MIN_BOUNCE_Y_DIRECTION, MAX_BOUNCE_Y_DIRECTION);

    // Spawning of the projectile to be above (+ve) or below (-ve) upon
    // collision
    int upOrDown = randomDirection <= 0 ? -1 : 1;

    float newXPosition = (float) (projectile.getPosition().x - 0.75);
    float newYPosition = (float) (projectile.getPosition().y + (0.65 * upOrDown));

    // Prevent spawn of new projectile if it goes out of boundaries.
    if (newYPosition >= 8 || newYPosition <= 1 || newXPosition >= 17 || newXPosition <= 1)
      return;

    // * RIGHT NOW TARGET IS NPC, SUBJECT TO CHANGE
    Entity newProjectile = ProjectileFactory.createRicochetFireball(PhysicsLayer.NPC,
        new Vector2(100, projectile.getPosition().y + randomDirection), new Vector2(2f, 2f), ++bounceCount); // Increment bounceCount

    newProjectile.setPosition(newXPosition, newYPosition);
    newProjectile.setScale(0.75f, 0.75f);

    ServiceLocator.getEntityService().register(newProjectile);
  }

  private int getRandomNumFrom(int min, int max) {
    return (int) (Math.random() * (max - min) + min);
  }
}
