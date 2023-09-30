package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.components.TouchAttackComponent;
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
import java.util.Random;
import java.util.Timer;
import static com.csse3200.game.screens.AssetLoader.loadAllAssets;
import java.util.TimerTask;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_BUILDINGS = 4;
  private static final int NUM_GHOSTS = 0;
  private static final int NUM_GRUNTS = 5;
  private static final int NUM_BOSS = 4;
  private AssetLoader assetLoader;

  private static final int NUM_MOBBOSS2=3;
  private static final int NUM_MOBBOSS1=1;

  private Random random = new Random();

  private int wave = 0;
  private Timer waveTimer;

  private static final int NUM_WEAPON_TOWERS = 3;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(2, 4);
  // Temporary spawn point for testing
  private static final float WALL_WIDTH = 0.1f;

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
  public void setAssetLoader(AssetLoader assetLoader) {
    this.assetLoader = assetLoader;
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
        spawnSkeleton();
        spawnWizard();
        // mobBoss2 = spawnMobBoss2();
        break;
      case 4:
        spawnWaterQueen();
        spawnWaterSlime();
        // mobBoss2 = spawnMobBoss2();
        
        break;
      case 5:
        spawnDemonBoss();
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
    loadAllAssets();
    logger.info("Lol");
    displayUI();
    logger.info("Lol");
    spawnTerrain();
    logger.info("Lol");
    
    // Set up infrastructure for end game tracking
    player = spawnPlayer();
    player.getEvents().addListener("spawnWave", this::spawnXenoGrunts);

    spawnProjectile(new Vector2(0, 10), PhysicsLayer.NPC, towardsMobs, new Vector2(2f, 2f));
    player.getEvents().addListener("spawnWave", this::spawnWave);
    spawnXenoGrunts();
    startWaveTimer();
    spawnScrap();
    spawnDeflectXenoGrunt(15, 5);
    spawnSplittingXenoGrunt(15, 4);
    spawnScrap();
    spawnTNTTower();
    spawnWeaponTower();
    spawnGapScanners();
    spawnDroidTower();

  }
  
  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("Box Forest"));
    ui.addComponent(ServiceLocator.getGameEndService().getDisplay());
    ui.addComponent(ServiceLocator.getCurrencyService().getDisplay());
    spawnEntity(ui);
  }
  
  private void spawnTerrain() {

    terrain = terrainFactory.createTerrain(TerrainType.ALL_DEMO);
    // TODO: We might need a MapService
    Entity entity = new Entity().addComponent(terrain);
    spawnEntity(entity);
    
    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);
    
    // Left
    spawnEntityAt(
            ObstacleFactory.createWall(WALL_WIDTH, worldBounds.y),
            new GridPoint2(0, 2),
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
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH * 7),
            new GridPoint2(0, tileBounds.y),
            false,
            false);
    // Bottom
    spawnEntityAt(
            ObstacleFactory.createWall(worldBounds.x, WALL_WIDTH ),
            new GridPoint2(0, 2),
            false,
            false);
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
  
  // Spawn player at a specific position
  private Entity spawnPlayer(GridPoint2 position) {
    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntityAt(newPlayer, position, true, true);
    return newPlayer;
  }

  private void spawnDemonBoss() {
    Entity demon = MobBossFactory.createDemonBoss();
    spawnEntityAt(demon, new GridPoint2(19, 5), true, false);
  }

  private void spawnPatrick() {
    Entity patrick = MobBossFactory.createPatrickBoss(2500);
    spawnEntityAt(patrick, new GridPoint2(18, 5), true, false);
  }

  private Entity spawnMobBoss1() {
    int[] pickedLanes = random.ints(0, 8)
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
  
  private void spawnXenoGrunts() {
    int[] pickedLanes = random.ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      Entity xenoGrunt = NPCFactory.createXenoGrunt();
      xenoGrunt.setScale(1.5f, 1.5f);
      spawnEntityAt(xenoGrunt, randomPos, true, false);
    }
  }

  // * TEMPORARY FOR TESTING
  private void spawnSplittingXenoGrunt(int x, int y) {
    GridPoint2 pos = new GridPoint2(x, y);
    Entity xenoGrunt = NPCFactory.createSplittingXenoGrunt();
    xenoGrunt.setScale(1.5f, 1.5f);
    spawnEntityAt(xenoGrunt, pos, true, true);
  }

  // * TEMPORARY FOR TESTING
  private void spawnDodgingDragonKnight(int x, int y) {
    GridPoint2 pos = new GridPoint2(x, y);
    Entity fireworm = NPCFactory.createDodgingDragonKnight();
    fireworm.setScale(1.5f, 1.5f);
    spawnEntityAt(fireworm, pos, true, true);
  }

  // * TEMPORARY FOR TESTING
  private void spawnDeflectXenoGrunt(int x, int y) {
    GridPoint2 pos = new GridPoint2(x, y);
    Entity xenoGrunt = NPCFactory.createDeflectXenoGrunt();
    xenoGrunt.setScale(1.5f, 1.5f);
    spawnEntityAt(xenoGrunt, pos, true, true);
  }
  
  private void spawnFireWorm() {
    int[] pickedLanes = random.ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      Entity fireWorm = NPCFactory.createFireWorm();
      fireWorm.setScale(1.5f, 1.5f);
      spawnEntityAt(fireWorm, randomPos, true, false);
    }
  }

  private void spawnSkeleton() {
    int[] pickedLanes = new Random().ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      Entity skeleton = NPCFactory.createSkeleton();
      skeleton.setScale(1.5f, 1.5f);
      spawnEntityAt(skeleton, randomPos, true, false);
    }
  }

  private void spawnDragonKnight() {
    int[] pickedLanes = random.ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      Entity fireWorm = NPCFactory.createDragonKnight();
      fireWorm.setScale(1.5f, 1.5f);
      spawnEntityAt(fireWorm, randomPos, true, false);
    }
  }

  private void spawnWizard() {
    int[] pickedLanes = new Random().ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      Entity wizard = NPCFactory.createWizard();
      wizard.setScale(1.5f, 1.5f);
      spawnEntityAt(wizard, randomPos, true, false);
    }
  }

  private void spawnWaterQueen() {
    int[] pickedLanes = new Random().ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      Entity waterQueen = NPCFactory.createWaterQueen();
      waterQueen.setScale(1.5f, 1.5f);
      spawnEntityAt(waterQueen, randomPos, true, false);
    }
  }

  private void spawnWaterSlime() {
    int[] pickedLanes = new Random().ints(1, 7)
            .distinct().limit(5).toArray();
    for (int i = 0; i < NUM_GRUNTS; i++) {
      GridPoint2 randomPos = new GridPoint2(19, pickedLanes[i]);
      Entity waterSlime = NPCFactory.createWaterSlime();
      waterSlime.setScale(1.5f, 1.5f);
      spawnEntityAt(waterSlime, randomPos, true, false);
    }
  }
  
  private void spawnWeaponTower() {
    GridPoint2 minPos = new GridPoint2(0, 2);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_WEAPON_TOWERS + 10; i++) {
      GridPoint2 randomPos1 = RandomUtils.random(minPos, maxPos);
      GridPoint2 randomPos2 = RandomUtils.random(minPos, maxPos);
      Entity wallTower = TowerFactory.createWallTower();
      Entity fireTower = TowerFactory.createFireTower();
      Entity stunTower = TowerFactory.createStunTower();
      spawnEntityAt(fireTower, randomPos1, true, true);
      spawnEntityAt(stunTower, randomPos2, true, true);
    }
  }

  // * TEMPORARY FOR TESTING
  private void spawnFireTowerAt(int x, int y) {
    GridPoint2 pos = new GridPoint2(x, y);
    Entity fireTower = TowerFactory.createFireTower();

    spawnEntityAt(fireTower, pos, true, true);
  }
  private void spawnDroidTowerAt(int x, int y) {
    GridPoint2 pos = new GridPoint2(x, y);
    Entity droidTower = TowerFactory.createDroidTower();

    spawnEntityAt(droidTower, pos, true, true);
  }
  
  private void spawnTNTTower() {
    GridPoint2 minPos = new GridPoint2(0, 2);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
    
    for (int i = 0; i < NUM_WEAPON_TOWERS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity weaponTower = TowerFactory.createTNTTower();
      spawnEntityAt(weaponTower, randomPos, true, true);
    }
    
  }

  private void spawnDroidTower() {
    GridPoint2 minPos = new GridPoint2(0, 2);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_WEAPON_TOWERS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity weaponTower = TowerFactory.createDroidTower();
      spawnEntityAt(weaponTower, randomPos, true, true);
    }
  }

  private void unloadAssets() {
    logger.debug("Unloading assets");
    if (assetLoader != null) {
      AssetLoader.unloadAllAssets(); // Use the AssetLoader to unload assets if it's not null
    } else {
      logger.error("AssetLoader is not set. Cannot unload assets.");
    }
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