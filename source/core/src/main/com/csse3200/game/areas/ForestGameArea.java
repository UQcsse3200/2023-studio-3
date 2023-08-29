package com.csse3200.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Timer;
import java.util.TimerTask;


import static com.csse3200.game.entities.factories.NPCFactory.createGhost;

import com.csse3200.game.entities.factories.ProjectileFactory;
import java.util.ArrayList;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_BUILDINGS = 4;
  private static final int NUM_GHOSTS = 0;
  private static final int NUM_BOSS=4;
  private static final int NUM_WALLS = 7;
  private Timer bossSpawnTimer;
  private int bossSpawnInterval = 10000; // 1 minute in milliseconds



  private static final int NUM_WEAPON_TOWERS = 3;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;

  private static final GridPoint2 BOSS_SPAWN = new GridPoint2(5, 5);

  // Required to load assets before using them
  private static final String[] forestTextures = {
          "images/projectile.png",
          "images/box_boy_leaf.png",
          "images/building1.png",
          "images/ghost_1.png",
          "images/grass_2.png",
          "images/grass_3.png",
          "images/hex_grass_1.png",
          "images/mountain.png",
          "images/ghost_king.png",
          "images/ghost_1.png",
          "images/terrain 2 normal.png",
          "images/terrain 2 hex.png",
          "images/hex_grass_2.png",
          "images/hex_grass_3.png",
          "images/iso_grass_1.png",
          "images/iso_grass_2.png",
          "images/iso_grass_3.png",
          "images/turret.png",
          "images/turret01.png",
          "images/turret_deployed.png",
          "images/building2.png",
          "images/wall.png",
          "images/robot.png",
          "images/Attack_1.png",
          "images/Attack_2.png",
          "images/Charge_1.png",
          "images/Charge_2.png",
          "images/Dead.png",
          "images/Enabling-5.png",
          "images/satyr.png",
          "images/Hurt.png",
          "images/Idle.png",
          "images/rangeBossRight.png",
          "images/wallTower.png",
          "images/building2.png",
          "images/iso_grass_3.png"
  };
  private static final String[] forestTextureAtlases = {
          "images/terrain_iso_grass.atlas",
          "images/ghost.atlas",
          "images/ghostKing.atlas",
          "images/turret.atlas",
          "images/turret01.atlas",
          "images/robot.atlas",
          "images/rangeBossRight.atlas"
  };
  private static final String[] forestSounds = {
          "sounds/Impact4.ogg",
          "sounds/gun_shot_trimmed.mp3",
          "sounds/deploy.mp3",
          "sounds/stow.mp3"
  };
  private static final String backgroundMusic = "sounds/Sci-Fi1.ogg";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  private Entity player;
  private Entity bossKing1;
  private Entity bossKing2;


  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    spawnTerrain();
    spawnBuilding1();
    spawnBuilding2();
    spawnMountains();
    player = spawnPlayer();

    spawnGhosts();
    spawnWeaponTower();

    bossKing1 = spawnBossKing1();
    bossKing2 = spawnBossKing2();
//    spawnWall();

    //playMusic();

    spawnProjectile(new Vector2(3f, 3f));
    spawnMultiProjectile(new Vector2(3f, 3f));
  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO);
    spawnEntity(new Entity().addComponent(terrain));

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    // Left
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), GridPoint2Utils.ZERO, false, false);
    // Right
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0),
            false,
            false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH),
            new GridPoint2(0, tileBounds.y),
            false,
            false);
    // Bottom
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);
  }
