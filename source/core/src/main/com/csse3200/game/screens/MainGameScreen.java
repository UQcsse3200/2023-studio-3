package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameLoseDisplay;
import com.csse3200.game.components.maingame.UIElementsDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.DropInputComponent;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.*;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {"images/heart.png","images/ice_bg.png","images/lava_bg.png","images/desert_bg.png"};
  private static final Vector2 CAMERA_POSITION = new Vector2(10f, 5.64f);

  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;

  private final Stage stage;
  static int screenWidth = Gdx.graphics.getWidth();
  static int screenHeight = Gdx.graphics.getHeight();

  private Entity ui;


  public static int viewportWidth = screenWidth;
  public static int viewportHeight= screenHeight;
  int selectedLevel = GameLevelData.getSelectedLevel();

  private OrthographicCamera camera;
  private SpriteBatch batch;

  private Texture backgroundTexture;

  public MainGameScreen(GdxGame game) {
    this.game = game;
    camera = new OrthographicCamera();
    camera.setToOrtho(false, viewportWidth, viewportHeight);
    camera.position.set((float) (viewportWidth / 2), (float) (viewportHeight / 2), 0);

    batch = new SpriteBatch();

    stage = new Stage(new ScreenViewport());


    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());

    ServiceLocator.registerCurrencyService(new CurrencyService());

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerGameEndService(new GameEndService());
    ServiceLocator.registerWaveService(new WaveService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());
    InputComponent inputHandler = new DropInputComponent(renderer.getCamera().getCamera());
    ServiceLocator.getInputService().register(inputHandler);

    ServiceLocator.getCurrencyService().getDisplay().setCamera(renderer.getCamera().getCamera());

    loadAssets();
    createUI();

    logger.debug("Initialising main game screen entities");
    TerrainFactory terrainFactory = new TerrainFactory(renderer.getCamera());
    ForestGameArea forestGameArea = new ForestGameArea(terrainFactory);
    forestGameArea.create();
  }

  /**
   * Retrieves the background texture based on the currently selected game level.
   *
   * <p>The method returns different textures for each game level:
   * <ul>
   *     <li>Ice Level: "images/ice_bg.png"</li>
   *     <li>Lava Level: "images/lava_bg.png"</li>
   *     <li>Any other level: Default to "images/desert_bg.png"</li>
   * </ul>
   *
   * @return The background {@link Texture} corresponding to the selected level.
   */
  public Texture getBackgroundTexture() {
    Texture background;
    switch (selectedLevel) {
      // Desert
      case 1: // Ice
        background = ServiceLocator.getResourceService().getAsset("images/ice_bg.png", Texture.class);
        break;
      case 2: // Lava
        background = ServiceLocator.getResourceService().getAsset("images/lava_bg.png", Texture.class);
        break;
      default:
        // Use a default background for other levels or planets
        background = ServiceLocator.getResourceService().getAsset("images/desert_bg.png", Texture.class);
        break;
    }
    return background;
  }

  @Override
  public void render(float delta) {
    // Clear the screen
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Update the camera and set the batch's projection matrix
    camera.update();
    batch.setProjectionMatrix(camera.combined);

    // Begin the batch
    batch.begin();

    // Draw the background texture.
    batch.draw(backgroundTexture, 0, 0, viewportWidth, viewportHeight);

    // End the batch
    batch.end();

    // Continue with other rendering logic
    physicsEngine.update();
    ServiceLocator.getEntityService().update();

    // Check if the game has ended
    if (ServiceLocator.getGameEndService().hasGameEnded()) {
      ui.getEvents().trigger("lose");
    }

    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
  }

  @Override
  public void pause() {
    logger.info("Game paused");
  }

  @Override
  public void resume() {
    logger.info("Game resumed");
  }

  @Override
  public void dispose() {
    logger.debug("Disposing main game screen");

    renderer.dispose();
    unloadAssets();

    ServiceLocator.getEntityService().dispose();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getResourceService().dispose();

    ServiceLocator.clear();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainGameTextures);
    ServiceLocator.getResourceService().loadAll();
    backgroundTexture = getBackgroundTexture(); // Load the background image
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
  }

  /**
   * Creates the main game's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    InputComponent inputComponent =
        ServiceLocator.getInputService().getInputFactory().createForTerminal();

    ui = new Entity();
    ui.addComponent(new InputDecorator(stage, 10))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new UIElementsDisplay())
        .addComponent(new MainGameLoseDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);
  }
}
