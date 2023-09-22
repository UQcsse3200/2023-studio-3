package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.components.TouchAttackComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_BUILDINGS = 4;
  private static final int NUM_GHOSTS = 0;
  private static final int NUM_GRUNTS = 5;
  private static final int NUM_BOSS = 4;


  private static final int NUM_MOBBOSS2=3;
  private static final int NUM_MOBBOSS1=1;

  private int wave = 0;
  private Timer waveTimer;

  private Timer bossSpawnTimer;
  private int bossSpawnInterval = 10000; // 1 minute in milliseconds
  private static final int NUM_WEAPON_TOWERS = 3;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2, 4);
  // Temporary spawn point for testing
  private static final float WALL_WIDTH = 0.1f;
  
  // Required to load assets before using them
  private static final String[] forestTextures = {
         "images/desert_bg.png",
          "images/ice_bg.png",
          "images/lava_bg.png",
          "images/projectiles/projectile.png",
          "images/ingamebg.png",
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
          "images/mobs/boss2.png",
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
          "images/economy/crystal.png",
          "images/economy/econ-tower.png",
          "images/projectiles/bossProjectile.png",
          "images/towers/mine_tower.png",
          "images/towers/TNTTower.png",
          "images/towers/DroidTower.png",
          "images/projectiles/basic_projectile.png",
          "images/projectiles/mobProjectile.png",
          "images/projectiles/engineer_projectile.png",
          "images/projectiles/mobBoss_projectile.png",
          "images/projectiles/snow_ball.png",
          "images/projectiles/burn_effect.png",
          "images/projectiles/stun_effect.png",
          "images/projectiles/firework_anim.png",
          "images/projectiles/pierce_anim.png",
          "images/projectiles/snow_ball.png",
          "images/mobboss/demon.png",
          "images/mobboss/demon2.png",
          "images/mobs/fire_worm.png",
          "images/mobs/patrick.png"
  };
  private static final String[] forestTextureAtlases = {
          "images/economy/econ-tower.atlas",
          "images/terrain_iso_grass.atlas",
          "images/ghost.atlas",
          "images/mobs/boss2.atlas",
          "images/ghostKing.atlas",
          "images/towers/turret.atlas",
          "images/towers/turret01.atlas",
          "images/mobs/xenoGrunt.atlas",
          "images/towers/fire_tower_atlas.atlas",
          "images/towers/stun_tower.atlas",
          "images/mobs/xenoGruntRunning.atlas",
          "images/xenoGrunt.atlas",
          "images/mobs/robot.atlas",
          "images/mobs/rangeBossRight.atlas",
          "images/towers/DroidTower.atlas",
          "images/mobs/robot.atlas",
          "images/mobs/rangeBossRight.atlas",
          "images/towers/TNTTower.atlas",
          "images/projectiles/basic_projectile.atlas",
          "images/projectiles/bossProjectile.atlas",
          "images/projectiles/mobProjectile.atlas",
          "images/projectiles/mobProjectile.atlas",
          "images/projectiles/engineer_projectile.atlas",
          "images/projectiles/mobBoss_projectile.atlas",
          "images/projectiles/snow_ball.atlas",
          "images/projectiles/pierce_anim.atlas",
          "images/projectiles/burn_effect.atlas",
          "images/projectiles/firework_anim.atlas",
          "images/projectiles/mobProjectile.atlas",
          "images/projectiles/stun_effect.atlas",
          "images/mobboss/demon.atlas",
          "images/mobs/fire_worm.atlas",
          "images/mobs/dragon_knight.atlas",
          "images/mobs/patrick.atlas"
  };
  private static final String[] forestSounds = {
          "sounds/Impact4.ogg",
          "sounds/economy/click.wav",
          "sounds/economy/click_1.wav",
          "sounds/towers/gun_shot_trimmed.mp3",
          "sounds/towers/deploy.mp3",
          "sounds/towers/stow.mp3",
          "sounds/engineers/firing_auto.mp3",
          "sounds/engineers/firing_single.mp3",
          "sounds/projectiles/on_collision.mp3",
          "sounds/projectiles/explosion.mp3"
  };
  private static final String backgroundMusic = "sounds/background/Sci-Fi1.ogg";
  private static final String[] forestMusic = {backgroundMusic};
  
  private final TerrainFactory terrainFactory;
  
  private Entity player;
  
  // Variables to be used with spawn projectile methods. This is the variable
  // that should occupy the direction param.
  private static final int towardsMobs = 100;
  private Entity mobBoss2;
  private Entity mobBoss1;

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   *
   * @param terrainFactory TerrainFactory used to create the terrain for the GameArea.
   * @requires terrainFactory != null
   */
  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  // Add this method to start the wave spawning timer when the game starts.
  private void startWaveTimer() {
    waveTimer = new Timer();
    waveTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        spawnWave();
      }
    }, 0, 10000); // 10000 milliseconds = 10 seconds
  }

  // Add this method to stop the wave timer when the game ends or as needed.
  private void stopWaveTimer() {
    if (waveTimer != null) {
      waveTimer.cancel();
      waveTimer = null;
    }
  }

  private void spawnWave() {
    wave++;
    switch (wave) {
      case 1:
      case 2:
        spawnFireWorm();
        spawnDragonKnight();
        break;
      case 3:
        //mobBoss2 = spawnMobBoss2();
        break;
      case 4:
        //mobBoss2 = spawnMobBoss2();
        break;
      default:
        // Handle other wave scenarios if needed
        break;
    }
  }


  /**
   * Create the game area, including terrain, static entities (trees), dynamic entities (player)
   */
  @Override
  public void create() {
    // Load game assets
    loadAssets();
    logger.info("Lol");
    displayUI();
    logger.info("Lol");
    spawnTerrain();
    logger.info("Lol");
    
    // Set up infrastructure for end game tracking
    player = spawnPlayer();
//    player.getEvents().addListener("spawnWave", this::spawnWave);
    
    //playMusic();
    
    // Types of projectile
//    spawnAoeProjectile(new Vector2(0, 10), player, towardsMobs, new Vector2(2f, 2f), 1);
//    spawnProjectile(new Vector2(0, 10), PhysicsLayer.NPC, towardsMobs, new Vector2(2f, 2f));
//    spawnMultiProjectile(new Vector2(0, 10), PhysicsLayer.NPC, towardsMobs, 20, new Vector2(2f, 2f), 7);
//    spawnEffectProjectile(new Vector2(0, 10), PhysicsLayer.HUMANS, towardsMobs, new Vector2(2f, 2f), ProjectileEffects.BURN, true);
//    spawnPierceFireBall(new Vector2(2, 3), PhysicsLayer.NPC, towardsMobs, new Vector2(2f, 2f));
//    spawnRicochetFireball(new Vector2(2, 4), PhysicsLayer.NPC, towardsMobs, new Vector2(2f, 2f));
//    spawnSplitFireWorksFireBall(new Vector2(2, 5), PhysicsLayer.NPC, towardsMobs, new Vector2(2f, 2f), 12);
//    spawnEffectProjectile(new Vector2(2, 6), PhysicsLayer.NPC, towardsMobs, new Vector2(2f, 2f), ProjectileEffects.SLOW, false);
//    spawnXenoGrunts();
//    spawnWeaponTower();

//    spawnDragonKnight();
//    spawnFireWorm();
    spawnDemonBoss();

    //mobBoss1 = spawnMobBoss1();
  //  startWaveTimer();
////    spawnIncome();
//    spawnScrap();
//    spawnTNTTower();
//
//    spawnGapScanners();
//    spawnDroidTower();
//
//    mobBoss2 = spawnMobBoss2();

  }
  
  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    ui.addComponent(ServiceLocator.getGameEndService().getDisplay());
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
    // ! THIS ONE DOESNT WORK. GRIDPOINTS2UTIL.ZERO is (0, 4), not (0, 0)
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y), new GridPoint2(1, 0), false, false);
    // Right
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(tileBounds.x, 0),
            false,
            false);
    // Top
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH * 7),
            new GridPoint2(0, tileBounds.y - 1),
            false,
            false);
    // Bottom
    // spawnEntityAt(
    //         ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH), GridPoint2Utils.ZERO, false, false);
    // * TMPORARY
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH * 7), new GridPoint2(0, 0), false, false);
  }
