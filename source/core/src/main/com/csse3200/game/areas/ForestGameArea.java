package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Timer;


import static com.csse3200.game.entities.factories.NPCFactory.createGhost;

import java.util.ArrayList;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);

  private static final int NUM_BUILDINGS = 4;

  private static final int NUM_WALLS = 7;

  private static final int NUM_TREES = 0;
  private static final int NUM_GHOSTS = 0;
  private static final int NUM_GRUNTS = 5;

  private static final int NUM_BOSS=4;

  private Timer bossSpawnTimer;
  private int bossSpawnInterval = 10000; // 1 minute in milliseconds


  private static final int NUM_WEAPON_TOWERS = 3;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(0, 0);
  // Temporary spawn point for testing
  private static final float WALL_WIDTH = 0.1f;

  private static final GridPoint2 BOSS_SPAWN = new GridPoint2(5, 5);

  // Required to load assets before using them
  private static final String[] forestTextures = {
         "images/ingamebg.png",
          "images/projectiles/projectile.png",
          "images/box_boy_leaf.png",
          "images/background/building1.png",
          "images/ghost_1.png",
          "images/grass_2.png",
          "images/grass_3.png",
          "images/hex_grass_1.png",
          "images/background/mountain.png",
          "images/ghost_king.png",
          "images/ghost_1.png",
          "images/terrain 2 normal.png",
          "images/terrain 2 hex.png",
          "images/hex_grass_2.png",
          "images/hex_grass_3.png",
          "images/iso_grass_1.png",
          "images/iso_grass_2.png",
          "images/iso_grass_3.png",
          "images/towers/turret.png",
          "images/towers/turret01.png",
          "images/towers/turret_deployed.png",
          "images/towers/fire_tower_atlas.png",
          "images/towers/stun_tower.png",
          "images/background/building2.png",
          "images/mobs/robot.png",
          "images/mobs/Attack_1.png",
          "images/mobs/Attack_2.png",
          "images/mobs/Charge_1.png",
          "images/mobs/Charge_2.png",
          "images/mobs/Dead.png",
          "images/mobs/Enabling-5.png",
          "images/mobs/satyr.png",
          "images/mobs/Hurt.png",
          "images/mobs/Idle.png",
          "images/mobs/rangeBossRight.png",
          "images/towers/wallTower.png",
          "images/background/building2.png",
          "images/iso_grass_3.png",

          "images/terrain_use.png",
          "images/Dusty_MoonBG.png",

          "images/economy/scrap.png",
          "images/towers/mine_tower.png",
          "images/towers/TNTTower.png",
          "images/towers/DroidTower.png"
  };
  private static final String[] forestTextureAtlases = {
          "images/terrain_iso_grass.atlas",
          "images/ghost.atlas",
          "images/ghostKing.atlas",
          "images/towers/turret.atlas",
          "images/towers/turret01.atlas",
          "images/towers/fire_tower_atlas.atlas",
          "images/towers/stun_tower.atlas",
          "images/mobs/xenoGruntRunning.atlas",
          "images/mobs/robot.atlas",
          "images/mobs/rangeBossRight.atlas",
          "images/towers/DroidTower.atlas",
          "images/xenoGrunt.atlas",
          "images/mobs/robot.atlas",
          "images/mobs/rangeBossRight.atlas",
          "images/towers/TNTTower.atlas",
          "images/projectiles/basic_projectile.atlas",
          "images/projectiles/mobProjectile.atlas"

  };
  private static final String[] forestSounds = {
          "sounds/Impact4.ogg",
          "sounds/towers/gun_shot_trimmed.mp3",
          "sounds/towers/deploy.mp3",
          "sounds/towers/stow.mp3",
          "sounds/engineers/firing_auto.mp3",
          "sounds/engineers/firing_single.mp3"
  };
  private static final String backgroundMusic = "sounds/background/Sci-Fi1.ogg";
  private static final String[] forestMusic = {backgroundMusic};

  private final TerrainFactory terrainFactory;

  private Entity player;
  
  // Variables to be used with spawn projectile methods. This is the variable 
  // that should occupy the direction param.
  private static final int towardsMobs = 100;
  private static final int towardsTowers = 0;
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
    // Load game assets
    loadAssets();
    displayUI();
    spawnTerrain();

    player = spawnPlayer();
    player.getEvents().addListener("spawnWave", this::spawnXenoGrunts);

    playMusic();

    // Types of projectile

    spawnEffectProjectile(new Vector2(0, 10), PhysicsLayer.HUMANS, towardsMobs, new Vector2(2f, 2f), ProjectileEffects.BURN, true);

    spawnXenoGrunts();

    spawnGhosts();
    spawnWeaponTower();



    spawnIncome();

    spawnDroidTower();

    playMusic();

    spawnEngineer();
    bossKing1 = spawnBossKing1();

    spawnTNTTower();

  }

  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    ui.addComponent(ServiceLocator.getCurrencyService().getDisplay());
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

  // Spawn player at a specific position
  private Entity spawnPlayer(GridPoint2 position) {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, position, true, true);
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
    GridPoint2 randomPos 
     = new GridPoint2(0, 0);
    Entity ghostKing = NPCFactory.createGhostKing(player);
    spawnEntityAt(ghostKing, randomPos, true, true);
    return ghostKing;
  }

    /**
    * Spawns a projectile that only heads towards the enemies in its lane.
    * 
    * @param position The position of the Entity that's shooting the projectile.
    * @param targetLayer The enemy layer of the "shooter".
    * @param direction The direction the projectile should head towards.
    * @param speed The speed of the projectiles.
   * 
   */
  private void spawnProjectile(Vector2 position, short targetLayer, int direction, Vector2 speed) {
    Entity Projectile = ProjectileFactory.createFireBall(targetLayer, new Vector2(direction, position.y), speed);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }
 /**
   * Spawns a projectile specifically for general mobs/xenohunters
   * 
   * @param position The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction The direction the projectile should head towards.
   * @param speed The speed of the projectiles.
   * 
   */
  private void spawnMobBall(Vector2 position, short targetLayer, int direction, Vector2 speed) {
    Entity Projectile = ProjectileFactory.createMobBall(targetLayer, new Vector2(direction, position.y), speed);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }

  /**
    * Spawns a projectile to be used for multiple projectile function.
    * 
    * @param position The position of the Entity that's shooting the projectile.
    * @param targetLayer The enemy layer of the "shooter".
    * @param space The space between the projectiles' destination.
    * @param direction The direction the projectile should head towards.
    * @param speed The speed of the projectiles.
   * 
   */
  private void spawnProjectile(Vector2 position, short targetLayer, int space,  int direction, Vector2 speed) {
    Entity Projectile = ProjectileFactory.createFireBall(targetLayer, new Vector2(direction, position.y + space), speed);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }

  // private Entity spawnBossKing() {
  //   for (int i = 0; i < NUM_BOSS; i++) {
  //     int fixedX = terrain.getMapBounds(0).x - 1; // Rightmost x-coordinate
  //     int randomY = MathUtils.random(0, maxPos.y);
  //     GridPoint2 randomPos = new GridPoint2(fixedX, randomY);
  //     bossKing1 = BossKingFactory.createBossKing1(player);
  //     spawnEntityAt(bossKing1,
  //         randomPos,
  //         true,
  //         false);
  //   }
  //   return bossKing1;

  // }


  private void spawnXenoGrunts() {
    GridPoint2 minPos = terrain.getMapBounds(0).sub(1, 5);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(1, 25);
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = RandomUtils.random(maxPos, minPos);
      System.out.println(randomPos);
      Entity xenoGrunt = NPCFactory.createXenoGrunt(player);
      xenoGrunt.setScale(1.5f, 1.5f);
      spawnEntityAt(xenoGrunt, randomPos, true, true);
    }
  }

