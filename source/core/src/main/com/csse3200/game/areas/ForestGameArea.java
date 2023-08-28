package com.csse3200.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.areas.terrain.TerrainFactory.TerrainType;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.ObstacleFactory;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.TowerFactory;
import com.csse3200.game.utils.math.GridPoint2Utils;
import com.csse3200.game.utils.math.RandomUtils;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.gamearea.GameAreaDisplay;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
// import com.badlogic.gdx.utils.Timer;
// import com.badlogic.gdx.utils.Timer.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csse3200.game.entities.factories.ProjectileFactory;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_TREES = 7;
  private static final int NUM_GHOSTS = 2;

  private static final int NUM_WEAPON_TOWERS = 10;
  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(0, 15);
  // Temporary spawn point for testing
  private static final GridPoint2 PLAYER_SPAWN2 = new GridPoint2(15, 15);
  private static final float WALL_WIDTH = 0.1f;

  // Keep track of the positions of the placed towers
  private static final List<GridPoint2> towerPositions = new ArrayList<GridPoint2>();


  // Required to load assets before using them
  private static final String[] forestTextures = {
    "images/aoe_projectile.png",
    "images/projectile.png",
    "images/box_boy_leaf.png",
    "images/tree.png",
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
    "images/turret_deployed.png"
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
  private Entity ghostking;

  private static final int towardsTowers = 0;
  private static final int towardsMobs = 100;

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
    spawnTrees();
    player = spawnPlayer();
    spawnWeaponTower();
    // spawnGhosts();
    ghostking = spawnGhostKing();

    spawnProjectiles();

    playMusic();

    // spawnProjectile(ghostking.getPosition(), player, towardsMobs, new Vector2(3f, 3f));
    // spawnMultiProjectile(player.getPosition(), ghostking, towardsMobs, 20, new Vector2(3f, 3f), 7);

    // For testing purposes:
    spawnAoeProjectile(new Vector2(0, 10), player, towardsMobs, new Vector2(2f, 2f), 1);
    // spawnProjectile(new Vector2(0, 10), player, towardsMobs, new Vector2(2f, 2f));
    // spawnMultiProjectile(new Vector2(0, 10), ghostking, towardsMobs, 20, new Vector2(2f, 2f), 7);
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

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_TREES; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity tree = ObstacleFactory.createTree();
      spawnEntityAt(tree, randomPos, true, false);
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
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_GHOSTS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      Entity ghost = NPCFactory.createGhost(player);
      spawnEntityAt(ghost, randomPos, true, true);
    }
  }

  private Entity spawnGhostKing() {
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
    * @param position Position of the Entity that's shooting the projectile.
    * @param target The enemy entities of the "shooter".
    * @param direction The direction the projectile should head towards.
    * @param speed Speed of the projectiles
    * @param placedPos The position of the tower that's shooting the projectiles.
   * 
   */
  private void spawnProjectile(Vector2 position, Entity target, int direction, Vector2 speed, GridPoint2 placedPos) {
    Entity Projectile = ProjectileFactory.createFireBall(target, new Vector2(direction, position.y), speed);
    Projectile.setPosition(position);
    // spawnEntity(Projectile);
    spawnEntityAt(Projectile, placedPos, true, true);
  }

  /**
    * Spawns a projectile to be used for multiple projectile function.
    * 
    * @param position Position of the Entity that's shooting the projectile.
    * @param target The enemy entities of the "shooter".
    * @param direction The direction the projectile should head towards.
    * @param speed Speed of the projectiles
    * @param placedPos The position of the tower that's shooting the projectiles. 
   */
  
  private void spawnProjectile(Vector2 position, Entity target, int space,  int direction, Vector2 speed, GridPoint2 placedPos) {
    Entity Projectile = ProjectileFactory.createFireBall(target, new Vector2(direction, position.y + space), speed);
    spawnEntityAt(Projectile, placedPos, true, true);
  }

  // ? JUST IN CASE: OLD CODE
  // private void spawnProjectile(Vector2 position, Entity target, int space,  int direction, Vector2 speed) {
  //   Entity Projectile = ProjectileFactory.createFireBall(target, new Vector2(direction, position.y + space), speed);
  //   Projectile.setPosition(position);
  //   spawnEntity(Projectile);
  // }

   /**
    * Returns multiple projectiles that travel simultaneous.
    * 
    * @param position Position of the Entity that's shooting the projectile.
    * @param target The enemy entities of the "shooter".
    * @param direction The direction the projectile should head towards.
    * @param space Space between the projectiles.
    * @param speed Speed of the projectiles
    * @param amount The amount of projectiles to spawn.
    * @param placedPos The position of the tower that's shooting the projectiles.
    */
  private void spawnMultiProjectile(Vector2 position, Entity target, int direction, int space, Vector2 speed, int amount, GridPoint2 placedPos) {
    int half = amount / 2;
    for (int i = 0; i < amount; i++) {
        spawnProjectile(position, target, space * half, direction, speed, placedPos);
        --half;
    }
  }

  /**
   * Returns projectile that can do an area of effect damage
   * @param position Position of the Entity that's shooting the projectile.
   * @param target The enemy entities of the "shooter".
   * @param direction The direction the projectile should head towards.
   * @param speed Speed of the projectiles
   * @param aoeSize Size of the area of effect
   */
  private void spawnAoeProjectile(Vector2 position, Entity target, int direction, Vector2 speed, int aoeSize) {
    Entity Projectile = ProjectileFactory.createAOEFireBall(target, new Vector2(direction, position.y), speed, aoeSize);
    Projectile.setPosition(position);
    spawnEntity(Projectile);
  }

  private void spawnWeaponTower() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    for (int i = 0; i < NUM_WEAPON_TOWERS; i++) {
      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
      towerPositions.add(randomPos);
      Entity weaponTower = TowerFactory.createWeaponTower();
      spawnEntityAt(weaponTower, randomPos, true, true);
    }
  }

  /**
   * Spawns projectile periodically based on the locations of the towers.
   */
  private void spawnProjectiles() {
    // TODO: spawn it based on the event when mobs are detected in a specified range.
    // ! Buggy!
    // * WORKING VERSION
    Timer projectileTimer = new Timer();
    projectileTimer.schedule(new TimerTask() {
        @Override
        public void run() {
            for (GridPoint2 towerPosition : towerPositions) {
                spawnProjectile(new Vector2(0f, 0f), player, towardsMobs, new Vector2(2f, 2f), towerPosition);
                spawnMultiProjectile(new Vector2(0f, 0f), player, towardsMobs, 20, new Vector2(2f, 2f), 3, towerPosition);
            }
          }
        }, 0, 2000); // delay 0ms, repeat every 2000ms (2 seconds)

    // Need to clear tower positions when the user presses exits or the loop will keep spawning
    // towerPositions.clear();
}

  /**
   * WITHOUT TIMER spawn projectile based on the locations of the towers. 
   */
  // private void spawnProjectiles() {
  //   for (GridPoint2 towerPosition : towerPositions) {
  //     // * First param must be in new Vector(0f, 0f)
  //     // * The first param is the x-offset in respect to the center of the tower and the second param is the y-offset in respect to the center of the tower
  //     spawnMultiProjectile(new Vector2(3f, 3f), player, towardsMobs, 20, new Vector2(2f, 2f), 7, towerPosition);
  //     spawnProjectile(new Vector2(0f, 0f), player, towardsMobs, new Vector2(2f, 2f), towerPosition);
  //   }

  //   // Conditions that clears the list of tower positions
  //   towerPositions.clear();
  // }

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
