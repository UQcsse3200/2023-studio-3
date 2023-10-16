package com.csse3200.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.screens.*;
import com.csse3200.game.screens.HelpScreen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.Gdx.app;

/**
 * Entry point of the non-platform-specific game logic. Controls which screen is currently running.
 * The current screen triggers transitions to other screens. This works similarly to a finite state
 * machine (See the State Pattern).
 */
public class GdxGame extends Game {
  private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
  public int currentLevel = 0;
  @Override
  public void create() {
    logger.info("Creating game");
    loadSettings();

    // Sets background to light yellow
    Gdx.gl.glClearColor(248f/255f, 249/255f, 178/255f, 1);

    setScreen(ScreenType.MAIN_MENU);
  }

  /**
   * Loads the game's settings.
   */
  private void loadSettings() {
    logger.debug("Loading game settings");
    UserSettings.Settings settings = UserSettings.get();
    UserSettings.applySettings(settings);
  }

  /**
   * Sets the game's screen to a new screen of the provided type.
   * @param screenType screen type
   */
  public void setScreen(ScreenType screenType) {
    logger.info("Setting game screen to {}", screenType);
    Screen currentScreen = getScreen();
    if (currentScreen != null) {
      currentScreen.dispose();
    }
    setScreen(newScreen(screenType));
  }

  @Override
  public void dispose() {
    logger.debug("Disposing of current screen");
    getScreen().dispose();
  }

  /**
   * Create a new screen of the provided type.
   * @param screenType screen type
   * @return new screen
   */
  private Screen newScreen(ScreenType screenType) {
      return switch (screenType) {
          case Next_Screen -> new NextLevelScreen(this, currentLevel);
          case Win_Screen -> new WinningScreen(this);
          case MAIN_MENU -> new MainMenuScreen(this);
          case MAIN_GAME -> new MainGameScreen(this);
          case SETTINGS -> new SettingsScreen(this);
          case STORY_SCREEN -> new StoryScreen(this);
          case LEVEL_SELECT -> new LevelSelectScreen(this, currentLevel);
          case LOSING_SCREEN -> new LosingScreen(this);
          case TURRET_SELECTION -> new TurretSelectionScreen(this);
          case HELP_SCREEN -> new GameDescriptionHelpScreen(this);
          case HELP_MOBS_SCREEN -> new MobsDescriptionHelpScreen(this);
          case HELP_TOWER_SCREEN -> new TowerDescriptionHelpScreen(this);
          case HELP_BOSS_SCREEN -> new BossDescriptionHelpScreen(this);
          case LOAD_SCREEN -> new LoadingScreen(this);
          case HOW_TO_PLAY -> new HowToPlay(this);

          case TUTORIAL_SCREEN-> new Tutorial(this);
      default-> null;
      };
  }

  public enum ScreenType {
    MAIN_MENU, MAIN_GAME, SETTINGS, STORY_SCREEN, LEVEL_SELECT, TURRET_SELECTION, LOSING_SCREEN, HELP_SCREEN, LOAD_SCREEN,
    HELP_MOBS_SCREEN, HELP_TOWER_SCREEN, HELP_BOSS_SCREEN, Win_Screen, Next_Screen, HOW_TO_PLAY, TUTORIAL_SCREEN
  }

  /**
   * Exit the game.
   */
  public void exit() {
    app.exit();
  }
}