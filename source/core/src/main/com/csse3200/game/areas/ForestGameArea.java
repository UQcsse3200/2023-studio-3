package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.*;
import com.badlogic.gdx.audio.Music;

import com.csse3200.game.components.ProjectileEffects;

import com.csse3200.game.services.GameTime;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Timer;

import static com.csse3200.game.screens.AssetLoader.loadAllAssets;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);

  /* Change below to change the number of ms between spawns of engineers in the case */
  private static final long ENGINEER_MIN_SPAWN_INTERVAL = 1000;

  private long lastSpawnTime = 0;

  private int wave = 0;

  private Timer waveTimer;
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
          "images/terrain_use.png",
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
          "images/towers/wall_tower.png",
          "images/background/building2.png",
          "images/iso_grass_3.png",
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
          "images/mobboss/patrick.png",
          "images/towers/fireworks_tower.png",
          "images/towers/barrier.png",
          "images/towers/wall_tower.png",
          "images/towers/PierceTower.png",
          "images/towers/RicochetTower.png",
          "images/GrassTile/grass_tile_1.png",
          "images/GrassTile/grass_tile_2.png",
          "images/GrassTile/grass_tile_3.png",
          "images/GrassTile/grass_tile_4.png",
          "images/GrassTile/grass_tile_5.png",
          "images/GrassTile/grass_tile_6.png",
          "images/GrassTile/grass_tile_7.png",
          "images/mobboss/iceBaby.png",
          "images/bombship/bombship.png"
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
          "images/projectiles/engineer_projectile.atlas",
          "images/projectiles/mobBoss_projectile.atlas",
          "images/projectiles/snow_ball.atlas",
          "images/projectiles/pierce_anim.atlas",
          "images/projectiles/burn_effect.atlas",
          "images/projectiles/firework_anim.atlas",
          "images/projectiles/stun_effect.atlas",
          "images/mobboss/demon.atlas",
          "images/mobs/fire_worm.atlas",
          "images/mobs/dragon_knight.atlas",
          "images/mobs/skeleton.atlas",
          "images/mobs/wizard.atlas",
          "images/mobs/water_queen.atlas",
          "images/mobs/water_slime.atlas",
          "images/mobboss/patrick.atlas",
          "images/towers/fireworks_tower.atlas",
          "images/towers/barrier.atlas",
          "images/towers/PierceTower.atlas",
          "images/towers/RicochetTower.atlas",
          "images/mobboss/iceBaby.atlas",
          "images/bombship/bombship.atlas",
          "images/mobs/coat.atlas",
          "images/mobs/night_borne.atlas",
          "images/mobs/arcane_archer.atlas",
          "images/mobs/necromancer.atlas",
          "images/mobs/firewizard.atlas",
          "images/mobs/rocky.atlas"
  };
  private static final String[] forestSounds = {
          "sounds/ui/Open_Close/NA_SFUI_Vol1_Open_01.ogg",
          "sounds/Impact4.ogg",
          "sounds/economy/click.wav",
          "sounds/economy/click_1.wav",
          "sounds/towers/gun_shot_trimmed.mp3",
          "sounds/towers/deploy.mp3",
          "sounds/towers/stow.mp3",
          "sounds/engineers/firing_auto.mp3",
          "sounds/engineers/firing_single.mp3",
          "sounds/projectiles/on_collision.mp3",
          "sounds/projectiles/explosion.mp3",
          "sounds/waves/wave-start/Wave_Start_Alarm.ogg",
          "sounds/waves/wave-end/Wave_Over_01.ogg",
          "sounds/mobBoss/iceBabySound.mp3",
          "sounds/mobBoss/mobSpawnStomp.mp3",
          "sounds/mobBoss/iceBabyAOE.mp3",
          "sounds/mobs/wizardSpell.mp3",
          "sounds/mobs/waterQueenSpell.mp3",
          "sounds/mobs/boneBreak.mp3",
          "sounds/mobs/fireWormRoar.mp3",
          "sounds/mobBoss/demonBreath.mp3",
          "sounds/mobBoss/demonSpawn.wav",
          "sounds/mobBoss/demonAttack.wav",
          "sounds/mobBoss/demonBreathIn.mp3",
          "sounds/mobBoss/demonLand.mp3",
          "sounds/mobBoss/demonJump.mp3",
          "sounds/mobBoss/demonHeal.mp3",
          "sounds/mobBoss/demonCleave.mp3",
          "sounds/mobBoss/demonDeath.mp3",
          "sounds/mobBoss/slimeySplat.mp3",
          "sounds/mobBoss/slimeJump.mp3",
          "sounds/mobBoss/slimePop.mp3",
          "sounds/mobBoss/patrickAttack.mp3",
          "sounds/mobBoss/patrickAppear.mp3",
          "sounds/mobBoss/patrickScream.mp3",
          "sounds/mobBoss/patrickSpell.mp3",
          "sounds/mobBoss/patrickSpawn.mp3",
          "sounds/mobBoss/patrickCast.mp3",
          "sounds/mobBoss/patrickThunder.mp3",
          "sounds/mobBoss/patrickHit.mp3",
          "sounds/mobBoss/spawnDemonSlime.mp3",
          "sounds/towers/Desert-Eagle-Far-Single-Gunshot.mp3",
          "sounds/towers/5.56_single_shot.mp3",
          "sounds/towers/explosion.mp3",
          "sounds/towers/eco_tower_ping.mp3",
          "sounds/towers/ar15_single_shot_far.mp3",
          "sounds/mobs/skeletonHit.mp3",
          "sounds/mobs/coatAttack.mp3",
          "sounds/mobs/archerArrow.mp3"
  };
  private static final String BACKGROUND_MUSIC = "sounds/background/Sci-Fi1.ogg";

  private static final String[] forestMusic = {BACKGROUND_MUSIC};

  /**
   * Initialise this ForestGameArea to use the provided TerrainFactory.
   * &#064;requires  terrainFactory != null
   */
  public ForestGameArea() {
    super();
  }

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
   * Create the game area, including terrain, static entities (trees), dynamic entities (player)
   */
  @Override
  public void create() {
    // Load game assets

    loadAllAssets();
    loadAssets();
    logger.debug("selected towers in main game are " + ServiceLocator.getTowerTypes());
    displayUI();
    spawnTerrain();

    // Set up infrastructure for end game tracking
    logger.info("Creating waves");
    Entity waves = WaveFactory.createWaves();
    spawnEntity(waves);
    waves.getEvents().addListener("spawnWave", this::spawnMob);

    spawnScrap();
    spawnGapScanners();

  }

  private void displayUI() {
    Entity ui = new Entity();
//    ui.addComponent(new GameAreaDisplay("Box Forest"));  TODO: This should be the level name?
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
    switch (entity) {
      case "Xeno":
        mob = NPCFactory.createXenoGrunt(health);
        break;
      case "SplittingWaterSlime":
        mob = NPCFactory.createSplittingWaterSlime(health);
        break;
      case "DodgingDragon":
        mob = NPCFactory.createDodgingDragonKnight(health);
        break;
      case "FireWorm":
        mob = NPCFactory.createFireWorm(health);
        break;
      case "Skeleton":
        mob = NPCFactory.createSkeleton(health);
        break;
      case "Wizard":
        mob = NPCFactory.createWizard(health);
        break;
      case "WaterQueen":
        mob = NPCFactory.createWaterQueen(health);
        break;
      case "FireBoss":
        mob = MobBossFactory.createDemonBoss(health);
        break;
      case "IceBoss":
        mob = MobBossFactory.createIceBoss(health);
        break;
      case "Coat":
        mob = NPCFactory.createCoat(health);
        break;
      case "NightBorne":
        mob = NPCFactory.createNightBorne(health);
        break;
      case "SplittingNightBorne":
        mob = NPCFactory.createSplittingNightBorne(health);
        break;
      case "ArcaneArcher":
        mob = NPCFactory.createDodgingArcaneArcher(health);
        break;
      case "PatrickBoss":
        mob = MobBossFactory.createPatrickBoss(health);
        break;
      default:
        mob = NPCFactory.createXenoGrunt(health);
        break;
    }

    if (entity.contains("Boss")) {
      mob.scaleHeight(5f);
      mob.scaleWidth(5f);
    } else {
      mob.setScale(1.5f, 1.5f);
    }
    spawnEntityAt(mob, randomPos, true, false);
    
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
    GameTime gameTime = ServiceLocator.getTimeSource();
    long currSpawnTime = gameTime.getTime();

    long diff = currSpawnTime - this.lastSpawnTime;
    if (diff < ENGINEER_MIN_SPAWN_INTERVAL) {
      return;
    }

    for (int i = 0; i < terrain.getMapBounds(0).y; i++) {
      Entity scanner = GapScannerFactory.createScanner();
      spawnEntityAt(scanner, new GridPoint2(0, i), true, true);
    }
    this.lastSpawnTime = currSpawnTime;
  }


}
