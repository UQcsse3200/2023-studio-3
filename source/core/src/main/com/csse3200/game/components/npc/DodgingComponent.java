package com.csse3200.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.ServiceLocator;

public class DodgingComponent extends Component {
  private final RaycastHit hit = new RaycastHit();
  private short targetLayer;
  private float rangeDetection;
  private PhysicsEngine physics;
  private static final float Y_OFFSET_MOB_DETECTION = 0.35f;

  public DodgingComponent(short targetLayer) {
    this.targetLayer = targetLayer;
    this.rangeDetection = 3;
  }

  public DodgingComponent(short targetLayer, float rangeDetection) {
    this.targetLayer = targetLayer;
    this.rangeDetection = rangeDetection;
  }

  // TODO: add speed as a param for the constructor

  @Override
  public void create() {
    physics = ServiceLocator.getPhysicsService().getPhysics();
    entity.getEvents().addListener("dodgeProj", this::changeTraverseDirection);
  }

  public void changeTraverseDirection(Vector2 mobPos) {
    // Vector2 currMobPos = entity.getCenterPosition();
    if (isTargetVisible(mobPos)) {
      if (mobPos.y > 4) {
        setVerticalAngleDirection(mobPos.y - 15);
      }
      setVerticalAngleDirection(mobPos.y > 3.5 ? mobPos.y - 15 : mobPos.y + 15);
      setVerticalSpeed(1.75f);
    } else {
      setVerticalAngleDirection(mobPos.y);
      setVerticalSpeed(1f);
    }
  }

  private boolean isTargetVisible(Vector2 mobPos) {
    Vector2 maxRange = new Vector2(mobPos.x - rangeDetection, mobPos.y);
    // check also the upper and lower boundaries of the mob with the offset y mob
    // detection.
    Vector2 upperYMaxRangePos = new Vector2(mobPos.x - 3, mobPos.y - Y_OFFSET_MOB_DETECTION);
    Vector2 lowerYMaxRangePos = new Vector2(mobPos.x - 3, mobPos.y + Y_OFFSET_MOB_DETECTION);

    return physics.raycast(mobPos, maxRange, targetLayer, hit)
        || physics.raycast(mobPos, upperYMaxRangePos, targetLayer, hit)
        || physics.raycast(mobPos, lowerYMaxRangePos, targetLayer, hit);
  }

  private void setVerticalAngleDirection(float y) {
    entity.getComponent(PhysicsMovementComponent.class).setTarget(new Vector2(0, y));
  }

  private void setVerticalSpeed(float y) {
    entity.getComponent(PhysicsMovementComponent.class).setSpeed(new Vector2(1f, y));
  }
}
