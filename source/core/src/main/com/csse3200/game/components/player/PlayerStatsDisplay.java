package com.csse3200.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;
  private Label healthLabel;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "default");

    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel);
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  // Inside the PlayerStatsDisplay class
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);

    if (health == 0) {
      // Player's health is zero or negative, trigger game over.
      showGameOverPopup();
    }
  }

  private void showGameOverPopup() {
    // Create a Dialog pop-up for game over.
    Dialog gameOverDialog = new Dialog("Game Over", skin);

    // Add a label with the game over message.
    gameOverDialog.text("Your health reached 0. The game is over.");

    // Add a button to restart the game or exit.

    TextButton exitButton = new TextButton("Exit", skin);


    exitButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        // Handle exiting the game (e.g., return to the main menu or close the app).
        // You can use Gdx.app.exit() to close the app gracefully.
        Gdx.app.exit();
      }
    });


    gameOverDialog.button(exitButton);

    // Make the dialog modal (disables interactions with the game underneath).
    gameOverDialog.setModal(true);

    // Calculate the position to center the dialog on the screen.
    float dialogWidth = stage.getWidth() * 0.6f; // Adjust as needed
    float dialogHeight = stage.getHeight() * 0.4f; // Adjust as needed
    float dialogX = (stage.getWidth() - dialogWidth) / 2;
    float dialogY = (stage.getHeight() - dialogHeight) / 2;

    // Set the dialog's size and position.
    gameOverDialog.setSize(dialogWidth, dialogHeight);
    gameOverDialog.setPosition(dialogX, dialogY);

    // Show the dialog on the stage.
    stage.addActor(gameOverDialog);
  }



  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
  }
}