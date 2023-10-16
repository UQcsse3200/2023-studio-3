package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.screens.AnimationTexturePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
  private final GdxGame game;
  private final Renderer renderer;
  private Texture backgroundTexture;
  private final SpriteBatch batch;
  private static final String[] mainMenuAtlases = {
          "images/ui/buttons/glass.atlas"
  };
  private static final String[] titleMusic = {"sounds/background/title_screen/ScifiAmbient.ogg"};
  private static final String[] mainMenuTextures = {"images/background/main_menu/main_menu_bg.png"};
  private Animation<TextureRegion> mmStar1Animation;
  private Animation<TextureRegion> mmGalaxy1Animation;
  private Animation<TextureRegion> mmPlanet1Animation;
  private Animation<TextureRegion> mmPlanet2Animation;
  private Animation<TextureRegion> mmPlanet3Animation;
  private Animation<TextureRegion> mmMonitorface1Animation;
  private Texture mmStar1Texture;
  private Texture mmGalaxy1Texture;
  private Texture mmPlanet1Texture;
  private Texture mmPlanet2Texture;
  private Texture mmPlanet3Texture;
  private Texture mmMonitorface1Texture;
  private float elapsedTime = 0;
  private int mmStar1Framewidth;
  private int mmStar1Frameheight;
  private int mmGalaxy1Framewidth;
  private int mmGalaxy1Frameheight;
  private int mmPlanet1Framewidth;
  private int mmPlanet1Frameheight;
  private int mmPlanet2Framewidth;
  private int mmPlanet2Frameheight;
  private int mmPlanet3Framewidth;
  private int mmPlanet3Frameheight;
  private int mmMonitorface1Framewidth;
  private int mmMonitorface1Frameheight;

  public MainMenuScreen(GdxGame game) {
    this.game = game;

    logger.debug("Initialising main menu screen services");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());

    renderer = RenderFactory.createRenderer();
    batch = new SpriteBatch();
    Gdx.gl.glClearColor(0.5f, 0.6f, 0.19f, 1);


    loadAssets();
    createUI();
  }

  /**
   * Renders the game screen with the given time delta.
   *
   * @param delta The time elapsed since the last frame in seconds.
   */
  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    ServiceLocator.getEntityService().update();

    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();

    // Draw the background image
    batch.begin();
    batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
    batch.end();

    renderer.render();

    batch.begin();
    elapsedTime += delta;

    // Draw various animations at specific positions and scales
    drawAnimation(mmStar1Animation, 890f / backgroundTexture.getWidth(), 680f / backgroundTexture.getHeight(), 0.3f);
    drawAnimation(mmGalaxy1Animation, 2100f / backgroundTexture.getWidth(), 1140f / backgroundTexture.getHeight(), 0.95f);
    drawAnimation(mmPlanet1Animation, 1630f / backgroundTexture.getWidth(), 800f / backgroundTexture.getHeight(), 0.5f);
    drawAnimation(mmPlanet2Animation, 1430f / backgroundTexture.getWidth(), 1250f / backgroundTexture.getHeight(), 0.7f);
    drawAnimation(mmPlanet3Animation, 420f / backgroundTexture.getWidth(), 990f / backgroundTexture.getHeight(), 0.65f);
    drawAnimation(mmMonitorface1Animation, 1930f / backgroundTexture.getWidth(), 360f / backgroundTexture.getHeight(), 3.0f);

    batch.end();
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
    logger.debug("Disposing main menu screen");

    renderer.dispose();
    unloadAssets();
    ServiceLocator.getRenderService().dispose();
    ServiceLocator.getEntityService().dispose();
    batch.dispose();

    ServiceLocator.clear();
  }

  /**
   * Loads the assets required for the main menu screen, including textures, texture atlases, music, and animations.
   * This method initializes and loads sprite sheets for various celestial objects such as stars, galaxies, and planets.
   * Each sprite sheet is divided into individual frames for animation purposes.
   * It also loads and sets up animations and textures for specific game objects.
   */
  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    resourceService.loadTextureAtlases(mainMenuAtlases);
    ServiceLocator.getResourceService().loadMusic(titleMusic);
    backgroundTexture = new Texture("images/background/main_menu/main_menu_bg.png");

    AnimationTexturePair result;

    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Star1.png", 60, 1, 0.15f);
    mmStar1Animation = result.getAnimation();
    mmStar1Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Galaxy1.png", 60, 1, 0.15f);
    mmGalaxy1Animation = result.getAnimation();
    mmGalaxy1Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Planet1.png", 60, 1, 0.15f);
    mmPlanet1Animation = result.getAnimation();
    mmPlanet1Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Planet2.png", 60, 1, 0.15f);
    mmPlanet2Animation = result.getAnimation();
    mmPlanet2Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Planet3.png", 54, 1, 0.15f);
    mmPlanet3Animation = result.getAnimation();
    mmPlanet3Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_MonitorFace1.png", 4, 1, 0.1f);
    mmMonitorface1Animation = result.getAnimation();
    mmMonitorface1Texture = result.getTexture();

    ServiceLocator.getResourceService().loadAll();
  }

  /**
   * Unloads the assets that were previously loaded for the main menu screen.
   * This method is responsible for releasing resources to free up memory.
   * This method disposes of textures and sprite sheets used for celestial objects such as stars, galaxies, and planets.
   */
  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
    resourceService.unloadAssets(mainMenuAtlases);
    resourceService.unloadAssets(titleMusic);
    mmStar1Texture.dispose();
    mmGalaxy1Texture.dispose();
    mmPlanet1Texture.dispose();
    mmPlanet2Texture.dispose();
    mmPlanet3Texture.dispose();
    mmMonitorface1Texture.dispose();
  }


  /**
   * Creates the main menu's ui including components for rendering ui elements to the screen and
   * capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    ui.addComponent(new MainMenuDisplay())
            .addComponent(new InputDecorator(stage, 10))
            .addComponent(new MainMenuActions(game));
    ServiceLocator.getEntityService().register(ui);
    Music music = ServiceLocator.getResourceService().getAsset(titleMusic[0], Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  /**
   * Loads an animation from a given texture file path by splitting it into frames
   * based on the specified number of columns and rows and creating an AnimationTexturePair.
   *
   * @param texturePath     The file path of the texture to create the animation from.
   * @param totalColumns    The total number of columns in the texture grid.
   * @param totalRows       The total number of rows in the texture grid.
   * @param frameDuration   The duration of each frame in seconds.
   * @return An AnimationTexturePair containing the animation and its associated texture.
   */
  private AnimationTexturePair loadAnimation(String texturePath, int totalColumns, int totalRows, float frameDuration) {
    Texture texture = new Texture(Gdx.files.internal(texturePath));
    int frameWidth = texture.getWidth() / totalColumns;
    int frameHeight = texture.getHeight() / totalRows;
    TextureRegion[][] frames = TextureRegion.split(texture, frameWidth, frameHeight);
    TextureRegion[] animationFrames = new TextureRegion[totalColumns];
    System.arraycopy(frames[0], 0, animationFrames, 0, totalColumns);
    return new AnimationTexturePair(new Animation<>(frameDuration, animationFrames), texture);
  }

  /**
   * Draws an animation onto the screen at a specified position and scale.
   *
   * @param animation          The animation to be drawn.
   * @param proportionalOffsetX The proportional X-coordinate offset from the screen's left edge.
   * @param proportionalOffsetY The proportional Y-coordinate offset from the screen's bottom edge.
   * @param referenceScaleFactor The scaling factor relative to the screen width.
   */
  private void drawAnimation(Animation<TextureRegion> animation, float proportionalOffsetX, float proportionalOffsetY, float referenceScaleFactor) {
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    float spriteX = proportionalOffsetX * screenWidth;
    float spriteY = proportionalOffsetY * screenHeight;

    float scalingFactor = screenWidth / 1920f;

    float width = animation.getKeyFrames()[0].getRegionWidth() * referenceScaleFactor * scalingFactor;
    float height = animation.getKeyFrames()[0].getRegionHeight() * referenceScaleFactor * scalingFactor;

    batch.draw(animation.getKeyFrame(elapsedTime, true), spriteX, spriteY, width, height);
  }
}