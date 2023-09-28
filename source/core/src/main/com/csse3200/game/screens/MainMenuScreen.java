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
  private Animation<TextureRegion> animation;
  private Texture MM_Star1_Texture;
  private float elapsedTime = 0;
  private int MM_Star1_frameWidth;
  private int MM_Star1_frameHeight;
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
    // Fixed offset relative to the background texture
    float MM_Star1_fixedOffsetX = 850;
    float MM_Star1_fixedOffsetY = 650;
    // Calculate the scaling factor based on how the background is stretched to fit the screen
    float scaleX = screenWidth / backgroundTexture.getWidth();
    float scaleY = screenHeight / backgroundTexture.getHeight();
    // Scale the fixed offset
    float spriteX = MM_Star1_fixedOffsetX * scaleX;
    float spriteY = MM_Star1_fixedOffsetY * scaleY;
    // Size adjustments
    float MM_Star1_ScaleFactor = 0.4f;
    float MM_Star1_Width = MM_Star1_frameWidth * MM_Star1_ScaleFactor;
    float MM_Star1_Height = MM_Star1_frameHeight * MM_Star1_ScaleFactor;
    batch.draw(animation.getKeyFrame(elapsedTime, true), spriteX, spriteY, MM_Star1_Width, MM_Star1_Height);

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

    int MM_Star1_totalColumns=10;
    MM_Star1_frameWidth = MM_Star1_Texture.getWidth() / MM_Star1_totalColumns; // totalColumns = no. of columns in MM_Star1 sprite sheet
    int MM_Star1_totalRows=6;
    MM_Star1_frameHeight = MM_Star1_Texture.getHeight() / MM_Star1_totalRows;  // totalRows = no. of rows in MM_Star1 sprite sheet

    TextureRegion[][] MM_Star1_Frames = TextureRegion.split(MM_Star1_Texture, MM_Star1_frameWidth, MM_Star1_frameHeight);

    TextureRegion[] MM_Star1_animationFrames = new TextureRegion[MM_Star1_totalColumns];

    System.arraycopy(MM_Star1_Frames[0], 0, MM_Star1_animationFrames, 0, MM_Star1_totalColumns);

    animation = new Animation<>(0.1f, MM_Star1_animationFrames);

    ServiceLocator.getResourceService().loadAll();
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(mainMenuTextures);
    MM_Star1_Texture.dispose();
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