package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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
  private Animation<TextureRegion> MM_Star1_animation;
  private Animation<TextureRegion> MM_Galaxy1_animation;
  private Animation<TextureRegion> MM_Planet1_animation;
  private Animation<TextureRegion> MM_Planet2_animation;
  private Animation<TextureRegion> MM_Planet3_animation;
//  private Animation<TextureRegion> MM_MonitorFace1_animation;
  private Texture MM_Star1_Texture;
  private Texture MM_Galaxy1_Texture;
  private Texture MM_Planet1_Texture;
  private Texture MM_Planet2_Texture;
  private Texture MM_Planet3_Texture;
//  private Texture MM_MonitorFace1_Texture;
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

//  private int MM_MonitorFace1_frameWidth;
//  private int MM_MonitorFace1_frameHeight;
  private static final String[] mainMenuTextures = {"images/background/main_menu/main_menu_bg.png"};

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

    // MM_Star1
    // Determine the proportional offset of the MM_Star1 sprite
    float MM_Star1_proportionalOffsetX = 830f / backgroundTexture.getWidth();
    float MM_Star1_proportionalOffsetY = 650f / backgroundTexture.getHeight();
    // Calculate the scaling factor based on how the background is stretched to fit the screen
    float scaleX = screenWidth / backgroundTexture.getWidth();
    float scaleY = screenHeight / backgroundTexture.getHeight();
    // Calculate the position of the MM_Star1 sprite on the screen
    float MM_Star1_spriteX = MM_Star1_proportionalOffsetX * screenWidth;
    float MM_Star1_spriteY = MM_Star1_proportionalOffsetY * screenHeight;
    // Size adjustments
    float MM_Star1_ScaleFactor = 0.3f;
    float MM_Star1_Width = MM_Star1_frameWidth * MM_Star1_ScaleFactor;
    float MM_Star1_Height = MM_Star1_frameHeight * MM_Star1_ScaleFactor;
    batch.draw(MM_Star1_animation.getKeyFrame(elapsedTime, true), MM_Star1_spriteX, MM_Star1_spriteY, MM_Star1_Width, MM_Star1_Height);

    // MM_Galaxy1
    // Determine the proportional offset of the MM_Star1 sprite
    float MM_Galaxy1_proportionalOffsetX = 2000f / backgroundTexture.getWidth();
    float MM_Galaxy1_proportionalOffsetY = 1075f / backgroundTexture.getHeight();
    // Calculate the position of the MM_Star1 sprite on the screen
    float MM_Galaxy1_spriteX = MM_Galaxy1_proportionalOffsetX * screenWidth;
    float MM_Galaxy1_spriteY = MM_Galaxy1_proportionalOffsetY * screenHeight;
    // Size adjustments
    float MM_Galaxy1_ScaleFactor = 0.95f;
    float MM_Galaxy1_Width = MM_Galaxy1_frameWidth * MM_Galaxy1_ScaleFactor;
    float MM_Galaxy1_Height = MM_Galaxy1_frameHeight * MM_Galaxy1_ScaleFactor;
    batch.draw(MM_Galaxy1_animation.getKeyFrame(elapsedTime, true), MM_Galaxy1_spriteX, MM_Galaxy1_spriteY, MM_Galaxy1_Width, MM_Galaxy1_Height);

    // MM_Planet1
    // Determine the proportional offset of the MM_Star1 sprite
    float MM_Planet1_proportionalOffsetX = 1630f / backgroundTexture.getWidth();
    float MM_Planet1_proportionalOffsetY = 800f / backgroundTexture.getHeight();
    // Calculate the position of the MM_Star1 sprite on the screen
    float MM_Planet1_spriteX = MM_Planet1_proportionalOffsetX * screenWidth;
    float MM_Planet1_spriteY = MM_Planet1_proportionalOffsetY * screenHeight;
    // Size adjustments
    float MM_Planet1_ScaleFactor = 0.5f;
    float MM_Planet1_Width = MM_Planet1_frameWidth * MM_Planet1_ScaleFactor;
    float MM_Planet1_Height = MM_Planet1_frameHeight * MM_Planet1_ScaleFactor;
    batch.draw(MM_Planet1_animation.getKeyFrame(elapsedTime, true), MM_Planet1_spriteX, MM_Planet1_spriteY, MM_Planet1_Width, MM_Planet1_Height);

    // MM_Planet2
    // Determine the proportional offset of the MM_Star1 sprite
    float MM_Planet2_proportionalOffsetX = 1290f / backgroundTexture.getWidth();
    float MM_Planet2_proportionalOffsetY = 1200f / backgroundTexture.getHeight();
    // Calculate the position of the MM_Star1 sprite on the screen
    float MM_Planet2_spriteX = MM_Planet2_proportionalOffsetX * screenWidth;
    float MM_Planet2_spriteY = MM_Planet2_proportionalOffsetY * screenHeight;
    // Size adjustments
    float MM_Planet2_ScaleFactor = 0.7f;
    float MM_Planet2_Width = MM_Planet2_frameWidth * MM_Planet2_ScaleFactor;
    float MM_Planet2_Height = MM_Planet2_frameHeight * MM_Planet2_ScaleFactor;
    batch.draw(MM_Planet2_animation.getKeyFrame(elapsedTime, true), MM_Planet2_spriteX, MM_Planet2_spriteY, MM_Planet2_Width, MM_Planet2_Height);

    // MM_Planet3
    // Determine the proportional offset of the MM_Star1 sprite
    float MM_Planet3_proportionalOffsetX = 420f / backgroundTexture.getWidth();
    float MM_Planet3_proportionalOffsetY = 990f / backgroundTexture.getHeight();
    // Calculate the position of the MM_Star1 sprite on the screen
    float MM_Planet3_spriteX = MM_Planet3_proportionalOffsetX * screenWidth;
    float MM_Planet3_spriteY = MM_Planet3_proportionalOffsetY * screenHeight;
    // Size adjustments
    float MM_Planet3_ScaleFactor = 0.65f;
    float MM_Planet3_Width = MM_Planet3_frameWidth * MM_Planet3_ScaleFactor;
    float MM_Planet3_Height = MM_Planet3_frameHeight * MM_Planet3_ScaleFactor;
    batch.draw(MM_Planet3_animation.getKeyFrame(elapsedTime, true), MM_Planet3_spriteX, MM_Planet3_spriteY, MM_Planet3_Width, MM_Planet3_Height);

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

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(mainMenuTextures);
    backgroundTexture = new Texture("images/background/main_menu/main_menu_bg.png");

    // MM_Star1
    MM_Star1_Texture = new Texture(Gdx.files.internal("images/background/main_menu/MM_Objects/MM_Star1.png"));

    int MM_Star1_totalColumns=60;
    MM_Star1_frameWidth = MM_Star1_Texture.getWidth() / MM_Star1_totalColumns; // totalColumns = no. of columns in MM_Star1 sprite sheet
    int MM_Star1_totalRows=1;
    MM_Star1_frameHeight = MM_Star1_Texture.getHeight() / MM_Star1_totalRows;  // totalRows = no. of rows in MM_Star1 sprite sheet

    TextureRegion[][] MM_Star1_Frames = TextureRegion.split(MM_Star1_Texture, MM_Star1_frameWidth, MM_Star1_frameHeight);

    TextureRegion[] MM_Star1_animationFrames = new TextureRegion[MM_Star1_totalColumns];

    System.arraycopy(MM_Star1_Frames[0], 0, MM_Star1_animationFrames, 0, MM_Star1_totalColumns);

    MM_Star1_animation = new Animation<>(0.17f, MM_Star1_animationFrames);


    // MM_Galaxy1
    MM_Galaxy1_Texture = new Texture(Gdx.files.internal("images/background/main_menu/MM_Objects/MM_Galaxy1.png"));

    int MM_Galaxy1_totalColumns=60;
    MM_Galaxy1_frameWidth = MM_Galaxy1_Texture.getWidth() / MM_Galaxy1_totalColumns; // totalColumns = no. of columns in MM_Star1 sprite sheet
    int MM_Galaxy1_totalRows=1;
    MM_Galaxy1_frameHeight = MM_Galaxy1_Texture.getHeight() / MM_Galaxy1_totalRows;  // totalRows = no. of rows in MM_Star1 sprite sheet

    TextureRegion[][] MM_Galaxy1_Frames = TextureRegion.split(MM_Galaxy1_Texture, MM_Galaxy1_frameWidth, MM_Galaxy1_frameHeight);

    TextureRegion[] MM_Galaxy1_animationFrames = new TextureRegion[MM_Galaxy1_totalColumns];

    System.arraycopy(MM_Galaxy1_Frames[0], 0, MM_Galaxy1_animationFrames, 0, MM_Galaxy1_totalColumns);

    MM_Galaxy1_animation = new Animation<>(0.17f, MM_Galaxy1_animationFrames);


    // MM_Planet1
    MM_Planet1_Texture = new Texture(Gdx.files.internal("images/background/main_menu/MM_Objects/MM_Planet1.png"));

    int MM_Planet1_totalColumns=60;
    MM_Planet1_frameWidth = MM_Planet1_Texture.getWidth() / MM_Planet1_totalColumns; // totalColumns = no. of columns in MM_Star1 sprite sheet
    int MM_Planet1_totalRows=1;
    MM_Planet1_frameHeight = MM_Planet1_Texture.getHeight() / MM_Planet1_totalRows;  // totalRows = no. of rows in MM_Star1 sprite sheet

    TextureRegion[][] MM_Planet1_Frames = TextureRegion.split(MM_Planet1_Texture, MM_Planet1_frameWidth, MM_Planet1_frameHeight);

    TextureRegion[] MM_Planet1_animationFrames = new TextureRegion[MM_Planet1_totalColumns];

    System.arraycopy(MM_Planet1_Frames[0], 0, MM_Planet1_animationFrames, 0, MM_Planet1_totalColumns);

    MM_Planet1_animation = new Animation<>(0.17f, MM_Planet1_animationFrames);


    // MM_Planet2
    MM_Planet2_Texture = new Texture(Gdx.files.internal("images/background/main_menu/MM_Objects/MM_Planet2.png"));

    int MM_Planet2_totalColumns=60;
    MM_Planet2_frameWidth = MM_Planet2_Texture.getWidth() / MM_Planet2_totalColumns; // totalColumns = no. of columns in MM_Star1 sprite sheet
    int MM_Planet2_totalRows=1;
    MM_Planet2_frameHeight = MM_Planet2_Texture.getHeight() / MM_Planet2_totalRows;  // totalRows = no. of rows in MM_Star1 sprite sheet

    TextureRegion[][] MM_Planet2_Frames = TextureRegion.split(MM_Planet2_Texture, MM_Planet2_frameWidth, MM_Planet2_frameHeight);

    TextureRegion[] MM_Planet2_animationFrames = new TextureRegion[MM_Planet2_totalColumns];

    System.arraycopy(MM_Planet2_Frames[0], 0, MM_Planet2_animationFrames, 0, MM_Planet2_totalColumns);

    MM_Planet2_animation = new Animation<>(0.17f, MM_Planet2_animationFrames);


    // MM_Planet3
    MM_Planet3_Texture = new Texture(Gdx.files.internal("images/background/main_menu/MM_Objects/MM_Planet3.png"));

    int MM_Planet3_totalColumns=54;
    MM_Planet3_frameWidth = MM_Planet3_Texture.getWidth() / MM_Planet3_totalColumns; // totalColumns = no. of columns in MM_Star1 sprite sheet
    int MM_Planet3_totalRows=1;
    MM_Planet3_frameHeight = MM_Planet3_Texture.getHeight() / MM_Planet3_totalRows;  // totalRows = no. of rows in MM_Star1 sprite sheet

    TextureRegion[][] MM_Planet3_Frames = TextureRegion.split(MM_Planet3_Texture, MM_Planet3_frameWidth, MM_Planet3_frameHeight);

    TextureRegion[] MM_Planet3_animationFrames = new TextureRegion[MM_Planet3_totalColumns];

    System.arraycopy(MM_Planet3_Frames[0], 0, MM_Planet3_animationFrames, 0, MM_Planet3_totalColumns);

    MM_Planet3_animation = new Animation<>(0.17f, MM_Planet3_animationFrames);

    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
    MM_Star1_Texture.dispose();
    MM_Galaxy1_Texture.dispose();
    MM_Planet1_Texture.dispose();
    MM_Planet2_Texture.dispose();
    MM_Planet3_Texture.dispose();
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
  }
}