private void spawnBuilding1() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_BUILDINGS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity building1 = ObstacleFactory.createBuilding1();
      spawnEntityAt(building1, randomPos, true, false);
    }
  }
  private void spawnBuilding2() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_BUILDINGS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity building2 = ObstacleFactory.createBuilding2();
      spawnEntityAt(building2, randomPos, true, false);
    }
  }

  private void spawnMountains() {
    ArrayList<GridPoint2> fixedPositions = new ArrayList<>(); //Generating ArrayList

    fixedPositions.add(new GridPoint2(5, 8));
    fixedPositions.add(new GridPoint2(12, 4));
    fixedPositions.add(new GridPoint2(20, 10));
    fixedPositions.add(new GridPoint2(33, 17));

    for (GridPoint2 fixedPos : fixedPositions) {
      Entity tree = ObstacleFactory.createMountain();
      spawnEntityAt(tree, fixedPos, true, false);
    }
  }

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnGhosts() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(0, 2);

    for (int i = 0; i < NUM_GHOSTS; i++) {
      int fixedX = terrain.getMapBounds(0).x - 1; // Rightmost x-coordinate
      int randomY = MathUtils.random(0, maxPos.y);
      GridPoint2 randomPos = new GridPoint2(fixedX, randomY);
      Entity ghost = createGhost(player);
      spawnEntityAt(ghost, randomPos, true, true);
    }
  }
  
  private Entity spawnBossKing1() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_BOSS; i++) {
      int fixedX = terrain.getMapBounds(0).x - 1; // Rightmost x-coordinate
      int randomY = MathUtils.random(0, maxPos.y);
      GridPoint2 randomPos = new GridPoint2(fixedX, randomY);
      bossKing1 = BossKingFactory.createBossKing1(player);
      spawnEntityAt(bossKing1,
              randomPos,
              true,
              false);
    }
      return bossKing1;
  }

  private Entity spawnBossKing2() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_BOSS; i++) {
      int fixedX = terrain.getMapBounds(0).x - 1; // Rightmost x-coordinate
      int randomY = MathUtils.random(0, maxPos.y);
      GridPoint2 randomPos = new GridPoint2(fixedX, randomY);
      bossKing2 = BossKingFactory.createBossKing2(player);
      spawnEntityAt(bossKing2,
              randomPos,
              true,
              false);
    }
    return bossKing2;
}

  /**
   * Spawns a projectile currently just in the center of the game
   *
   * @return a new projectile
   */
  private void spawnProjectile(Vector2 speed) {
    Entity newProjectile = ProjectileFactory.createProjectile(bossKing1, player, new Vector2(100, bossKing1.getPosition().x), speed);
    newProjectile.setPosition(bossKing1.getPosition());
    spawnEntity(newProjectile);
  }

  private void spawnMultiProjectile(Vector2 speed) {
    Entity newTopProjectile = ProjectileFactory.createProjectile(bossKing1, player, new Vector2(100, player.getPosition().x + 30), speed);
    newTopProjectile.setPosition(player.getPosition());
    Entity newMiddleProjectile = ProjectileFactory.createProjectile(bossKing1, player, new Vector2(100, player.getPosition().x), speed);
    newMiddleProjectile.setPosition(player.getPosition());
    Entity newBottomProjectile = ProjectileFactory.createProjectile(bossKing1, player, new Vector2(100, player.getPosition().x - 30), speed);
    newBottomProjectile.setPosition(player.getPosition());

    spawnEntity(newTopProjectile);
    spawnEntity(newMiddleProjectile);
    spawnEntity(newBottomProjectile);
  }

  private void spawnWeaponTower() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_WEAPON_TOWERS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity weaponTower = TowerFactory.createWeaponTower();
      Entity wallTower = TowerFactory.createWallTower();
      spawnEntityAt(weaponTower, randomPos, true, true);
      spawnEntityAt(wallTower, new GridPoint2(randomPos.x + 3, randomPos.y), true, true);
    }
  }

  private void playMusic() {
    Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    music.setLooping(true);
    music.setVolume(0.3f);
    music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    resourceService.loadSounds(forestSounds);
    resourceService.loadMusic(forestMusic);

    while (!resourceService.loadForMillis(10)) {
      // This could be upgraded to a loading screen
      logger.info("Loading... {}%", resourceService.getProgress());
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.unloadAssets(forestTextures);
    resourceService.unloadAssets(forestTextureAtlases);
    resourceService.unloadAssets(forestSounds);
    resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }

}