package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

/**
 * Displays the name of the current game area.
 */
public class GameAreaDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(GameAreaDisplay.class);
  private static final float Z_INDEX = 2f;
  // Dialog for displaying tower details

  private Vector2[] towerPositions1;
  private Vector2[] towerPositions2; // Store the positions of the towers
  private Vector2[] towerPositions3; // Store the positions of the towers

  private Dialog towerDetailsDialog1;
  private Dialog towerDetailsDialog2;
  private Dialog towerDetailsDialog3;// Dialog for displaying tower details
  private String gameAreaName = "";
  private Label title;
  private int numTowers1 = 2; // Total number of towers
  private int numTowers2 = 2; // Total number of towers
  private int numTowers3 = 2; // Total number of towers
  private Label numTowersLabel1;
  private Label numTowersLabel2;
  private Label numTowersLabel3;

  private boolean[] towerMoved1;
  private boolean[] towerMoved2;// Array of flags to track if each tower has been moved
  private boolean[] towerMoved3;// Array of flags to track if each tower has been moved


  public GameAreaDisplay(String gameAreaName) {
    this.gameAreaName = gameAreaName;
    towerPositions1 = new Vector2[2]; // Initialize for two towers
    towerMoved1 = new boolean[2]; // Initialize for two towers

    towerPositions2 = new Vector2[2]; // Initialize for two towers
    towerMoved2 = new boolean[2]; // Initialize for two towers

    towerPositions3 = new Vector2[2]; // Initialize for two towers
    towerMoved3 = new boolean[2]; // Initialize for two towers

    towerDetailsDialog1 = createTowerDetailsDialog(); // Create the tower details dialog
    towerDetailsDialog2 = createTowerDetailsDialog(); // Create the tower details dialog
    towerDetailsDialog3 = createTowerDetailsDialog(); // Create the tower details dialog
  }

  @Override
  public void create() {
    super.create();
    addActors();
    final Skin skin = new Skin();




    Image[] towers1 = new Image[2]; // Create an array for two towers
    Image[] towers2 = new Image[2]; // Create an array for two towers
    Image[] towers3 = new Image[3]; // Create an array for two towers


    for (int i = 0; i < 2; i++) {
      // Use "building1" for the first tower and "building2" for the second tower
      skin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      skin.add("building1", new Texture("images/towers/WallTower.png"));
      // Load textures for building1 and building2
      towers1[i] = new Image(skin, "building1");
      towers1[i].setBounds(Gdx.graphics.getWidth() * 40f / 100f, Gdx.graphics.getHeight() * 80f / 100f, 100, 100);
      // stage.addActor(towers1[i]);

      final int towerIndex1 = i; // Capture the index in a final variable for the listener
      towerPositions1[towerIndex1] = new Vector2(towers1[towerIndex1].getX(), towers1[towerIndex1].getY());

      towers1[i].addListener(new InputListener() {
        private float startX, startY;

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          startX = towers1[towerIndex1].getX();
          startY = towers1[towerIndex1].getY();
          Gdx.app.log("GameAreaDisplay", "Touch Down on Tower " + towerIndex1);
          towerDetailsDialog1.setVisible(true);
          towerDetailsDialog1.show(stage);

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
            numTowers1--;
            towerMoved1[towerIndex1] = true; // Set the flag to indicate tower movement
          }

          // Implement dragging logic here
          towers1[towerIndex1].moveBy(x - towers1[towerIndex1].getWidth() / 2,
                  y - towers1[towerIndex1].getHeight() / 2);

          towerPositions1[towerIndex1] = new Vector2(towers1[towerIndex1].getX(), towers1[towerIndex1].getY());
          towerDetailsDialog1.setPosition(
                  towerPositions1[towerIndex1].x + towers1[towerIndex1].getWidth(),
                  towerPositions1[towerIndex1].y);

        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          towerDetailsDialog1.hide();
        }
      });
    }
    for (int i = 0; i < 2; i++) {
      // Use "building1" for the first tower and "building2" for the second tower
      skin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      skin.add("building2", new Texture("images/towers/WallTower.png"));
      towers2[i] = new Image(skin, "building2");
      towers2[i].setBounds(Gdx.graphics.getWidth() * 50f / 100f, Gdx.graphics.getHeight() * 80f / 100f, 100, 100);
      stage.addActor(towers2[i]);

      final int towerIndex2 = i; // Capture the index in a final variable for the listener
      towerPositions2[towerIndex2] = new Vector2(towers2[towerIndex2].getX(), towers2[towerIndex2].getY());

      towers2[i].addListener(new InputListener() {
        private float startX, startY;

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          startX = towers2[towerIndex2].getX();
          startY = towers2[towerIndex2].getY();
          Gdx.app.log("GameAreaDisplay", "Touch Down on Tower " + towerIndex2);
          towerDetailsDialog2.setVisible(true);
          towerDetailsDialog2.show(stage);
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
            numTowers2--;
            numTowersLabel2.setText("TowersA: " + numTowers2);
            towerMoved2[towerIndex2] = true; // Set the flag to indicate tower movement
          }

          // Implement dragging logic here
          towers2[towerIndex2].moveBy(x - towers2[towerIndex2].getWidth() / 2,
                  y - towers2[towerIndex2].getHeight() / 2);

          towerPositions2[towerIndex2] = new Vector2(towers2[towerIndex2].getX(), towers2[towerIndex2].getY());
          towerDetailsDialog2.setPosition(
                  towerPositions2[towerIndex2].x + towers2[towerIndex2].getWidth(),
                  towerPositions2[towerIndex2].y
          );
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          towerDetailsDialog2.hide();
        }
      });
    }

    for (int i = 0; i < 2; i++) {
      // Use "building1" for the first tower and "building2" for the second tower
      skin.add("default", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
      skin.add("building3", new Texture("images/towers/mine_tower.png"));
      // Load textures for building1 and building2
      towers3[i] = new Image(skin, "building3");
      towers3[i].setBounds(Gdx.graphics.getWidth() * 60f / 100f, Gdx.graphics.getHeight() * 80f / 100f, 100, 100);
      stage.addActor(towers3[i]);

      final int towerIndex3 = i; // Capture the index in a final variable for the listener
      towerPositions3[towerIndex3] = new Vector2(towers3[towerIndex3].getX(), towers3[towerIndex3].getY());

      towers3[i].addListener(new InputListener() {
        private float startX, startY;

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          startX = towers3[towerIndex3].getX();
          startY = towers3[towerIndex3].getY();
          Gdx.app.log("GameAreaDisplay", "Touch Down on Tower " + towerIndex3);
          towerDetailsDialog3.setVisible(true);
          towerDetailsDialog3.show(stage);
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
            numTowers3--;
            numTowersLabel3.setText("TowersB: " + numTowers3);
            towerMoved3[towerIndex3] = true; // Set the flag to indicate tower movement
          }

          // Implement dragging logic here
          towers3[towerIndex3].moveBy(x - towers3[towerIndex3].getWidth() / 2,
                  y - towers3[towerIndex3].getHeight() / 2);

          towerPositions3[towerIndex3] = new Vector2(towers3[towerIndex3].getX(), towers3[towerIndex3].getY());
          towerDetailsDialog3.setPosition(
                  towerPositions3[towerIndex3].x + towers3[towerIndex3].getWidth(),
                  towerPositions3[towerIndex3].y
          );

        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          towerDetailsDialog3.hide();
        }
      });
    }

    // Create and add the label for the number of towers
    numTowersLabel1 = new Label("TowersA: " + numTowers1, skin);
    numTowersLabel1.setPosition(Gdx.graphics.getWidth() * 41f / 100f, Gdx.graphics.getHeight() * 75f / 100f); // Adjust the position as needed
    //  stage.addActor(numTowersLabel1);

    numTowersLabel2 = new Label("TowersA: " + numTowers2, skin);
    numTowersLabel2.setPosition(Gdx.graphics.getWidth() * 51f / 100f, Gdx.graphics.getHeight() * 75f / 100f); // Adjust the position as needed
    stage.addActor(numTowersLabel2);

    numTowersLabel3 = new Label("TowersB: " + numTowers3, skin);
    numTowersLabel3.setPosition(Gdx.graphics.getWidth() * 61f / 100f, Gdx.graphics.getHeight() * 75f / 100f); // Adjust the position as needed
    stage.addActor(numTowersLabel3);
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
    skin.add("default", labelStyle);

    // Create the dialog using the registered label style
    Dialog dialog = new Dialog("Tower Details", skin,"default");
    dialog.text("Health: 100"); // Set tower health here
    dialog.getContentTable().row();
    dialog.text("Attack: 50"); // Set tower attack here
    dialog.button("Close");
    dialog.setVisible(false); // Hide the dialog initially
    return dialog;
  }
  private void addActors() {
    title = new Label(this.gameAreaName, skin, "default");
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