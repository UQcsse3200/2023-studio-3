package com.csse3200.game.components.maingame;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Game Screen and does something when one of the
 * events is triggered.
 */
public class MainGameActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(MainGameActions.class);
  private final GdxGame game;

  public MainGameActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("lose", this::onLose);
    entity.getEvents().addListener("win", this::WinningScreen);
    entity.getEvents().addListener("Next Level", this::NextLevel);
  }

  private void NextLevel() {
    logger.info("Next level");
    game.setScreen(GdxGame.ScreenType.Next_Screen);

  }

  private void WinningScreen() {
    logger.info("Uer Won the game");
    game.setScreen(GdxGame.ScreenType.Win_Screen);
  }

  /**
   * Swaps to the Main Menu screen.
   */
  private void onExit() {
    logger.info("Exiting main game screen");
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  private void onLose() {
    game.setScreen(GdxGame.ScreenType.LOSING_SCREEN);
  }
}
