package com.csse3200.game.areas.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.screens.MainGameScreen.viewportHeight;
import static com.csse3200.game.screens.MainGameScreen.viewportWidth;

/** Factory for creating game terrains. */
public class  TerrainFactory {
  public static final GridPoint2 MAP_SIZE = new GridPoint2(20, 8);

  private static OrthographicCamera camera;
  private final TerrainOrientation orientation;
  private static Stage stage;
  /**
   * Create a terrain factory with Orthogonal orientation
   *
   * @param cameraComponent Camera to render terrains to. Must be ortographic.
   */
  public TerrainFactory(CameraComponent cameraComponent) {
    this(cameraComponent, TerrainOrientation.ORTHOGONAL);
    camera.position.set(viewportWidth / 2f, viewportHeight / 2f , 10);
    Viewport viewport = new ScreenViewport(camera);
    viewport.update(viewportWidth, viewportHeight, true);
    stage = new Stage(viewport, new SpriteBatch());
    camera.update();
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

    // Create a background layer
    TiledMapTileLayer backgroundLayer = new TiledMapTileLayer(20, 8, tileSize.x, tileSize.y);
    TextureRegion backgroundTextureRegion = new TextureRegion(ServiceLocator.getResourceService().getAsset("images/ingamebg.png", Texture.class));

    // Create a single cell for the entire background image
    Cell cell = new Cell();
    cell.setTile(new StaticTiledMapTile(backgroundTextureRegion));
    backgroundLayer.setCell(0, 0, cell);

    tiledMap.getLayers().add(backgroundLayer);

    // Create a grass layer
    TerrainTile grassTile = new TerrainTile(grass);
    TiledMapTileLayer grassLayer = new TiledMapTileLayer(20, 8, tileSize.x, tileSize.y);
    fillTiles(grassLayer, new GridPoint2(20, 8), grassTile);
    tiledMap.getLayers().add(grassLayer);

    return tiledMap;
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
