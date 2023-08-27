package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
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


/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_BUILDINGS = 4;
  private static final int NUM_GHOSTS = 2;
  private static final int NUM_WALLS = 7;
  private Timer bossSpawnTimer;
  private int bossSpawnInterval = 10000; // 1 minute in milliseconds



  private static final int NUM_WEAPON_TOWERS = 3;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final float WALL_WIDTH = 0.1f;

  // Required to load assets before using them
  private static final String[] forestTextures = {
          "images/projectile.png",
          "images/box_boy_leaf.png",
          "images/building1.png",
          "images/ghost_king.png",
          "images/ghost_1.png",
          "images/grass_1.png",
          "images/grass_2.png",
          "images/grass_3.png",
          "images/hex_grass_1.png",
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
          "images/mud.png"

  };
  private static final String[] forestTextureAtlases = {
          "images/terrain_iso_grass.atlas",
          "images/ghost.atlas",
          "images/ghostKing.atlas",
          "images/turret.atlas",
          "images/turret01.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
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
    player = spawnPlayer();

    spawnGhosts();
    spawnWeaponTower();

    bossKing1 = spawnBossKing1();
    bossKing2 = spawnBossKing2();
    startBossSpawnTimer();
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

  private void startBossSpawnTimer() {
    bossSpawnTimer = new Timer();

    // Schedule the boss spawning task to run every minute
    bossSpawnTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        spawnBosses(); // Call the method to spawn bosses here
      }
    }, 0, bossSpawnInterval);
  }

  private void spawnBosses() {
    // Modify this logic as needed to spawn bosses
    spawnBossKing1();
    spawnBossKing2();
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

  private Entity spawnPlayer() {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, PLAYER_SPAWN, true, true);
    return newPlayer;
  }

  private void spawnGhosts() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_GHOSTS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity ghost = createGhost(player);
      spawnEntityAt(ghost, randomPos, true, true);
    }
  }

  private Entity spawnBossKing1() {
    GridPoint2 bottomRight = terrain.getMapBounds(0).sub(1, 1);  // Subtract 1 to stay within map bounds
    bossKing1 = BossKingFactory.createBossKing1(player);
    spawnEntityAt(bossKing1, bottomRight, true, true);
    return bossKing1;
  }

  private Entity spawnBossKing2() {
    GridPoint2 bottomLeft = new GridPoint2(0, terrain.getMapBounds(0).y - 1);  // Subtract 1 to stay within map bounds
    bossKing2 = BossKingFactory.createBossKing2(player);
    spawnEntityAt(bossKing2, bottomLeft, true, true);
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
      spawnEntityAt(weaponTower, randomPos, true, true);
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
