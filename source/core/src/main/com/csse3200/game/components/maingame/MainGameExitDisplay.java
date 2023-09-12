package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import com.badlogic.gdx.graphics.Texture;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.utils.Null;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {

    super.create();
    addActors();
    final Skin skin = new Skin();
    skin.add("default", new LabelStyle(new BitmapFont(), Color.WHITE));
    skin.add("badlogic", new Texture("images/towers/turret_deployed.png"));

    Image sourceImage = new Image(skin, "badlogic");
    sourceImage.setBounds(600, 900, 100, 100);
//    stage.addActor(sourceImage);

//    Label dragTowerLabel = new Label("Drag Tower", skin); // The text you want to display
//    dragTowerLabel.setPosition(610, 890); // Adjust the position as needed
//    stage.addActor(dragTowerLabel);


    DragAndDrop dragAndDrop = new DragAndDrop();
    dragAndDrop.addSource(new Source(sourceImage) {
      @Null
      public Payload dragStart (InputEvent event, float x, float y, int pointer) {
        Payload payload = new Payload();
        payload.setObject("Some payload!");

        payload.setDragActor(getActor());

        Label validLabel = new Label("Some payload!", skin);
        validLabel.setColor(0, 1, 0, 1);
        payload.setValidDragActor(validLabel);

        Label invalidLabel = new Label("Some payload!", skin);
        invalidLabel.setColor(1, 0, 0, 1);
        payload.setInvalidDragActor(invalidLabel);

        return payload;
      }
    });

  }

  private void addActors() {
    table = new Table();
    table.top().right();
    table.setFillParent(true);

    TextButton mainMenuBtn = new TextButton("Quit", skin);

    // Triggers an event when the button is pressed.
    mainMenuBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.debug("Quit button clicked");
          entity.getEvents().trigger("exit");
        }
      });

    table.add(mainMenuBtn).padTop(10f).padRight(10f);

    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    table.clear();
    super.dispose();
  }
}
