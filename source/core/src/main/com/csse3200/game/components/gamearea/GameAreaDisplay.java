package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

/**
 * Displays the name of the current game area.
 */
public class GameAreaDisplay extends UIComponent {
  private static final String DEFAULT_STYLE = "default";
  private String gameAreaName = "";
  private Label title;

  public GameAreaDisplay(String gameAreaName) {
    this.gameAreaName = gameAreaName;
  }

  @Override
  public void create() {
    super.create();
    addActors();
  }

  public void render(float delta) {
    // ... other rendering logic ...

    // Update the stage
    stage.act(delta);
    stage.draw();
  }

  private Dialog createTowerDetailsDialog() {
    Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));

    // Register a label style named "default" with the skin
    Label.LabelStyle labelStyle = new Label.LabelStyle();
    labelStyle.font = new BitmapFont();
    labelStyle.fontColor = Color.WHITE;
    skin.add(DEFAULT_STYLE, labelStyle);

    // Create the dialog using the registered label style
    Dialog dialog = new Dialog("Tower Details", skin, DEFAULT_STYLE);
    dialog.text("Health: 100"); // Set tower health here
    dialog.getContentTable().row();
    dialog.text("Attack: 50"); // Set tower attack here
    dialog.button("Close");
    dialog.setVisible(false); // Hide the dialog initially
    return dialog;
  }

  private void addActors() {
    title = new Label(this.gameAreaName, skin, DEFAULT_STYLE);
    stage.addActor(title);
  }

  @Override
  public void draw(SpriteBatch batch) {
    int screenHeight = Gdx.graphics.getHeight();
    float offsetX = 10f;
    float offsetY = 30f;

    title.setPosition(offsetX, screenHeight - offsetY);
  }

  @Override
  public void dispose() {
    super.dispose();
    title.remove();
  }
}