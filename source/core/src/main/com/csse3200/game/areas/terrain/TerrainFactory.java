package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csse3200.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.screens.GameLevelData;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import static com.csse3200.game.screens.MainGameScreen.viewportHeight;
import static com.csse3200.game.screens.MainGameScreen.viewportWidth;

/** Factory for creating game terrains. */
public class TerrainFactory {
  public static final GridPoint2 MAP_SIZE = new GridPoint2(20, 8);

  private static OrthographicCamera camera;
  private final TerrainOrientation orientation;
  private static Stage stage;
  int selectedLevel = GameLevelData.getSelectedLevel();
  private Texture whiteTexture;


  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ORTHOGONAL);
    camera.position.set(viewportWidth / 2f, (viewportHeight / 2f), 10);
    Viewport viewport = new ScreenViewport(camera);
    viewport.update(viewportWidth, viewportHeight, true);
    stage = new Stage(viewport, new SpriteBatch());
    camera.update();

    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(1, 1, 1, 1);
    pixmap.fill();
    whiteTexture = new Texture(pixmap);
    pixmap.dispose();
  }

  /**
   * Create a terrain factory
   *
   * @param cameraComponent Camera to render terrains to. Must be orthographic.
   * @param orientation orientation to render terrain at
   */
  public TerrainFactory(CameraComponent cameraComponent, TerrainOrientation orientation) {
    this.camera = (OrthographicCamera) cameraComponent.getCamera();
    this.orientation = orientation;
  }

  /**
   * Create a terrain of the given type, using the orientation of the factory. This can be extended
   * to add additional game terrains.
   *
   * @param terrainType Terrain to create
   * @return Terrain component which renders the terrain
   */
  public TerrainComponent createTerrain(TerrainType terrainType) {
    ResourceService resourceService = ServiceLocator.getResourceService();
    switch (terrainType) {
      case FOREST_DEMO:
        TextureRegion orthoGrass =
                new TextureRegion(resourceService.getAsset("images/terrain_use.png", Texture.class));
        return createForestDemoTerrain(1f, orthoGrass);

      default:
        return null;
    }
  }

  private TerrainComponent createForestDemoTerrain(
          float tileWorldSize, TextureRegion grass) {
    GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
    TiledMap tiledMap = createForestDemoTiles(tilePixelSize, grass);
    TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
    return new TerrainComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
  }

  private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    switch (orientation) {
      case ORTHOGONAL:
        return new OrthogonalTiledMapRenderer(tiledMap, tileScale);
      default:
        return null;
    }
  }

  private TiledMap createForestDemoTiles(GridPoint2 tileSize, TextureRegion grass) {
    TiledMap tiledMap = new TiledMap();
    /**
     * Creates a background layer for a tiled map with the specified dimensions and tile size.
     *
     * @param width The width of the layer in tiles.
     * @param height The height of the layer in tiles.
     * @param tileWidth The width of each individual tile in pixels.
     * @param tileHeight The height of each individual tile in pixels.
     */

    TiledMapTileLayer backgroundLayer = new TiledMapTileLayer(20, 8, tileSize.x, tileSize.y);
    /**
     * Define a TextureRegion to be used as the background texture.
     */
    TextureRegion backgroundTextureRegion ;

    switch (selectedLevel) {
      case 0: // Desert
        backgroundTextureRegion = new TextureRegion(ServiceLocator.getResourceService().getAsset("images/desert_bg.png", Texture.class));
        break;
      case 1: // Ice
        backgroundTextureRegion = new TextureRegion(ServiceLocator.getResourceService().getAsset("images/ice_bg.png", Texture.class));
        break;
      case 2: // Lava
        backgroundTextureRegion = new TextureRegion(ServiceLocator.getResourceService().getAsset("images/lava_bg.png", Texture.class));
        break;
      default:
        // Use a default background for other levels or planets
        backgroundTextureRegion = new TextureRegion(ServiceLocator.getResourceService().getAsset("images/desert_bg.png", Texture.class));
        break;
    }

    /**
     * Creates a single cell with the specified background texture region and adds it to the background layer
     * of a tiled map. The background layer represents the entire background image of the map.
     *
     * @param backgroundTextureRegion The TextureRegion to use as the background texture.
     * @param tileSizeX The width of each individual tile in pixels.
     * @param tileSizeY The height of each individual tile in pixels.
     * @param tiledMap The TiledMap to which the background layer should be added.
     */
    Cell cell = new Cell();
    cell.setTile(new StaticTiledMapTile(backgroundTextureRegion));
    backgroundLayer.setCell(0, 0, cell);

    tiledMap.getLayers().add(backgroundLayer);

    /**
     * Creates a grass layer for the tiled map with the specified dimensions and tile size, filling it with
     * grass tiles using the provided grass terrain tile.
     *
     * @param tileSizeX The width of each individual tile in pixels.
     * @param tileSizeY The height of each individual tile in pixels.
     * @param grassTile The TerrainTile representing the grass tile to be used for the layer.
     * @param tiledMap The TiledMap to which the grass layer should be added.
     */
    TerrainTile grassTile = new TerrainTile(grass);
    TiledMapTileLayer grassLayer = new TiledMapTileLayer(20, 8, tileSize.x, tileSize.y);
    fillTiles(grassLayer, new GridPoint2(20, 8), grassTile);
    tiledMap.getLayers().add(grassLayer);

    // Create lanes (invisible)
    int numberOfLanes = 8;
    int laneHeight = 1; // Height of each lane in tiles
    int mapWidth = 20;
    int mapHeight = 8;
    int laneTileHeight = mapHeight / numberOfLanes;

    for (int i = 0; i < numberOfLanes; i++) {
      TiledMapTileLayer laneLayer = new TiledMapTileLayer(mapWidth, laneHeight, tileSize.x, tileSize.y);
      fillInvisibleTiles(laneLayer, new GridPoint2(mapWidth, laneHeight));
      tiledMap.getLayers().add(laneLayer);
    }

    return tiledMap;
  }

  private void fillInvisibleTiles(TiledMapTileLayer layer, GridPoint2 mapSize) {
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        // Set an invisible tile (using a transparent texture)
        StaticTiledMapTile invisibleTile = new StaticTiledMapTile(new TextureRegion(whiteTexture));
        cell.setTile(invisibleTile);
        layer.setCell(x, y, cell);
      }
    }
  }

  //tile class
  public static class Tile {
    private int row;
    private int col;
    private Object object;

    public Tile(int row, int col) {
      this.row = row;
      this.col = col;
      this.object = null; // Initially, no object is placed on the tile
    }

    public void setObject(Object object) {
      this.object = object;
    }

    public Object getObject() {
      return object;
    }

    public String getLogCoordinates() {
      return "(" + row + ", " + col + ")";
    }
  }

  // grid class
  public static class Grid {
    private Tile[][] tiles;

    public Grid(int numRows, int numCols) {
      tiles = new Tile[numRows][numCols];

      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          tiles[row][col] = new Tile(row,col);
        }
      }
    }

    public void placeObject(int row, int col, Object object) {
      if (isValidCoordinate(row, col)) {
        tiles[row][col].setObject(object);
      } else {
        System.out.println("Invalid coordinates.");
      }
    }

    public Object getObject(int row, int col) {
      if (isValidCoordinate(row, col)) {
        return tiles[row][col].getObject();
      } else {
        System.out.println("Invalid coordinates.");
        return null;
      }
    }

    public String getLogCoordinates(int row, int col) {
      if (isValidCoordinate(row, col)) {
        return tiles[row][col].getLogCoordinates();
      } else {
        return "Invalid coordinates.";
      }
    }

    private boolean isValidCoordinate(int row, int col) {
      return row >= 0 && row < tiles.length && col >= 0 && col < tiles[0].length;
    }

    public void placeEntity(int row, int col, Object existingEntity) {
    }

    public Object getEntity(int row, int col) {
      return null;
    }
  }

  // Array class 1+2
  public class Array {
    public static void main(String[] args) {
      int numRows = 8;
      int numCols = 20;

      Grid grid = new Grid(numRows, numCols);

      // Place an existing entity in a specific tile
      int row = 3;
      int col = 5;
      // Replace 'Object' with the type of existing entity you want to place
      Object existingEntity = new YourExistingEntity();

      grid.placeEntity(row, col, existingEntity);

      // Get the entity from a tile
      Object entity = grid.getEntity(row, col);
      System.out.println("Entity at " + grid.getLogCoordinates(row, col) + ": " + entity);
    }

    private static class YourExistingEntity {
    }
  }

  private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
    BitmapFont font = new BitmapFont();
    TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    textButtonStyle.font = font;
    textButtonStyle.fontColor = Color.WHITE;
    for (int x = 0; x < mapSize.x; x++) {
      for (int y = 0; y < mapSize.y; y++) {
        Cell cell = new Cell();
        cell.setTile(tile);
        layer.setCell(x, y, cell);
        TextButton button = new TextButton("" + x + y * 20, textButtonStyle);
        stage.addActor(button);
      }
    }
  }

  public enum TerrainType {
    FOREST_DEMO
  }
}