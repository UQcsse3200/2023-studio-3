package com.csse3200.game.entities.factories;

import com.badlogic.gdx.math.MathUtils;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.WaveClass;
import com.csse3200.game.components.tasks.waves.WaveTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.screens.GameLevelData;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class WaveFactory {
  // CONSTANTS
  private static final String SKELETON = "Skeleton";
  private static final String COAT = "Coat";
  private static final String WATER_QUEEN = "WaterQueen";
  private static final String SPLITTING_WATER_SLIME = "SplittingWaterSlime";
  private static final String ARCANE_ARCHER = "ArcaneArcher";
  private static final String WIZARD = "Wizard";
  private static final String SPLITTING_NIGHT_BORNE = "SplittingNightBorne";
  private static final String DRAGON_KNIGHT = "DragonKnight";
  private static final String NECROMANCER = "Necromancer";
  private static final String SPLITTING_ROCKY = "SplittingRocky";
  private static final String FIRE_WORM = "FireWorm";
  private static final String DODGING_DRAGON = "DodgingDragon";
  private static final String DEFLECT_FIRE_WIZARD = "DeflectFireWizard";
  private static final String ICE_BOSS = "IceBoss";
  private static final String PATRICK_BOSS = "PatrickBoss";
  private static final String FIRE_BOSS = "FireBoss";

  private static final Logger logger = LoggerFactory.getLogger(WaveFactory.class);
  private static final ArrayList<String> MELEE_MOBS = new ArrayList<>(Arrays.asList(
          SKELETON, COAT, DRAGON_KNIGHT, NECROMANCER
  ));

  private static final ArrayList<ArrayList<String>> LVL1_STRUCTURE = new ArrayList<>(Arrays.asList(
          new ArrayList<>(Arrays.asList(COAT
          )), new ArrayList<>(Arrays.asList(COAT, WATER_QUEEN
          )), new ArrayList<>(Arrays.asList(WATER_QUEEN, SPLITTING_WATER_SLIME
          )), new ArrayList<>(Arrays.asList(COAT, WATER_QUEEN, SPLITTING_WATER_SLIME
          ))
  ));

  private static final ArrayList<ArrayList<String>> LVL2_STRUCTURE = new ArrayList<>(Arrays.asList(
          new ArrayList<>(Arrays.asList(SKELETON
          )), new ArrayList<>(Arrays.asList(SKELETON, ARCANE_ARCHER
          )), new ArrayList<>(Arrays.asList(SKELETON, WIZARD
          )), new ArrayList<>(Arrays.asList(SKELETON, SPLITTING_NIGHT_BORNE
          )), new ArrayList<>(Arrays.asList(WIZARD, SPLITTING_NIGHT_BORNE
          )), new ArrayList<>(Arrays.asList(SPLITTING_NIGHT_BORNE, SKELETON
          )), new ArrayList<>(Arrays.asList(WIZARD, SPLITTING_NIGHT_BORNE
          )), new ArrayList<>(Arrays.asList(ARCANE_ARCHER, SPLITTING_NIGHT_BORNE, WIZARD
          )), new ArrayList<>(Arrays.asList(SKELETON, ARCANE_ARCHER, WIZARD, SPLITTING_NIGHT_BORNE
          ))
  ));

  private static final ArrayList<ArrayList<String>> LVL3_STRUCTURE = new ArrayList<>(Arrays.asList(
          new ArrayList<>(Arrays.asList(NECROMANCER
          )), new ArrayList<>(Arrays.asList(NECROMANCER, DODGING_DRAGON
          )), new ArrayList<>(Arrays.asList(NECROMANCER, FIRE_WORM
          )), new ArrayList<>(Arrays.asList(NECROMANCER, DEFLECT_FIRE_WIZARD
          )), new ArrayList<>(Arrays.asList(DEFLECT_FIRE_WIZARD, FIRE_WORM
          )), new ArrayList<>(Arrays.asList(DODGING_DRAGON, FIRE_WORM
          )), new ArrayList<>(Arrays.asList(DODGING_DRAGON, NECROMANCER
          )), new ArrayList<>(Arrays.asList(FIRE_WORM, NECROMANCER
          )), new ArrayList<>(Arrays.asList(DEFLECT_FIRE_WIZARD, NECROMANCER
          )), new ArrayList<>(Arrays.asList(DODGING_DRAGON, DEFLECT_FIRE_WIZARD, NECROMANCER
          )), new ArrayList<>(Arrays.asList(FIRE_WORM, NECROMANCER, DODGING_DRAGON
          )), new ArrayList<>(Arrays.asList(FIRE_WORM, SPLITTING_ROCKY, NECROMANCER
          )), new ArrayList<>(Arrays.asList(SPLITTING_ROCKY, DEFLECT_FIRE_WIZARD, FIRE_WORM
          )), new ArrayList<>(Arrays.asList(DEFLECT_FIRE_WIZARD, SPLITTING_ROCKY, NECROMANCER, DODGING_DRAGON, FIRE_WORM
          ))
  ));

  /**
   * The function will create the waves depending on the level selected by the user.
   */
  public static Entity createWaves() {
    int chosenLevel = GameLevelData.getSelectedLevel();
    int difficulty;
    switch (chosenLevel) {
      case 0:
        difficulty = 2;
        break;
      case 2:
        difficulty = 3;
        break;
      default:
        difficulty = 1;
    }

    LevelWaves level = createLevel(difficulty);
    AITaskComponent aiComponent = new AITaskComponent().addTask(new WaveTask());
    return level.addComponent(aiComponent);
  }

  /**
   * This function is responsible for creating the level and all the waves associated with it.
   * It takes in the difficulty, number of waves and level selected by the user. From the level
   * selected by the user, it will produce the waves for the level.
   *
   * Depending on the level selected (1 easy, 2 medium, 3 hard), the number of waves will increase as well as
   * the number of mobs per wave and the health of the mobs. Based on the level the mobs will change and waves will be
   * constructed from the predefined structures above that ensure more difficult abilities the deeper the wave.
   * Based on the level chosen the health of the mobs will increase at a greater rate. For wave i the
   * health will be increased from BASE_HEALTH to BASE_HEALTH + (I * chosen_level) so the difficulty
   * increases quicker.
   *
   * The last wave of every level is a boss.
   *
   * @param chosenLevel - the level selected by the user
   * @return level - the level constructed with all the waves of mobs
   * */
  public static LevelWaves createLevel(int chosenLevel) {
    // Tell the waveService what the spawn delay for levels will be (for UI team).
    int spawnDelay = 5;
    ServiceLocator.getWaveService().setSpawnDelay(spawnDelay);

    // Create new level entity with spawn delay of 5 seconds
    LevelWaves level = new LevelWaves(spawnDelay);

    // set the possible mobs and boss for the level
    ArrayList<ArrayList<String>> possibleMobs;
    String boss = "";
    int bossHealth;
    int minMobs;
    // Base health of the bosses
    final int LVL1_BOSS_BASE_HEALTH = 500;
    final int LVL2_BOSS_BASE_HEALTH = 1000;
    final int LVL3_BOSS_BASE_HEALTH = 2000;

    switch (chosenLevel) {
      case 2:
        boss = PATRICK_BOSS;
        bossHealth = LVL2_BOSS_BASE_HEALTH;
        possibleMobs = LVL2_STRUCTURE;
        minMobs = 6;
        break;
      case 3:
        boss = FIRE_BOSS;
        bossHealth = LVL3_BOSS_BASE_HEALTH;
        possibleMobs = LVL3_STRUCTURE;
        minMobs = 8;
        break;
      default:
        boss = ICE_BOSS;
        bossHealth = LVL1_BOSS_BASE_HEALTH;
        possibleMobs = LVL1_STRUCTURE;
        minMobs = 5;
        break;
    }

    // Create mxWaves number of waves with mob stats increasing
    int atWave = 1;
    for (ArrayList<String> wave : possibleMobs) {
      HashMap<String, int[]> mobs = new HashMap<>();

      int leftToSort = wave.size() - 1;
      int currentMobs = 0;

      // Add each mob to the wave
      for (String mob: wave) {
        int num;

        // Calculate the number of mobs for the wave
        if (leftToSort == 0) {
          num = minMobs - currentMobs;
        } else {
          num = MathUtils.random(minMobs - currentMobs - (2 * leftToSort) - 2) + 2;
          System.out.println(num + " for " + mob + " at wave " + atWave);
          currentMobs += num;
        }

        // Calculate the health
          int health = 60;
        if (MELEE_MOBS.contains(mob)) {
          // The base health for the different mobs
            health = 80;
        }
        int[] mobStats = {num, health + (atWave * chosenLevel)};
        mobs.put(mob, mobStats);

        leftToSort --;
      }
      minMobs ++;
      level.addWave(new WaveClass(mobs));
      atWave++;
    }

    // Add boss wave
    HashMap<String, int[]> bossMob = new HashMap<>();
    bossMob.put(boss, new int[]{1, bossHealth});

    level.addWave(new WaveClass(bossMob));


    logger.info("Level created: {}", level);
    return level;
  }

  private WaveFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

