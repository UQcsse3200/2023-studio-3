package com.csse3200.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.PhysicsUtils;
import com.csse3200.game.physics.components.ColliderComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.rendering.TextureRenderComponent;

/**
 * Factory to create obstacle entities.
 *
 * <p>Each obstacle entity type should have a creation method that returns a corresponding entity.
 */
public class ObstacleFactory {

  /**
   * Creates a tree entity.
   * @return entity
   */
  public static Entity createBuilding1() {
    Entity building1 =
        new Entity()
            .addComponent(new TextureRenderComponent("images/background/building1.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    building1.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    building1.getComponent(TextureRenderComponent.class).scaleEntity();
    building1.scaleHeight(1.5f);
    PhysicsUtils.setScaledCollider(building1, 0.5f, 0.2f);
    return building1;
  }
  public static Entity createBuilding2() {
    Entity building2 =
            new Entity()
                    .addComponent(new TextureRenderComponent("images/background/building2.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

    building2.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
    building2.getComponent(TextureRenderComponent.class).scaleEntity();
    building2.scaleHeight(1.5f);
    PhysicsUtils.setScaledCollider(building2, 0.5f, 0.2f);
    return building2;
  }

  public static Entity createMountain() {
      Entity mountain =
              new Entity()
                      .addComponent(new TextureRenderComponent("images/background/mountain.png"))
                      .addComponent(new PhysicsComponent())
                      .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

      mountain.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
      mountain.getComponent(TextureRenderComponent.class).scaleEntity();
      mountain.scaleHeight(2.0f);
      PhysicsUtils.setScaledCollider(mountain, 0.6f, 0.4f);
      return mountain;
    }

  /**
   * Creates an invisible physics wall.
   * @param width Wall width in world units
   * @param height Wall height in world units
   * @return Wall entity of given width and height
   */
  public static Entity createWall(float width, float height) {
    Entity wall = new Entity()
        .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
        // * TMEPORARRYY WALLL
        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.WALL));
    wall.setScale(width, height);
    return wall;
  }

  private ObstacleFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}