//  private void spawnBuilding1() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
//    for (int i = 0; i < NUM_BUILDINGS; i++) {
//      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//      Entity building1 = ObstacleFactory.createBuilding1();
//      spawnEntityAt(building1, randomPos, true, false);
//    }
//  }
//  private void spawnBuilding2() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
//    for (int i = 0; i < NUM_BUILDINGS; i++) {
//      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//      Entity building2 = ObstacleFactory.createBuilding2();
//      spawnEntityAt(building2, randomPos, true, false);
//    }
//  }


//  private void spawnMountains() {
//    ArrayList<GridPoint2> fixedPositions = new ArrayList<>(); //Generating ArrayList
//
//    fixedPositions.add(new GridPoint2(5, 8));
//    fixedPositions.add(new GridPoint2(12, 4));
//    fixedPositions.add(new GridPoint2(20, 10));
//    fixedPositions.add(new GridPoint2(33, 17));
//
//    for (GridPoint2 fixedPos : fixedPositions) {
//      Entity tree = ObstacleFactory.createMountain();
//      spawnEntityAt(tree, fixedPos, true, false);
//    }
//    for (int i = 0; i < NUM_BUILDINGS; i++) {
//      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//      Entity building1 = ObstacleFactory.createBuilding1();
//      spawnEntityAt(building1, randomPos, true, false);
//    }
//  }
  
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
    newPlayer.addComponent(new TouchAttackComponent(PhysicsLayer.NPC));
    newPlayer.addComponent(new HitboxComponent().setLayer(PhysicsLayer.HUMANS));
    return newPlayer;
  }
  
  // Spawn player at a specific position
  private Entity spawnPlayer(GridPoint2 position) {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, position, true, true);
    return newPlayer;
  }

