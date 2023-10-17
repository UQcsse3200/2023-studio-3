package com.csse3200.game.screens.HelpScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.GameArea;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.physics.PhysicsLayer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;

import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.screens.AssetLoader;

import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Timer;

import static com.csse3200.game.entities.factories.NPCFactory.createGhost;
import static com.csse3200.game.screens.AssetLoader.loadAllAssets;
import static com.csse3200.game.screens.AssetLoader.unloadAllAssets;

import java.util.ArrayList;

import java.util.TimerTask;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class TutorialForestGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(com.csse3200.game.screens.HelpScreen.TutorialForestGameArea.class);
    private Timer waveTimer;

    private static final int NUM_WEAPON_TOWERS = 3;
    private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2, 4);
    // Temporary spawn point for testing
    private static final float WALL_WIDTH = 0.1f;

    // Required to load assets before using them
    private static final String backgroundMusic = "sounds/background/Sci-Fi1.ogg";
    private GdxGame game;
    private Entity waves;
    private Table table;
    private Stage stage;
    Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));

    public TutorialForestGameArea() {
        super();
    }


    /**
     * Add this method to start the wave spawning timer when the game starts.
     */
//  private void startWaveTimer() {
//    waveTimer = new Timer();
//    waveTimer.scheduleAtFixedRate(new TimerTask() {
//      @Override
//      public void run() {
//        spawnWave();
//      }
//    }, 0, 10000); // 10000 milliseconds = 10 seconds
//  }

    /**
     * Add this method to stop the wave timer when the game ends or as needed.
     */
    private void stopWaveTimer() {
        if (waveTimer != null) {
            waveTimer.cancel();
            waveTimer = null;
        }
    }

    /**
     * Cases to spawn a wave
     */
//  private void spawnWave() {
//    wave++;
//    switch (wave) {
//      case 1:
//      case 2:
//        spawnFireWorm();
//        spawnDragonKnight();
//
//        break;
//      case 3:
//        spawnSkeleton();
//        spawnWizard();
//        // mobBoss2 = spawnMobBoss2();
//        break;
//      case 4:
//        spawnWaterQueen();
//        spawnWaterSlime();
//        // mobBoss2 = spawnMobBoss2();
//
//        break;
//      case 5:
//        spawnDemonBoss();
//      default:
//        // Handle other wave scenarios if needed
//        break;
//    }
//  }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        // Load game assets
        loadAllAssets();
        loadAssets();
        logger.info("selected towers in main game are " + ServiceLocator.getTowerTypes());
        displayUI();
        spawnTerrain();

        // Set up infrastructure for end game tracking
        waves = WaveFactory.createWaves();
        spawnEntity(waves);
        waves.getEvents().addListener("spawnWave", this::spawnMob);

        spawnScrap();
        spawnGapScanners();

//    spawnTNTTower();
//    spawnWeaponTower();
//    spawnGapScanners();
//    spawnDroidTower();
//     spawnFireWorksTower();  // Commented these out until they are needed for Demonstration
//     spawnPierceTower();
//     spawnRicochetTower();
//    spawnBombship();

    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Tutorial"));
        ui.addComponent(ServiceLocator.getGameEndService().getDisplay());
        ui.addComponent(ServiceLocator.getCurrencyService().getDisplay());
        spawnEntity(ui);
    }

    private void spawnTerrain() {

        terrain = ServiceLocator.getMapService().getComponent();
        spawnEntity(ServiceLocator.getMapService().getEntity());

        // Terrain walls
        float tileSize = terrain.getTileSize();
        GridPoint2 tileBounds = terrain.getMapBounds(0);
        Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

        // Left
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
                new GridPoint2(0, 0),
                false,
                false);
        // Right
        spawnEntityAt(
                ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
                new GridPoint2(tileBounds.x, 0),
                false,
                false);
        // Top
        spawnEntityAt(
                ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH * 0),
                new GridPoint2(0, tileBounds.y),
                false,
                false);
        // Bottom
        Entity wall = ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH * 0);
        wall.setPosition(0,-0.1f);
        ServiceLocator.getEntityService().register(wall);

    }

    /**
     * Spawn an entity on the map. Is called during a wave. Add cases here for each mob type
     * @param entity mob to be spawned
     * @param randomPos position to be spawned at
     * @param health health of the mob
     */
    public void spawnMob(String entity, GridPoint2 randomPos, int health) {
        Entity mob;
                mob = NPCFactory.createWaterQueen(health);
            mob.setScale(1.5f, 1.5f);
        spawnEntityAt(mob, randomPos, true, false);
    }


    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        unloadAllAssets();

    }

    @Override
    public void dispose() {
        super.dispose();
        this.unloadAssets();
        stopWaveTimer();
    }

    private void spawnScrap() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

        for (int i = 0; i < 5; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity scrap = DropFactory.createScrapDrop();
            spawnEntityAt(scrap, randomPos, true, false);
        }

        for (int i = 0; i < 5; i++) {
            GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
            Entity crystal = DropFactory.createCrystalDrop();
            spawnEntityAt(crystal, randomPos, true, false);
        }
    }

    /**
     * Creates the scanners (one per lane) that detect absence of towers and presence of mobs,
     * and trigger engineer spawning
     */
    private void spawnGapScanners() {
        for (int i = 0; i < terrain.getMapBounds(0).y; i++) {
            Entity scanner = GapScannerFactory.createScanner();
            spawnEntityAt(scanner, new GridPoint2(0, i), true, true);
        }
    }


}
