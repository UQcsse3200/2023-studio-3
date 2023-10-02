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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

    // Continue with the rest of the rendering
    renderer.render();
  }

  @Override
  public void resize(int width, int height) {
    renderer.resize(width, height);
    logger.trace("Resized renderer: ({} x {})", width, height);
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
    resourceService.loadTextures(SettingsTextures);
    backgroundTexture = new Texture("images/background/settings/settings_bg.png");
    ServiceLocator.getResourceService().loadAll();
  }

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
