package com.csse3200.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entities with this component will self destruct after hitting the grid edge
 * upon collision.
 */
public class DeleteOnMapEdgeComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(DeleteOnMapEdgeComponent.class);

  @Override
  public void create() {
    entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
  }

  private void onCollisionEnd(Fixture me, Fixture other) {
    Entity selfEntity = ((BodyUserData) me.getBody().getUserData()).entity;

    // * Should change the PhysicLayer to WALL / BOUNDARIES when established
    if (!PhysicsLayer.contains(PhysicsLayer.WALL, other.getFilterData().categoryBits))
      return;

    Vector2 position = selfEntity.getPosition();
    
    if (position.x <= 1 || position.x >= 18 || position.y < 0 || position.y >= 6.5) {
      logger.debug("DELETION POSITION: " + position);
      selfEntity.setFlagForDelete(true);
    }
  }
}
