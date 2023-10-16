package com.csse3200.game.entities.factories;

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
  /**
   * Create a Wave entity.
   * Each wave class represents a single wave, then they are appended to a level.
   * Cases can be written in here to set what happens for each level.
   *
   * @return entity
   */

  private static final Logger logger = LoggerFactory.getLogger(WaveFactory.class);
  private static Random rand = new Random();
  private static final ArrayList<String> MELEE_MOBS = new ArrayList<>(Arrays.asList(
      "Skeleton", "Coat", "DragonKnight", "Necromancer"
  ));

  private static final ArrayList<ArrayList<String>> lvl1Structure = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList("Coat"
      )), new ArrayList<>(Arrays.asList("Coat", "WaterQueen"
      )), new ArrayList<>(Arrays.asList("WaterQueen", "SplittingWaterSlime"
      )), new ArrayList<>(Arrays.asList("Coat", "WaterQueen", "SplittingWaterSlime"
      ))
  ));

  private static final ArrayList<ArrayList<String>> lvl2Structure = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList("Skeleton"
      )), new ArrayList<>(Arrays.asList("Skeleton", "ArcaneArcher"
      )), new ArrayList<>(Arrays.asList("Skeleton", "Wizard"
      )), new ArrayList<>(Arrays.asList("Skeleton", "SplittingNightBorne"
      )), new ArrayList<>(Arrays.asList("Wizard", "SplittingNightBorne"
      )), new ArrayList<>(Arrays.asList("SplittingNightBorne", "Skeleton"
      )), new ArrayList<>(Arrays.asList("Wizard", "SplittingNightBorne"
      )), new ArrayList<>(Arrays.asList("ArcaneArcher", "SplittingNightBorne", "Wizard"
      )), new ArrayList<>(Arrays.asList("Skeleton", "ArcaneArcher", "Wizard", "SplittingNightBorne"
      ))
  ));

  private static final ArrayList<ArrayList<String>> lvl3Structure = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList("Necromancer"
      )), new ArrayList<>(Arrays.asList("Necromancer", "DodgingDragon"
      )), new ArrayList<>(Arrays.asList("Necromancer", "FireWorm"
      )), new ArrayList<>(Arrays.asList("Necromancer", "DeflectFireWizard"
      )), new ArrayList<>(Arrays.asList("DeflectFireWizard", "FireWorm"
      )), new ArrayList<>(Arrays.asList("DodgingDragon", "FireWorm"
      )), new ArrayList<>(Arrays.asList("DodgingDragon", "Necromancer"
      )), new ArrayList<>(Arrays.asList("FireWorm", "Necromancer"
      )), new ArrayList<>(Arrays.asList("DeflectFireWizard", "Necromancer"
      )), new ArrayList<>(Arrays.asList("DodgingDragon", "DeflectFireWizard", "Necromancer"
      )), new ArrayList<>(Arrays.asList("FireWorm", "Necromancer", "DodgingDragon"
      )), new ArrayList<>(Arrays.asList("FireWorm", "SplittingRocky", "Necromancer"
      )), new ArrayList<>(Arrays.asList("SplittingRocky", "DeflectFireWizard", "FireWorm"
      )), new ArrayList<>(Arrays.asList("DeflectFireWizard", "SplittingRocky", "Necromancer", "DodgingDragon", "FireWorm"
      ))
  ));

  private static final String BOSS_1 = "IceBoss";
  private static final String BOSS_2 = "PatrickBoss";
  private static final String BOSS_3 = "FireBoss";

  /**
   * The function will create the waves depending on the level selected by the user.
   * */
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
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WaveTask());
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
    int LVL1_BOSS_BASE_HEALTH = 500;
    int LVL2_BOSS_BASE_HEALTH = 1000;
    int LVL3_BOSS_BASE_HEALTH = 2000;

    switch (chosenLevel) {
      case 2:
        boss = BOSS_2;
        bossHealth = LVL2_BOSS_BASE_HEALTH;
        possibleMobs = lvl2Structure;
        minMobs = 6;
        break;
      case 3:
        boss = BOSS_3;
        bossHealth = LVL3_BOSS_BASE_HEALTH;
        possibleMobs = lvl3Structure;
        minMobs = 8;
        break;
      default:
        boss = BOSS_1;
        bossHealth = LVL1_BOSS_BASE_HEALTH;
        possibleMobs = lvl1Structure;
        minMobs = 5;
        break;
    }

    int totalMobs = 0;
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
          System.out.println(num + " for " + mob + " at wave " + atWave);
        } else {
          num = rand.nextInt(minMobs - currentMobs - (2 * leftToSort) - 2) + 2;
          System.out.println(num + " for " + mob + " at wave " + atWave);
          currentMobs += num;
        }

        // Calculate the health
        int RANGE_BASE_HEALTH = 60;
        int health = RANGE_BASE_HEALTH;
        if (MELEE_MOBS.contains(mob)) {
          // The base health for the different mobs
          int MELEE_BASE_HEALTH = 80;
          health = MELEE_BASE_HEALTH;
        }
        int[] mobStats = {num, health + (atWave * chosenLevel)};
        mobs.put(mob, mobStats);

        leftToSort --;
        totalMobs += num;
      }
      minMobs ++;
      level.addWave(new WaveClass(mobs));
      atWave++;
    }

    // Add boss wave
    HashMap<String, int[]> bossMob = new HashMap<>();
    bossMob.put(boss, new int[]{1, bossHealth});
    totalMobs ++;

    ServiceLocator.getWaveService().setTotalMobs(totalMobs);
    level.addWave(new WaveClass(bossMob));


    logger.info("Level created: {}", level);
    return level;
  }

  private WaveFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
