package com.csse3200.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
   * Get a specific entity. If it doesn't exist, returns null.
   * @param id The id of the entity to get.
   * @return The entity with the given id, or null if it doesn't exist.
   */
  public Entity getEntity(int id) {
    for (Entity entity : entities) {
      if (entity.getId() == id) {
        return entity;
      }
    }
    return null;
  }

  /**
   * Get entities within a certain radius of a given entity.
   *
   * @param source The reference entity to check distance from.
   * @param radius The radius within which to fetch entities.
   * @return An Array<Entity> containing entities within the given radius.
   */
  public Array<Entity> getNearbyEntities(Entity source, float radius) {
    Array<Entity> nearbyEntities = new Array<Entity>();
    Array<Entity> allEntities = ServiceLocator.getEntityService().getEntities();
    for (Entity otherEntity : allEntities) {
      if (source == otherEntity) continue; // Skip the source entity

      Vector2 positionSource = source.getPosition();
      Vector2 positionOther = otherEntity.getPosition();

      if (positionSource.dst(positionOther) <= radius) {
        nearbyEntities.add(otherEntity);
      }
    }
    return nearbyEntities;
  }
}