//  private void spawnGhosts() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(0, 2);
//
//    for (int i = 0; i < NUM_GHOSTS; i++) {
//      int fixedX = terrain.getMapBounds(0).x - 1; // Rightmost x-coordinate
//      int randomY = MathUtils.random(0, maxPos.y);
//      GridPoint2 randomPos = new GridPoint2(fixedX, randomY);
//      Entity ghost = createGhost(player);
//      spawnEntityAt(ghost, randomPos, true, true);
//    }
//  }

//  private Entity spawnMobBoss1() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//    GridPoint2 randomPos
//     = new GridPoint2(0, 0);
//    Entity ghostKing = NPCFactory.createGhostKing(player);
//    spawnEntityAt(ghostKing, randomPos, true, true);
//    return ghostKing;
//  }

  private void spawnDemonBoss() {
    Entity demon = MobBossFactory.createDemonBoss();
    spawnEntityAt(demon, new GridPoint2(19, 4), true, false);
  }

  private Entity spawnMobBoss1() {
    int[] pickedLanes = new Random().ints(0, 8)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_MOBBOSS1; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      mobBoss1 = MobBossFactory.createMobBoss1(pickedLanes[i]);
      spawnEntityAt(mobBoss1,
              randomPos,
              true,
              false);
    }
    return mobBoss1;
  }


  /**
   * Spawns a projectile that only heads towards the enemies in its lane.
   *
   * @param position    The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction   The direction the projectile should head towards.
   * @param speed       The speed of the projectiles.
   */
  private void spawnProjectile(Vector2 position, short targetLayer, int direction, Vector2 speed) {
    Entity Projectile = ProjectileFactory.createFireBall(targetLayer, new Vector2(direction, position.y), speed);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }
  
  /**
   * Spawns a projectile specifically for general mobs/xenohunters
   *
   * @param position    The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction   The direction the projectile should head towards.
   * @param speed       The speed of the projectiles.
   */
  private void spawnProjectileTest(Vector2 position, short targetLayer, int direction, Vector2 speed) {
    Entity Projectile = ProjectileFactory.createEngineerBullet(targetLayer, new Vector2(direction, position.y), speed);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }
  
  /**
   * Spawns a projectile to be used for multiple projectile function.
   *
   * @param position    The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param space       The space between the projectiles' destination.
   * @param direction   The direction the projectile should head towards.
   * @param speed       The speed of the projectiles.
   */
  private void spawnProjectile(Vector2 position, short targetLayer, int space, int direction, Vector2 speed) {
    Entity Projectile = ProjectileFactory.createFireBall(targetLayer, new Vector2(direction, position.y + space), speed);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }


