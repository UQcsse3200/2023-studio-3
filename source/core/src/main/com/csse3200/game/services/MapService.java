package com.csse3200.game.services;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.CameraShaker;



/**
 * Provides services related to map functionalities such as tiles and lanes in genral.
 */
public class MapService {

    private Entity entity;
    private final TerrainFactory terrainFactory;
    private final CameraShaker cameraShakerMap;
    private final CameraShaker cameraShakerGrid;

    /**
     * Constructs a new MapService instance based on the provided camera.
     *
     * @param camera The camera component used for the terrain creation.
     */
    public MapService(CameraComponent camera,Camera cam) {
        this.terrainFactory = new TerrainFactory(camera);
        this.entity = new Entity().addComponent(terrainFactory.createTerrain(TerrainFactory.TerrainType.ALL_DEMO));
        this.cameraShakerGrid = new CameraShaker(camera.getCamera());
        this.cameraShakerMap = new CameraShaker(cam,2,0.1f,0.8f);

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
        this.cameraShakerMap = new CameraShaker(new OrthographicCamera());
        this.cameraShakerGrid = new CameraShaker(new OrthographicCamera());
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

    /**
     * Update method for the Grid Camera Shaker. Calls this method in the main game loop.
     *
     * @param delta Time since the last frame.
     */
    public void updateShakerGrid(float delta) {
        cameraShakerGrid.update(delta);
    }

    /**
     * Starts the Grid shaking process
     */
    public void shakeCameraGrid() {
        cameraShakerGrid.startShaking();
    }

    /**
     * Update method for the Background Camera Shaker. Calls this method in the main game loop.
     *
     * @param delta Time since the last frame.
     */
    public void updateShakerMap(float delta) {
        cameraShakerMap.update(delta);
    }

    /**
     * Starts the Background shaking process
     */
    public void shakeCameraMap() {
        cameraShakerMap.startShaking();
    }

}