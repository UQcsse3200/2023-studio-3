package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;

/**
 * This specific entity will self-destruct after the collisionEnd
 * event if the targetLayer matches.
 * Possible extensions to take in an array of entities instead.
 */
public class SelfDestructOnHitComponent extends Component {
  private short targetLayer;
  private HitboxComponent hitboxComponent;

  /**
   * Create a component that self-destructs based on a specified targetLayer.
   * @param targetLayer Physic's layer of the target collider.
   */
  public SelfDestructOnHitComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
  }

  private void onCollisionEnd(Fixture me, Fixture other) {
    if (hitboxComponent.getFixture() != me)
      return;

    if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits))
      return;

    Entity selfEntity = ((BodyUserData) me.getBody().getUserData()).entity;

    selfEntity.setFlagForDelete(true);
  }

}