//   private Entity spawnMobBoss() {
//     for (int i = 0; i < NUM_BOSS; i++) {
//       int fixedX = terrain.getMapBounds(0).x - 1; // Rightmost x-coordinate
//       int randomY = MathUtils.random(0, maxPos.y);
//       GridPoint2 randomPos = new GridPoint2(fixedX, randomY);
//       mobBoss1 = MobBossFactory.createMobBoss1(player);
//       spawnEntityAt(mobBoss1,
//           randomPos,
//           true,
//           false);
//     }
//     return mobBoss1;
//
//   }
  
  
  private void spawnXenoGrunts() {
    int[] pickedLanes = new Random().ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      System.out.println(randomPos);
      Entity xenoGrunt = NPCFactory.createXenoGrunt(player);
      xenoGrunt.setScale(1.5f, 1.5f);
      spawnEntityAt(xenoGrunt, randomPos, true, false);
    }
  }

  private void spawnFireWorm() {
    int[] pickedLanes = new Random().ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      System.out.println(randomPos);
      Entity fireWorm = NPCFactory.createFireWorm(player);
      fireWorm.setScale(1.5f, 1.5f);
      spawnEntityAt(fireWorm, randomPos, true, false);
    }
  }

  private void spawnDragonKnight() {
    int[] pickedLanes = new Random().ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      System.out.println(randomPos);
      Entity fireWorm = NPCFactory.createDragonKnight(player);
      fireWorm.setScale(1.5f, 1.5f);
      spawnEntityAt(fireWorm, randomPos, true, false);
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

//  private Entity spawnMobBoss2() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
//    for (int i = 0; i < NUM_BOSS; i++) {
//      int fixedX = terrain.getMapBounds(0).x - 1; // Rightmost x-coordinate
//      int randomY = MathUtils.random(0, maxPos.y);
//      GridPoint2 randomPos = new GridPoint2(fixedX, randomY);
//      mobBoss2 = MobBossFactory.createMobBoss2(player);
//      spawnEntityAt(mobBoss2,
//              randomPos,
//              true,
//              false);
//    }
//    return mobBoss2;
//  }
  
  private Entity spawnMobBoss2() {
    int[] pickedLanes = new Random().ints(0, 8)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_MOBBOSS2; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      mobBoss2 = MobBossFactory.createMobBoss2();
      spawnEntityAt(mobBoss2,
              randomPos,
              true,
              false);
    }
    return mobBoss2;
  }
  
  /**
   * Creates multiple projectiles that travel simultaneous. They all have same
   * the starting point but different destinations.
   *
   * @param position    The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction   The direction the projectile should head towards.
   * @param space       The space between the projectiles' destination.
   * @param speed       The speed of the projectiles.
   * @param quantity    The amount of projectiles to spawn.
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
   * @param position    The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction   The direction the projectile should head towards.
   * @param speed       The speed of the projectiles.
   * @param effect      Type of effect.
   * @param aoe         Whether it is an aoe projectile.
   */
  private void spawnEffectProjectile(Vector2 position, short targetLayer, int direction, Vector2 speed,
                                     ProjectileEffects effect, boolean aoe) {
    Entity Projectile = ProjectileFactory.createEffectProjectile(targetLayer, new Vector2(direction, position.y), speed, effect, aoe);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }
  
  /**
   * Spawns a pierce fireball.
   * Pierce fireball can go through targetlayers without disappearing but damage
   * will still be applied.
   *
   * @param position    The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction   The direction the projectile should head towards.
   * @param speed       The speed of the projectiles.
   */
  private void spawnPierceFireBall(Vector2 position, short targetLayer, int direction, Vector2 speed) {
    Entity projectile = ProjectileFactory.createPierceFireBall(targetLayer, new Vector2(direction, position.y), speed);
    projectile.setPosition(position);
    spawnEntity(projectile);
  }
  
  /**
   * Spawns a ricochet fireball
   * Ricochet fireballs bounce off targets with a specified maximum count of 3
   * Possible extensions: Make the bounce count flexible with a param.
   *
   * @param position    The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction   The direction the projectile should head towards.
   * @param speed       The speed of the projectiles.
   */
  private void spawnRicochetFireball(Vector2 position, short targetLayer, int direction, Vector2 speed) {
    // Bounce count set to 0.
    Entity projectile = ProjectileFactory.createRicochetFireball(targetLayer, new Vector2(direction, position.y), speed, 0);
    projectile.setPosition(position);
    spawnEntity(projectile);
  }
  
  /**
   * Spawns a split firework fireball.
   * Splits into mini projectiles that spreads out after collision.
   *
   * @param position    The position of the Entity that's shooting the projectile.
   * @param targetLayer The enemy layer of the "shooter".
   * @param direction   The direction the projectile should head towards.
   * @param speed       The speed of the projectiles.
   * @param amount      The amount of projectiles appearing after collision.
   */
  private void spawnSplitFireWorksFireBall(Vector2 position, short targetLayer, int direction, Vector2 speed, int amount) {
    Entity projectile = ProjectileFactory.createSplitFireWorksFireball(targetLayer, new Vector2(direction, position.y), speed, amount);
    projectile.setPosition(position);
    spawnEntity(projectile);
  }
  
  private void spawnWeaponTower() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
    
    for (int i = 0; i < NUM_WEAPON_TOWERS; i++) {
      GridPoint2 randomPos1 = RandomUtils.random(minPos, maxPos);
      GridPoint2 randomPos2 = RandomUtils.random(minPos, maxPos);
      Entity wallTower = TowerFactory.createWallTower();
      Entity fireTower = TowerFactory.createFireTower();
      Entity stunTower = TowerFactory.createStunTower();
      spawnEntityAt(fireTower, randomPos1, true, true);
      spawnEntityAt(stunTower, randomPos2, true, true);
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
  
  private void spawnIncome() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
    
    for (int i = 0; i < 50; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity towerfactory = TowerFactory.createIncomeTower();
      spawnEntityAt(towerfactory, randomPos, true, true);
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