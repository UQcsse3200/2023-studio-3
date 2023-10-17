package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/** Factory for creating game terrains. */
public class TerrainFactory {


  private final OrthographicCamera camera;
  private final TerrainComponent.TerrainOrientation orientation;

  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainComponent.TerrainOrientation.ORTHOGONAL);
  }

  /**
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainComponent.TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory.
   * This can be extended to add additional game terrains.
   * @param terrainType Terrain to create
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(new String[]{"images/terrain_use.png"});
    resourceService.loadAll();
    if (terrainType == TerrainType.ALL_DEMO) {
      TextureRegion orthogonal =
              new TextureRegion(resourceService.getAsset("images/terrain_use.png", Texture.class));
      return createTerrain(1f, orthogonal);
    }
    return null;
  }

  /**
   * Creates and returns a terrain component .
   *
   * @param tileWorldSize The world size of a tile.
   * @param terrain The texture region representing grass.
   * @return A TerrainComponent instance representing the created terrain.
   */

  private TerrainComponent createTerrain(float tileWorldSize, TextureRegion terrain) {
    GridPoint2 tilePixelSize = new GridPoint2(terrain.getRegionWidth(), terrain.getRegionHeight());
    TiledMap tiledMap = createTiles(tilePixelSize, terrain);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  /**
   * Creates and returns a renderer for the provided tiled map and scale.
   *
   * @param tiledMap The tiled map for which the renderer is created.
   * @param tileScale The scale factor for the tiles.
   * @return A TiledMapRenderer instance suitable for the given map and scale.
   */

  public TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    if (orientation == TerrainComponent.TerrainOrientation.ORTHOGONAL) {
      return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
    }
    return null;
  }

  /**
   * Creates a tiled map filled with forest demo tiles using the provided grass texture.
   *
   * @param tileSize The dimensions (width and height) of a tile in pixels.
   * @param terrain The texture region representing grass.
   * @return A TiledMap instance representing the created map.
   */
  private TiledMap createTiles(GridPoint2 tileSize, TextureRegion terrain) {
    TiledMap tiledMap = new TiledMap();

    TiledMapTileLayer layer = new TiledMapTileLayer(20, 6, tileSize.x, tileSize.y);
    fillInvisibleTiles(layer, new GridPoint2(20, 6), terrain);
    tiledMap.getLayers().add(layer);

    return tiledMap;
  }


  /**
   * Fills the provided tile layer with invisible tiles to create a transparent layer.
   *
   * @param layer The tile layer to fill.
   * @param mapSize The size of the map in tiles.
   * @param terrain The tile used to fill the layer.
   */
  private void fillInvisibleTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TextureRegion terrain) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        TerrainTile tile = new TerrainTile(terrain);
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
      }
    }
  }

  public enum TerrainType {
    ALL_DEMO
  }
}
