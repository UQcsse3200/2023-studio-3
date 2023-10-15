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
  private Animation<TextureRegion> MM_Star1_animation;
  private Animation<TextureRegion> MM_Galaxy1_animation;
  private Animation<TextureRegion> MM_Planet1_animation;
  private Animation<TextureRegion> MM_Planet2_animation;
  private Animation<TextureRegion> MM_Planet3_animation;
  private Animation<TextureRegion> MM_MonitorFace1_animation;
  private Texture MM_Star1_Texture;
  private Texture MM_Galaxy1_Texture;
  private Texture MM_Planet1_Texture;
  private Texture MM_Planet2_Texture;
  private Texture MM_Planet3_Texture;
  private Texture MM_MonitorFace1_Texture;
  private float elapsedTime = 0;
  private int MM_Star1_frameWidth;
  private int MM_Star1_frameHeight;
  private int MM_Galaxy1_frameWidth;
  private int MM_Galaxy1_frameHeight;
  private int MM_Planet1_frameWidth;
  private int MM_Planet1_frameHeight;
  private int MM_Planet2_frameWidth;
  private int MM_Planet2_frameHeight;
  private int MM_Planet3_frameWidth;
  private int MM_Planet3_frameHeight;
  private int MM_MonitorFace1_frameWidth;
  private int MM_MonitorFace1_frameHeight;

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
    drawAnimation(MM_Star1_animation, 890f / backgroundTexture.getWidth(), 680f / backgroundTexture.getHeight(), 0.3f);
    drawAnimation(MM_Galaxy1_animation, 2100f / backgroundTexture.getWidth(), 1140f / backgroundTexture.getHeight(), 0.95f);
    drawAnimation(MM_Planet1_animation, 1630f / backgroundTexture.getWidth(), 800f / backgroundTexture.getHeight(), 0.5f);
    drawAnimation(MM_Planet2_animation, 1430f / backgroundTexture.getWidth(), 1250f / backgroundTexture.getHeight(), 0.7f);
    drawAnimation(MM_Planet3_animation, 420f / backgroundTexture.getWidth(), 990f / backgroundTexture.getHeight(), 0.65f);
    drawAnimation(MM_MonitorFace1_animation, 1930f / backgroundTexture.getWidth(), 360f / backgroundTexture.getHeight(), 3.0f);

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
    MM_Star1_animation = result.getAnimation();
    MM_Star1_Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Galaxy1.png", 60, 1, 0.15f);
    MM_Galaxy1_animation = result.getAnimation();
    MM_Galaxy1_Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Planet1.png", 60, 1, 0.15f);
    MM_Planet1_animation = result.getAnimation();
    MM_Planet1_Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Planet2.png", 60, 1, 0.15f);
    MM_Planet2_animation = result.getAnimation();
    MM_Planet2_Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_Planet3.png", 54, 1, 0.15f);
    MM_Planet3_animation = result.getAnimation();
    MM_Planet3_Texture = result.getTexture();
    result = loadAnimation("images/background/main_menu/MM_Objects/MM_MonitorFace1.png", 4, 1, 0.1f);
    MM_MonitorFace1_animation = result.getAnimation();
    MM_MonitorFace1_Texture = result.getTexture();

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
    MM_Star1_Texture.dispose();
    MM_Galaxy1_Texture.dispose();
    MM_Planet1_Texture.dispose();
    MM_Planet2_Texture.dispose();
    MM_Planet3_Texture.dispose();
    MM_MonitorFace1_Texture.dispose();
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