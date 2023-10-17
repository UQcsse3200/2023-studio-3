package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.*;
import com.csse3200.game.components.gamearea.PerformanceDisplay;
import com.csse3200.game.components.maingame.MainGameActions;
import com.csse3200.game.components.maingame.MainGameDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.*;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.*;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: <a href="https://happycoding.io/tutorials/libgdx/game-screens">...</a>
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {
          "images/heart.png",
          "images/ice_bg.png",
          "images/lava_bg.png",
          "images/desert_bg.png"
  };

  private static final String ICE_BACKDROP = mainGameTextures[1];
  private static final String LAVA_BACKDROP = mainGameTextures[2];
  private static final String DESERT_BACKDROP = mainGameTextures[3];
  private static final String[] backgroundMusic = {
          "sounds/background/ice/ice_bgm.ogg",
          "sounds/background/lava/lava_bgm.ogg",
          "sounds/background/desert/desert_bgm.ogg"
  };
  private static final String[] uiSounds = {
          "sounds/ui/click/click_01.ogg",
          "sounds/ui/hover/hover_01.ogg",
          "sounds/ui/open_close/close_01.ogg",
          "sounds/ui/open_close/open_01.ogg",
          "sounds/ui/switch/switch_01.ogg"
  };
  private static final String[] desertSounds = {
          "sounds/background/desert/Elements.ogg",
          "sounds/background/desert/Rocks1.ogg",
          "sounds/background/desert/Rocks2.ogg"
  };
  private static final String[] iceSounds = {
          "sounds/background/ice/Sequences1.ogg",
          "sounds/background/ice/Sequences2.ogg",
          "sounds/background/ice/Sequences3.ogg"
  };
  private static final String[] lavaSounds = {
          "sounds/background/lava/Burst.ogg",
          "sounds/background/lava/Glitch_ripples.ogg",
          "sounds/background/lava/Sizzling.ogg",
          "sounds/background/lava/Swoosh.ogg"
  };
  private static final String ICE_BGM = backgroundMusic[0];
  private static final String LAVA_BGM = backgroundMusic[1];
  private static final String DESERT_BGM = backgroundMusic[2];
  private static final Vector2 CAMERA_POSITION = new Vector2(10f, 5.64f);

  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private final InputComponent buildHandler;
  private final InputComponent upgradedInputHandler;
  static int screenWidth = Gdx.graphics.getWidth();
  static int screenHeight = Gdx.graphics.getHeight();
  private Entity ui;
  public static final int viewportWidth = screenWidth;
  public static final int viewportHeight= screenHeight;
  int selectedLevel = GameLevelData.getSelectedLevel();

  private final OrthographicCamera camera;
  private final SpriteBatch batch;

  private Texture backgroundTexture;
  private Music music;
  private final Array<String> ambientSounds = new Array<>(false, 5, String.class);

  public MainGameScreen(GdxGame game) {
    this.game = game;
    camera = new OrthographicCamera();
    camera.setToOrtho(false, viewportWidth, viewportHeight);
    camera.position.set((float) (viewportWidth) / 2, (float) (viewportHeight) / 2, 0);

    batch = new SpriteBatch();

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
    renderer.getCamera().getCamera().position.set(CAMERA_POSITION.x,CAMERA_POSITION.y,0);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());
    InputComponent inputHandler = new DropInputComponent(renderer.getCamera().getCamera());
    buildHandler = new BuildInputComponent(renderer.getCamera().getCamera());
    upgradedInputHandler = new UpgradeUIComponent(renderer.getCamera().getCamera(), renderer.getStage());
    InputComponent engineerInputHandler = new EngineerInputComponent(game, renderer.getCamera().getCamera());
    ServiceLocator.getInputService().register(inputHandler);
    ServiceLocator.getInputService().register(buildHandler);
    ServiceLocator.getInputService().register(engineerInputHandler);
    ServiceLocator.getInputService().register(upgradedInputHandler);
    ServiceLocator.getCurrencyService().getDisplay().setCamera(renderer.getCamera().getCamera());

    loadAssets();
    createUI();
    ServiceLocator.registerMapService(new MapService(renderer.getCamera(),camera));
    logger.debug("Initialising main game screen entities");
    ForestGameArea forestGameArea = new ForestGameArea();
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
      case 1 -> { // Ice
        background = ServiceLocator.getResourceService().getAsset(ICE_BACKDROP, Texture.class);
        music = ServiceLocator.getResourceService().getAsset(ICE_BGM, Music.class);
        ambientSounds.addAll(iceSounds);
      }
      case 2 -> { // Lava
        background = ServiceLocator.getResourceService().getAsset(LAVA_BACKDROP, Texture.class);
        music = ServiceLocator.getResourceService().getAsset(LAVA_BGM, Music.class);
        ambientSounds.addAll(lavaSounds);
      }
      default -> {
        // Use a default background for other levels or planets
        background = ServiceLocator.getResourceService().getAsset(DESERT_BACKDROP, Texture.class);
        music = ServiceLocator.getResourceService().getAsset(DESERT_BGM, Music.class);
        ambientSounds.addAll(desertSounds);
      }
    }
    return background;
  }

  @Override
  public void render(float delta) {
    // Clear the screen
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    ServiceLocator.getMapService().updateShakerGrid(delta);
    // Update the camera and set the batch's projection matrix
    camera.update();
    batch.setProjectionMatrix(camera.combined);
    ServiceLocator.getMapService().updateShakerMap(delta);

    // Begin the batch
    batch.begin();

    // Draw the background texture.
    batch.draw(backgroundTexture, 0, 0, viewportWidth, viewportHeight);

    // End the batch
    batch.end();

    // Continue with other rendering logic
    physicsEngine.update();
    ServiceLocator.getEntityService().update();

    // Checks if tower selected is dead
    this.getUpgradedInputHandler().checkForDispose();

    ServiceLocator.getWaveService().getDisplay().updateTimerButton();
    ServiceLocator.getWaveService().getDisplay().updateMobCount();
    renderer.render();

    // Check if the game has ended
    if (ServiceLocator.getGameEndService().hasGameEnded()) {
      ui.getEvents().trigger("lose");
    } else if (ServiceLocator.getWaveService().isLevelCompleted()) {
      // Check if all waves are completed and the level has been completed
      if (selectedLevel == 2) { // Lava level
        // If it's the lava level, go to the "win" screen
        ui.getEvents().trigger("win");
        logger.info("Main game level completed detected, go to win screen");
      } else {
        // For other levels, go to the "NextLevelScreen"
        game.setScreen(new NextLevelScreen(game, selectedLevel));
        logger.info("game level completed detected, go to NextLevelScreen");
      }
    }
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
    ServiceLocator.getResourceService().loadMusic(backgroundMusic);
    ServiceLocator.getResourceService().loadSounds(iceSounds);
    ServiceLocator.getResourceService().loadSounds(desertSounds);
    ServiceLocator.getResourceService().loadSounds(lavaSounds);
    ServiceLocator.getResourceService().loadSounds(uiSounds);
    ServiceLocator.getResourceService().loadAll();
    backgroundTexture = getBackgroundTexture(); // Load the background image
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainGameTextures);
    ServiceLocator.getResourceService().unloadAssets(backgroundMusic);
    ServiceLocator.getResourceService().unloadAssets(iceSounds);
    ServiceLocator.getResourceService().unloadAssets(desertSounds);
    ServiceLocator.getResourceService().unloadAssets(lavaSounds);
    ServiceLocator.getResourceService().unloadAssets(uiSounds);
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
            .addComponent(ServiceLocator.getWaveService().getDisplay())
            //.addComponent(new MainGameWinDisplay()) <- needs to be uncommented when team 3 have implemented the ui
            .addComponent(new MainGameDisplay(this.game, selectedLevel))
            .addComponent(new Terminal())
            .addComponent(buildHandler)
            .addComponent(inputComponent)
            .addComponent(new TerminalDisplay());


    ServiceLocator.getEntityService().register(ui);

    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
    playAmbientSound();
  }

  /**
   * Plays one of the ambient sounds for the level at random
   */
  private void playAmbientSound() {

     ServiceLocator.getResourceService().getAsset(ambientSounds.random(), Sound.class).play(0.2f);
  }

  private UpgradeUIComponent getUpgradedInputHandler() {
    return (UpgradeUIComponent) upgradedInputHandler;
  }
}