//  private Entity spawnGhostKing() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(0, 0);
//    GridPoint2 randomPos
//            = RandomUtils.random(minPos, maxPos);
//    // = new GridPoint2(26, 26);
//    Entity ghostKing = NPCFactory.createGhostKing(player);
//    spawnEntityAt(ghostKing, randomPos, true, true);
//    return ghostKing;
//
//  }

//  private Entity spawnBossKing2() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
//    for (int i = 0; i < NUM_BOSS; i++) {
//      int fixedX = terrain.getMapBounds(0).x - 1; // Rightmost x-coordinate
//      int randomY = MathUtils.random(0, maxPos.y);
//      GridPoint2 randomPos = new GridPoint2(fixedX, randomY);
//      bossKing2 = BossKingFactory.createBossKing2(player);
//      spawnEntityAt(bossKing2,
//              randomPos,
//              true,
//              false);
//    }
//    return bossKing2;
//  }

  /**
   * Creates multiple projectiles that travel simultaneous. They all have same 
   * the starting point but different destinations.
   * 
   * @param position The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction The direction the projectile should head towards.
   * @param space The space between the projectiles' destination.
   * @param speed The speed of the projectiles.
   * @param quantity The amount of projectiles to spawn.
   */

  private void spawnMultiProjectile(Vector2 position, short targetLayer, int direction, int space, Vector2 speed, int quantity) {
    int half = quantity / 2;
    for (int i = 0; i < quantity; i++) {
        spawnProjectile(position, targetLayer, space * half, direction, speed);
        --half;
    }
  }


  /**
   * Returns projectile that can do an area of effect damage
   * 
   * @param position The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction The direction the projectile should head towards.
   * @param speed The speed of the projectiles.
   * @param effect Type of effect.
   * @param aoe Whether it is an aoe projectile.
   */
  private void spawnEffectProjectile(Vector2 position, short targetLayer, int direction, Vector2 speed,
                                     ProjectileEffects effect, boolean aoe) {
    Entity Projectile = ProjectileFactory.createEffectProjectile(targetLayer, new Vector2(direction, position.y), speed, effect, aoe);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }

  private void spawnWeaponTower() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_WEAPON_TOWERS; i++) {
      GridPoint2 randomPos1 = RandomUtils.random(minPos, maxPos);
      GridPoint2 randomPos2 = RandomUtils.random(minPos, maxPos);
      //Entity weaponTower = TowerFactory.createWeaponTower();
      Entity wallTower = TowerFactory.createWallTower();
      Entity fireTower = TowerFactory.createFireTower();
      Entity stunTower = TowerFactory.createStunTower();
      //spawnEntityAt(weaponTower, randomPos, true, true);
      spawnEntityAt(fireTower, randomPos1, true, true);
      spawnEntityAt(stunTower, randomPos2, true, true);
      //spawnEntityAt(wallTower, new GridPoint2(randomPos1.x + 3, randomPos1.y), true, true);
    }
  }

  private void spawnTNTTower() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_WEAPON_TOWERS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity weaponTower = TowerFactory.createTNTTower();
      spawnEntityAt(weaponTower, randomPos, true, true);
    }

  }

  private void spawnDroidTower() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_WEAPON_TOWERS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity weaponTower = TowerFactory.createDroidTower();
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

//  private void spawnScrap() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
//    for (int i = 0; i < 50; i++) {
//      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//      Entity scrap = DropFactory.createScrapDrop();
//      spawnEntityAt(scrap, randomPos, true, false);
//    }
//  }

  private void spawnIncome() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < 5; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity towerfactory = TowerFactory.createIncomeTower();
      spawnEntityAt(towerfactory, randomPos, true, true);
    }
  }
  
  private void spawnEngineer() {

    for (int i = 0; i < terrain.getMapBounds(0).x; i += 3) {
      Entity engineer = EngineerFactory.createEngineer();
      spawnEntityAt(engineer, new GridPoint2(1, i), true, true);
    }
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = new GridPoint2(5, terrain.getMapBounds(0).sub(2, 2).y);
//    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//
//    Entity engineer = EngineerFactory.createEngineer();
//    spawnEntityAt(engineer, randomPos, true, true);
  }
}