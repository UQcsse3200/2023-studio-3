package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TowerFactory;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TurretSelectionScreen extends ScreenAdapter {

    private Stage stage;
    private List<TowerType> turretList;
    private TextButton confirmButton;

    private GdxGame game;

    private Set<TowerType> selectedTurrets = new HashSet<>();
    private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);

    public TurretSelectionScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Table table = new Table();

        turretList = new ArrayList<>();
        // Add turrets to the list
        turretList.add(TowerType.WEAPON);
        turretList.add(TowerType.TNT);
        turretList.add(TowerType.DROID);
        turretList.add(TowerType.WALL);
        turretList.add(TowerType.FIRE);
        turretList.add(TowerType.STUN);
        turretList.add(TowerType.INCOME);


        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        confirmButton = new TextButton("Continue", skin);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_GAME);
            }
        });



        for (TowerType turret : turretList) {
            TextButton turretButton = new TextButton(turret.getTowerName(), skin);
            turretButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (selectedTurrets.contains(turret)) {
                        // Turret is already selected, unselect it
                        selectedTurrets.remove(turret);
                        // You can also change the button appearance to indicate unselection
                        logger.info(selectedTurrets.toString());
                    } else {
                        // Turret is not selected, select it
                        selectedTurrets.add(turret);
                        logger.info(selectedTurrets.toString());

                        // You can change the button appearance to indicate selection
                    }
                }
            });
            table.add(turretButton).row();
        }
        table.add(confirmButton).padBottom(-400).row();

        stage.addActor(table);
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
