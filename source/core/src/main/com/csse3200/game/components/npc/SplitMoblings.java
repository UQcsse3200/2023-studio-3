package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.tasks.MobTask.MobType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.services.ServiceLocator;

/**
 * A component that splits the target mob entity into multiple entities after
 * after the mob dies. This class adds a method to the existing event listener
 * "dieStart".
 * <p>
 * Amount of moblings spawned must be provided in the construcor.
 * Scaled size of the moblings can be altered in the X and Y direction if
 * desired. If not provided scaling alteration is assumed to be 0.75.
 * </p>
 */
public class SplitMoblings extends Component {
  private int amount;
  private MobType mobType;
  private int baseMoblingHealth = 60;
  private float scaleX;
  private float scaleY;
  public static final float DEFAULT_MINIFIED_SCALE = 0.75f;
  public static final double OFFSET_DISTANCE = 1.5;
  public static final int FULL_CIRCLE_ANGLE = 360;
  public static final float MIN_X_BOUNDS = 1;
  public static final float MAX_X_BOUNDS = (float) 18.5;
  public static final float MIN_Y_BOUNDS = 0;
  public static final float MAX_Y_BOUNDS = 5;
  public static final String DIE_START_EVENT = "splitDeath";

  /**
   * Initialises a component that splits mob into multiple moblings. Amount of
   * moblings split based on the amount provided param.
   * 
   * @param mobType Type of moblings split on death based on the MobType enum.
   * @param amount  Amount of moblings to be split.
   * @require amount > 0
   */
  public SplitMoblings(MobType mobType, int amount) {
    this.mobType = mobType;
    this.amount = amount;
    scaleX = scaleY = DEFAULT_MINIFIED_SCALE;
  }

  /**
   * Initialises a component that splits mob into multiple moblings. Amount of
   * moblings split is based on the amount provided param.
   * The overalling scaling (x and y) is also altered in the param.
   * 
   * @param mobType Type of moblings split on death based on the MobType enum.
   * @param amount  Amount of moblings to be split.
   * @param scale   X and Y scaling of the moblings in respect to the original
   *                size
   *                of the mobs.
   * @require amount > 0
   */
  public SplitMoblings(MobType mobType, int amount, float scale) {
    this.mobType = mobType;
    this.amount = amount;
    this.scaleX = this.scaleY = scale;
  }

  /**
   * Initialises a component that splits mob into multiple moblings. Amount of
   * moblings split is based on the amount provided param.
   * The individual scaling (x and y) is also altered in the param.
   * 
   * @param mobType Type of moblings split on death based on the MobType enum.
   * @param amount  Amount of moblings to be split.
   * @param scaleX  X scaling of the moblings compared to original size.
   * @param scaleY  Y scaling of the moblings compared to original size.
   * @require amount > 0
   */
  public SplitMoblings(MobType mobType, int amount, float scaleX, float scaleY) {
    this.mobType = mobType;
    this.amount = amount;
    this.scaleX = scaleX;
    this.scaleY = scaleY;
  }

  @Override
  public void create() {
    entity.getEvents().addListener(DIE_START_EVENT, this::onDeath);
  }

  /**
   * Splits into multiple xeno grunts after death
   */
  private void onDeath() {
    float initialScaleX = entity.getScale().x;
    float initialScaleY = entity.getScale().y;

    // If there's only one amount to be spawned, spawn it 1 x-coordinate to the
    // left.
    if (amount == 1) {
      float newXPosition = (float) (entity.getPosition().x - OFFSET_DISTANCE);
      float newYPosition = (entity.getPosition().y);

      if (withinBounds(newXPosition, newYPosition)) {
        spawnAdditionalMob(newXPosition, newYPosition, initialScaleX, initialScaleY);
      }
    }

    // Inspired by:
    // https://stackoverflow.com/questions/37145768/distribute-points-evenly-on-circle-circumference-in-quadrants-i-and-iv-only
    for (int i = 0; i < amount; i++) {
      float currAngle = FULL_CIRCLE_ANGLE / (float) amount * i;
      double radians = currAngle * Math.PI / 180;

      float newX = entity.getPosition().x + (float) OFFSET_DISTANCE *
          (float) Math.cos(radians);
      float newY = entity.getPosition().y + (float) OFFSET_DISTANCE *
          (float) Math.sin(radians);

      if (withinBounds(newX, newY)) {
        spawnAdditionalMob(newX, newY, initialScaleX, initialScaleY);
      }
    }
  }

  /**
   * Helper function that spawns a xeno grunt based on a x and y-coordinate and
   * scales down/up the entity based on the initial scale and this object's
   * scale.
   * 
   * @param positionX     New spawn x-coordinate
   * @param positionY     New spawn y-coordinate
   * @param initialScaleX Initial horizontal scale of the entity
   * @param initialScaleY Initial vertical scale of the entity
   */
  public void spawnAdditionalMob(float positionX, float positionY,
      float initialScaleX, float initialScaleY) {
    Entity entityType;
    switch (mobType) {
      case WATER_SLIME ->
        entityType = NPCFactory.createBaseWaterSlime(baseMoblingHealth);

      case NIGHT_BORNE ->
        entityType = NPCFactory.createNightBorne(baseMoblingHealth);

      case ROCKY ->
        entityType = NPCFactory.createRocky(baseMoblingHealth);

      default ->
        entityType = NPCFactory.createBaseWaterSlime(baseMoblingHealth);
    }

    entityType.setPosition(positionX, positionY);

    switch (mobType) {
      case NIGHT_BORNE ->
        entityType.setScale(initialScaleX, initialScaleY);

      default ->
        entityType.setScale(initialScaleX * scaleX, initialScaleY * scaleY);

    }

    ServiceLocator.getEntityService().register(entityType);

    ServiceLocator.getWaveService().setEnemyCount(ServiceLocator.getWaveService().getEnemyCount() + 1);
  }

  /**
   * Helper to check if the current projectile position is within map bounds.
   * Prevents spawning of mobs outof bounds.
   * 
   * @param currX x-coordinate of the gamegrid.
   * @param currY y-coordinate of the gamegrid.
   * @return true if current position is within bounds of the map constraints.
   *         False otherwise.
   */
  private boolean withinBounds(float currX, float currY) {
    return currX >= MIN_X_BOUNDS
        && currX <= MAX_X_BOUNDS
        && currY >= MIN_Y_BOUNDS
        && currY <= MAX_Y_BOUNDS;
  }
}
