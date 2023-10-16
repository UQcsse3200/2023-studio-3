package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Render a tiled terrain for a given tiled map and orientation. A terrain is a map of tiles that
 * shows the 'ground' in the game. Enabling/disabling this component will show/hide the terrain.
 */
public class TerrainComponent extends RenderComponent {
  private static final Logger logger = LoggerFactory.getLogger(TerrainComponent.class);
  private static final int TERRAIN_LAYER = 0;

  private final TiledMap tiledMap;
  private final TiledMapRenderer tiledMapRenderer;
  private final OrthographicCamera camera;
  private final TerrainOrientation orientation;
  private final float tileSize;
  private TiledMapTileLayer.Cell lastHoveredCell = null;
  private TiledMapTile originalTile = null;
  private TextureRegion originalRegion = null;



  public TerrainComponent(
      OrthographicCamera camera,
      TiledMap map,
      TiledMapRenderer renderer,
      TerrainOrientation orientation,
      float tileSize) {
    this.camera = camera;
    this.tiledMap = map;
    this.orientation = orientation;
    this.tileSize = tileSize;
    this.tiledMapRenderer = renderer;

  }

  public Vector2 tileToWorldPosition(GridPoint2 tilePos) {
    return tileToWorldPosition(tilePos.x, tilePos.y);
  }

  public Vector2 tileToWorldPosition(int x, int y) {
    switch (orientation) {
      case HEXAGONAL:
        float hexLength = tileSize / 2;
        float yOffset = (x % 2 == 0) ? 0.5f * tileSize : 0f;
        return new Vector2(x * (tileSize + hexLength) / 2, y + yOffset);
      case ISOMETRIC:
        return new Vector2((x + y) * tileSize / 2, (y - x) * tileSize / 2);
      case ORTHOGONAL:
        return new Vector2(x * tileSize, y * tileSize);
      default:
        return null;
    }
  }

  public float getTileSize() {
    return tileSize;
  }

  public GridPoint2 getMapBounds(int layer) {
    TiledMapTileLayer terrainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(layer);
    return new GridPoint2(terrainLayer.getWidth(), terrainLayer.getHeight());
  }

  public TiledMap getMap() {
    return tiledMap;
  }

  @Override
  public void draw(SpriteBatch batch) {
    tiledMapRenderer.setView(camera);
    hoverHighlight();
    tiledMapRenderer.render();
  }

  @Override
  public void dispose() {
    tiledMap.dispose();
    super.dispose();
  }

  @Override
  public float getZIndex() {
    return 0f;
  }

  @Override
  public int getLayer() {
    return TERRAIN_LAYER;
  }

   // TODO : This is just a visual effect that we might not need in the end but just keeping it here for now
  public void colorTile(final int x, final int y) {
    final TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
    final TiledMapTile originalTile = tileLayer.getCell(x, y).getTile();

    ResourceService resourceService = ServiceLocator.getResourceService();

    // Load all the tiles into an array
    final TerrainTile[] terrainTiles = new TerrainTile[7];
    for (int i = 0; i < 7; i++) {
      Texture texture = resourceService.getAsset("images/GrassTile/grass_tile_" + (i + 1) + ".png", Texture.class);
      terrainTiles[i] = new TerrainTile(new TextureRegion(texture));
    }

    final float interval = 0.2f; // Switch every 0.2 seconds
    final float duration = 1.4f; // 7 images * 0.2 seconds each

    Timer.schedule(new Timer.Task() {
      float timeElapsed = 0.0f;

      @Override
      public void run() {
        timeElapsed += interval;

        if (timeElapsed >= duration) {
          tileLayer.getCell(x, y).setTile(originalTile); // Reset to original tile after the total duration
          this.cancel(); // End the timer task
        } else {
          int index = (int) (timeElapsed / interval);
          tileLayer.getCell(x, y).setTile(terrainTiles[index]);
        }
      }
    }, 0, interval, (int) (duration / interval) - 1); // Scheduling the task
  }

  /**
   * Highlights the tile under the mouse cursor by changing its texture region.
   *
   * When hovering over a tile on the terrain, this method performs the following:
   * 
   * 1-Unprojects the mouse's screen position to the world position using the camera.
   * 2-Calculates the tile's coordinates based on the world position and tile size.
   * 3-If there was a previously highlighted tile, it restores its original texture region.
   * 4-If the current tile under the mouse is different from the last hovered tile, it updates.
   * 5-the tile's texture region to a highlight texture.
   * 6-Updates the reference to the last hovered tile.
   *
   * @see TiledMapTileLayer
   * @see TiledMapTileLayer.Cell
   * @see TextureRegion
   */

  public void hoverHighlight() {
    Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(mousePos);


    int tileX = (int) (mousePos.x / tileSize);
    int tileY = (int) (mousePos.y / tileSize);

    final TiledMapTileLayer tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
    TiledMapTileLayer.Cell currentCell = tileLayer.getCell(tileX, tileY);


    if (lastHoveredCell != null && lastHoveredCell != currentCell && originalRegion != null) {
      lastHoveredCell.getTile().setTextureRegion(originalRegion);
    }


    if (currentCell != null && currentCell != lastHoveredCell) {
      originalRegion = currentCell.getTile().getTextureRegion();

      ResourceService resourceService = ServiceLocator.getResourceService();
      Texture texture = resourceService.getAsset("images/highlight_tile.png", Texture.class);
      currentCell.getTile().setTextureRegion(new TextureRegion(texture));
    }

    lastHoveredCell = currentCell;
  }

  public enum TerrainOrientation {
    ORTHOGONAL,
    ISOMETRIC,
    HEXAGONAL
  }
}
