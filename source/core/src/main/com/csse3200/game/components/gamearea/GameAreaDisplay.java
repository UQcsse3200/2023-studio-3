package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Null;
import com.csse3200.game.components.maingame.MainGameExitDisplay;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.concurrent.TimeUnit;

/**
 * Displays the name of the current game area.
 */
public class GameAreaDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(GameAreaDisplay.class);
  private static final float Z_INDEX = 2f;


  private Vector2[] towerPositions1;
  private Vector2[] towerPositions2;
  private Vector2[] towerPositions3;// Store the positions of the towers

  private String gameAreaName = "";
  private Label title;
  private int numTowers = 2; // Total number of towers


  private boolean[] towerMoved1;
  private boolean[] towerMoved2;
  private boolean[] towerMoved3;

  public GameAreaDisplay(String gameAreaName) {
    this.gameAreaName = gameAreaName;
    // Create the tower details dialog
    towerPositions1 = new Vector2[2]; // Initialize for two towers
    towerMoved1 = new boolean[2]; // Initialize for two towers

    // Create the tower details dialog
    towerPositions2 = new Vector2[2]; // Initialize for two towers
    towerMoved2 = new boolean[2]; // Initialize for two towers

    towerPositions3 = new Vector2[3]; // Initialize for two towers
    towerMoved3 = new boolean[3]; // Initialize for two towers
  }

  @Override
  public void create() {
    super.create();
    addActors();
    final Skin skin = new Skin();




    Image[] towers1 = new Image[2]; // Create an array for two towers
    Image[] towers2 = new Image[2];
    Image[] towers3 = new Image[2];// Create an array for two towers

    for (int i = 0; i < 2; i++) {
      // Use "building1" for the first tower and "building2" for the second tower
      skin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      skin.add("building1", new Texture("images/towers/mine_tower.png"));
      // Load textures for building1 and building2
      towers1[i] = new Image(skin, "building1");
      towers1[i].setBounds(600 , 700, 100, 100); // Adjust the X position as needed
      stage.addActor(towers1[i]);

      final int towerIndex1 = i; // Capture the index in a final variable for the listener
      towerPositions1[towerIndex1] = new Vector2(towers1[towerIndex1].getX(), towers1[towerIndex1].getY());

      towers1[i].addListener(new InputListener() {
        private float startX, startY;

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          startX = towers1[towerIndex1].getX();
          startY = towers1[towerIndex1].getY();
          Gdx.app.log("GameAreaDisplay", "Touch Down on Tower " + towerIndex1);

          return true; // Return true to indicate that the event was handled
        }

        public void touchDragged(InputEvent event, float x, float y, int pointer) {
          float deltaX = towers1[towerIndex1].getX() - startX;
          float deltaY = towers1[towerIndex1].getY() - startY;

          // Calculate the distance moved
          float distanceMoved = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

          // Check if the tower has been moved for a significant distance
          if (!towerMoved1[towerIndex1] && distanceMoved >= 100f) {
            // Decrement the number of towers and set the flag for this tower
            numTowers--;

            towerMoved1[towerIndex1] = true; // Set the flag to indicate tower movement
          }

          // Implement dragging logic here
          towers1[towerIndex1].moveBy(x - towers1[towerIndex1].getWidth() / 2,
                  y - towers1[towerIndex1].getHeight() / 2);

          towerPositions1[towerIndex1] = new Vector2(towers1[towerIndex1].getX(), towers1[towerIndex1].getY());

        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

        }
      });
    }
    for (int i = 0; i < 2; i++) {
      // Use "building1" for the first tower and "building2" for the second tower
      skin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      skin.add("building2", new Texture("images/towers/turret_deployed.png"));
      towers2[i] = new Image(skin, "building2");
      towers2[i].setBounds(800 , 700, 100, 100); // Adjust the X position as needed
      stage.addActor(towers2[i]);

      final int towerIndex2 = i; // Capture the index in a final variable for the listener
      towerPositions2[towerIndex2] = new Vector2(towers2[towerIndex2].getX(), towers2[towerIndex2].getY());

      towers2[i].addListener(new InputListener() {
        private float startX, startY;

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          startX = towers2[towerIndex2].getX();
          startY = towers2[towerIndex2].getY();
          Gdx.app.log("GameAreaDisplay", "Touch Down on Tower " + towerIndex2);

          return true; // Return true to indicate that the event was handled
        }

        public void touchDragged(InputEvent event, float x, float y, int pointer) {
          float deltaX = towers2[towerIndex2].getX() - startX;
          float deltaY = towers2[towerIndex2].getY() - startY;

          // Calculate the distance moved
          float distanceMoved = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

          // Check if the tower has been moved for a significant distance
          if (!towerMoved2[towerIndex2] && distanceMoved >= 100f) {
            // Decrement the number of towers and set the flag for this tower
            numTowers--;

            towerMoved2[towerIndex2] = true; // Set the flag to indicate tower movement
          }

          // Implement dragging logic here
          towers2[towerIndex2].moveBy(x - towers2[towerIndex2].getWidth() / 2,
                  y - towers2[towerIndex2].getHeight() / 2);

          towerPositions2[towerIndex2] = new Vector2(towers2[towerIndex2].getX(), towers2[towerIndex2].getY());

        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

        }
      });
    }
    for (int i = 0; i < 2; i++) {
      // Use "building1" for the first tower and "building2" for the second tower
      skin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      skin.add("building3", new Texture("images/towers/WallTower.png"));
      towers3[i] = new Image(skin, "building3");
      towers3[i].setBounds(900 , 700, 100, 100); // Adjust the X position as needed
      stage.addActor(towers3[i]);

      final int towerIndex3 = i; // Capture the index in a final variable for the listener
      towerPositions3[towerIndex3] = new Vector2(towers3[towerIndex3].getX(), towers3[towerIndex3].getY());

      towers3[i].addListener(new InputListener() {
        private float startX, startY;

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          startX = towers3[towerIndex3].getX();
          startY = towers3[towerIndex3].getY();
          Gdx.app.log("GameAreaDisplay", "Touch Down on Tower " + towerIndex3);

          return true; // Return true to indicate that the event was handled
        }

        public void touchDragged(InputEvent event, float x, float y, int pointer) {
          float deltaX = towers3[towerIndex3].getX() - startX;
          float deltaY = towers3[towerIndex3].getY() - startY;

          // Calculate the distance moved
          float distanceMoved = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

          // Check if the tower has been moved for a significant distance
          if (!towerMoved3[towerIndex3] && distanceMoved >= 100f) {
            // Decrement the number of towers and set the flag for this tower
            numTowers--;

            towerMoved3[towerIndex3] = true; // Set the flag to indicate tower movement
          }

          // Implement dragging logic here
          towers3[towerIndex3].moveBy(x - towers3[towerIndex3].getWidth() / 2,
                  y - towers3[towerIndex3].getHeight() / 2);

          towerPositions3[towerIndex3] = new Vector2(towers3[towerIndex3].getX(), towers3[towerIndex3].getY());

        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

        }
      });
    }


    // Create and add the label for the number of towers

  }



  public void render(float delta) {
    // ... other rendering logic ...

    // Update the stage
    stage.act(delta);
    stage.draw();
  }

  private void addActors() {
    title = new Label(this.gameAreaName, skin, "large");
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
