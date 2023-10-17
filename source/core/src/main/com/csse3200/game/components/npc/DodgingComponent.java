package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.ServiceLocator;

import java.util.Random;

/**
 * A component that adds a dodging event listener to the current attached
 * entity. The entity will dodge another entity that is presumably coming at its
 * current position. It will dodge at a certain angle in the vertical direction
 * with an altered speed.
 * <p>
 * This component handles the detection of a certain mob layer within a certain
 * horizontal range using a raycast. It also handles the vertical speed doding
 * configuration.
 * </p>
 * 
 * <p>
 * Created for the mob entity to dodge incoming projectiles but can be used for
 * other entities and functionalities if needed.
 * </p>
 */
public class DodgingComponent extends Component {
  private final RaycastHit hit = new RaycastHit();
  private short targetLayer;
  private float rangeDetection;
  private float dodgeSpeed = 1.75f;
  private float originalSpeed; // Original entity vertical speed
  private PhysicsEngine physics;

  // Sometimes the raycast mechanic doesn't detect the other entity because of the
  // target's (or self) collider size does not match. This value makes sure the
  // top and bottom detection is also taken care of, ensuring the entity will
  // dodge.
  private static final float Y_OFFSET_MOB_DETECTION = 0.35f;
  public static final String DODGE_EVENT = "dodgeIncomingEntity";

  /**
   * Initialises a component that dodges an incoming entity based on its target
   * layer. The range detection is defaultto 0.25f.
   * 
   * @param targetLayer The target layer to be detected horizontally.
   */
  public DodgingComponent(short targetLayer) {
    this.targetLayer = targetLayer;
    this.rangeDetection = 0.25f;
  }

  /**
   * Initialises a component that dodges an incoming entity based on its target
   * layer. This initialisation also sets the range detection whereby the dodge
   * event is triggered.
   * 
   * @param targetLayer    The target layer to be detected horizontally.
   * @param rangeDetection The range where the entity of the target layer is
   *                       detected, activating the dodge event.
   */
  public DodgingComponent(short targetLayer, float rangeDetection) {
    this.targetLayer = targetLayer;
    this.rangeDetection = rangeDetection;
  }

  public DodgingComponent(short targetLayer, float rangeDetection, float dodgeSpeed) {
    this.targetLayer = targetLayer;
    this.rangeDetection = rangeDetection;
    this.dodgeSpeed = dodgeSpeed;
  }

  /**
   * Called when the entity is created and registered.
   * 
   * Entity created will have the dodge entity event. Also registers the original
   * speed of the current entity.
   */
  @Override
  public void create() {
    physics = ServiceLocator.getPhysicsService().getPhysics();
    entity.getEvents().addListener(DODGE_EVENT, this::changeTraverseDirection);
    originalSpeed = entity.getComponent(PhysicsMovementComponent.class).getSpeed().y;
  }

  /**
   * Changes the moving direction of the attached entity with this component.
   * <p>
   * Relies heavily on the isTargetVisible() method, and setting the vertical
   * angle direction and speed helper methods found in this component class.
   * </p>
   * 
   * @param mobPos The current Vector2 mob position in the map.
   */
  public void changeTraverseDirection(Vector2 mobPos) {
    int randDirection = MathUtils.random(0,2) == 1 ? -1 : 1;

    if (isTargetVisible(mobPos)) {
      // If mob is in the top half quadrant of the map grid, make the entity dodge
      // downwards.
      // setVerticalAngleDirection(mobPos.y > 3.5 ? mobPos.y - 15 : mobPos.y + 15);
      // Random direction
      setVerticalAngleDirection(mobPos.y + (15 * randDirection));
      setVerticalSpeed(dodgeSpeed);
    } else {
      setVerticalAngleDirection(mobPos.y);
      setVerticalSpeed(originalSpeed);
    }
  }

  /**
   * Detects if the a target is visible based on the range detection, current
   * Vector2 position of the mob and the target layer initialised with this class.
   * 
   * @param mobPos The current Vector2 position of the mob
   * @return True if a target is visible, false otherwise.
   */
  public boolean isTargetVisible(Vector2 mobPos) {
    Vector2 maxRange = new Vector2(mobPos.x - rangeDetection, mobPos.y);
    // check also the upper and lower boundaries of the mob with the offset y mob
    // detection.
    Vector2 upperYMaxRangePos = new Vector2(mobPos.x - 3, mobPos.y - Y_OFFSET_MOB_DETECTION);
    Vector2 lowerYMaxRangePos = new Vector2(mobPos.x - 3, mobPos.y + Y_OFFSET_MOB_DETECTION);

    // Raycast the upper, middle and lower detection range.
    return physics.raycast(mobPos, maxRange, targetLayer, hit)
        || physics.raycast(mobPos, upperYMaxRangePos, targetLayer, hit)
        || physics.raycast(mobPos, lowerYMaxRangePos, targetLayer, hit);
  }

  /**
   * Sets vertical direction of the moving entity based on a float value. This
   * method alters the setaTarget method in the PhysicsMovementComponent of the
   * entity.
   * 
   * @param y A float value that alters the moving direction of the entity.
   */
  private void setVerticalAngleDirection(float y) {
    entity.getComponent(PhysicsMovementComponent.class).setTarget(new Vector2(0, y));
  }

  /**
   * Sets the vertical speed of the entity based on a float value. This method
   * alters the setSpeed method in the PhysicsMovementComponent of the entity.
   * 
   * @param y Float value that alters the entity's vertical movement speed.
   */
  private void setVerticalSpeed(float y) {
    entity.getComponent(PhysicsMovementComponent.class).setSpeed(new Vector2(1f, y));
  }
}
