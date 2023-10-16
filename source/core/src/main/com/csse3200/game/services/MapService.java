package com.csse3200.game.services;

import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;

/**
 * Provides services related to map functionalities such as tiles and lanes in genral.
 */
public class MapService {

    private Entity entity;
    private final TerrainFactory terrainFactory;

    /**
     * Constructs a new MapService instance based on the provided camera.
     *
     * @param camera The camera component used for the terrain creation.
     */
    public MapService(CameraComponent camera) {
        this.terrainFactory = new TerrainFactory(camera);
        this.entity = new Entity().addComponent(terrainFactory.createTerrain(TerrainFactory.TerrainType.ALL_DEMO));
    }

    /**
     * Constructs a new MapService instance using the given entity and terrain factory.
     *
     * @param entity The entity associated with this service.
     * @param terrainFactory The terrain factory used for creating terrains.
     */
    public MapService(Entity entity, TerrainFactory terrainFactory) {
        this.entity = entity;
        this.terrainFactory = terrainFactory;
    }

    /**
     * Returns the associated entity.
     *
     * @return The entity related to this map service.
     */
    public Entity getEntity() {

        return this.entity;
    }

    /**
     * Retrieves the terrain component from the entity.
     *
     * @return The terrain component of the associated entity.
     */
    public TerrainComponent getComponent() {

        return entity.getComponent(TerrainComponent.class);
    }

    /**
     * Returns the height of the Grid.
     *
     * @return Height of the Grid.
     */
    public int getHeight() {

        return entity.getComponent(TerrainComponent.class).getMapBounds(0).y;
    }

    /**
     * Returns the width of the Grid.
     *
     * @return Width of the Grid.
     */
    public int getWidth() {

        return entity.getComponent(TerrainComponent.class).getMapBounds(0).x;
    }
}
