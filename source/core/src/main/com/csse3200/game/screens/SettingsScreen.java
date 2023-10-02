package com.csse3200.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.input.InputService;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The game screen containing the settings. */
public class SettingsScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(SettingsScreen.class);

  private final GdxGame game;
  private final Renderer renderer;
  private Texture backgroundTexture;
  private final SpriteBatch batch;
  private static final String[] SettingsTextures = {"images/background/settings/settings_bg.png"};

  public SettingsScreen(GdxGame game) {
    this.game = game;
    this.batch = new SpriteBatch();
    backgroundTexture = new Texture("images/background/settings/settings_bg.png");

    logger.debug("Initialising settings screen services");
    ServiceLocator.registerInputService(new InputService());
    ServiceLocator.registerResourceService(new ResourceService());
    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerTimeSource(new GameTime());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(5f, 5f);

    loadAssets();
    createUI();
  }

  /**
   * Renders the main gameplay screen.
   *
   * @param delta The time elapsed since the last frame in seconds.
   */
  @Override
  public void render(float delta) {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    ServiceLocator.getEntityService().update();

    // Render the background
    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();

    batch.begin();
    batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
    batch.end();

    renderer.render();
  }

  /**
   * Called when the game window is resized.
   *
   * @param width  The new width of the window.
   * @param height The new height of the window.
   */
  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    // Add the below line to update the stage's viewport
    ServiceLocator.getRenderService().getStage().getViewport().update(width, height, true);
    logger.trace("Resized renderer: ({} x {})", width, height);
    renderer.resize(width, height);
  }

  /**
   * Disposes of resources and services associated with the main menu screen.
   * This method performs cleanup tasks such as disposing of the renderer, unloading assets, disposing of the render service,
   * disposing of the entity service, disposing of the batch, and clearing the service locator.
   */
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
   * Loads the assets required for the settings screen.
   * This method initializes and loads textures, including the background texture and any other assets specified in SettingsTextures.
   */
  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(SettingsTextures);
    backgroundTexture = new Texture("images/background/settings/settings_bg.png");
    ServiceLocator.getResourceService().loadAll();
  }

  /**
   * Unloads the assets that were previously loaded for the settings screen.
   * This method disposes of textures and assets specified in SettingsTextures to release resources.
   */
  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(SettingsTextures);
  }

  /**
   * Creates the setting screen's ui including components for rendering ui elements to the screen
   * and capturing and handling ui input.
   */
  private void createUI() {
    logger.debug("Creating ui");
    Stage stage = ServiceLocator.getRenderService().getStage();
    Entity ui = new Entity();
    ui.addComponent(new SettingsMenuDisplay(game)).addComponent(new InputDecorator(stage, 10));
    ServiceLocator.getEntityService().register(ui);
  }
}
