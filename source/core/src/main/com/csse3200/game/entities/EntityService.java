package com.csse3200.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.npc.DropComponent;
import com.csse3200.game.input.DropInputComponent;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Comparator;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 *
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class EntityService {
  private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
  private static final int INITIAL_CAPACITY = 16;
  private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);
  private static final float MAX_RADIUS = 50f;

  public static void removeEntity(Entity clickedEntity) {
    clickedEntity.dispose();
  }

  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   * @param entity new entity.
   */
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    entities.add(entity);
    entity.create();
  }

  /**
   * Unregister an entity with the entity service. The entity will be removed and stop updating.
   * @param entity entity to be removed.
   */
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    entities.removeValue(entity, true);
  }

  /**
   * Update all registered entities. Should only be called from the main game loop.
   */
  public void update() {
    for (Entity entity : entities) {
      entity.earlyUpdate();
      entity.update();
    }
  }

  /**
   * Dispose all entities.
   */
  public void dispose() {
    for (Entity entity : entities) {
      entity.dispose();
    }
  }

  /**
   * Get all entities
   */
  public Array<Entity> getEntities() {
    return entities;
  }

  /**
   * Get entities within a certain radius of a given entity.
   *
   * @param source The reference entity to check distance from.
   * @param radius The radius within which to fetch entities.
   * @return An array containing entities within the given radius.
   */
  public Array<Entity> getNearbyEntities(Entity source, float radius) {
    Array<Entity> nearbyEntities = new Array<Entity>();
    Array<Entity> allEntities = ServiceLocator.getEntityService().getEntities();
    for (int i = 0; i < allEntities.size; i++) {
      Entity otherEntity = allEntities.get(i);

      if (source == otherEntity) continue; // Skip the source entity

      Vector2 positionSource = source.getPosition();
      Vector2 positionOther = otherEntity.getPosition();

      if (positionSource.dst(positionOther) <= radius) {
        nearbyEntities.add(otherEntity);
      }
    }
    return nearbyEntities;
  }

  /**
   * Get entities within a certain radius of a given entity.
   *
   * @param source The reference entity to check distance from.
   * @param radius The radius within which to fetch entities.
   * @param layer Desired layer for entities to be in
   * @return An array containing entities within the given radius.
   */
  public Array<Entity> getEntitiesInLayer(Entity source, float radius, short layer) {
    Array<Entity> entities = new Array<Entity>();
    Array<Entity> allEntities = getNearbyEntities(source, radius);

    for (int i = 0; i < allEntities.size; i++) {
      Entity targetEntity = allEntities.get(i);

      // check targets layer
      HitboxComponent targetHitbox = targetEntity.getComponent(HitboxComponent.class);
      if (targetHitbox == null) {
        continue;
      }
      if (!PhysicsLayer.contains(layer, targetHitbox.getLayer())) {
        continue;
      }
      entities.add(targetEntity);
    }
    return entities;
  }

  /**
   * Returns the closest entity to the source of provided layer
   * @param source source entity
   * @param layer layer for desired entity to be returned
   * @return closest entity of correct layer
   */
  public Entity getClosestEntityOfLayer(Entity source, short layer) {
    Entity closestHuman = null;
    Vector2 sourcePos = source.getPosition();
    float closestDistance = MAX_RADIUS;
    Array<Entity> entitiesInLayer = getEntitiesInLayer(source, MAX_RADIUS, layer);

    for (int i = 0; i < entitiesInLayer.size; i++) {
      Entity targetEntity = entitiesInLayer.get(i);

      // check how close target is to source
      Vector2 targetPosition = targetEntity.getPosition();
      float distance = sourcePos.dst(targetPosition);
      if (distance < closestDistance) {
        closestHuman = targetEntity;
        closestDistance = distance;
      }
    }
    return closestHuman;
  }
  
  public Entity getEntityAtPosition(float x, float y) {
    entities.sort(Comparator.comparingInt(Entity::getLayer));
    for (Entity entity : entities) {
      if (entityContainsPosition(entity, x, y)) {
        return entity;
      }
    }
    return null;
  }

  private boolean entityContainsPosition(Entity entity, float x, float y) {
    float entityX = entity.getPosition().x;
    float entityY = entity.getPosition().y;
    float entityWidth = entity.getScale().x;
    float entityHeight = entity.getScale().y;
    return (x >= entityX && x <= entityX + entityWidth && y >= entityY && y <= entityY + entityHeight);
  }

}
