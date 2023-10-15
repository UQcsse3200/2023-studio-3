package com.csse3200.game.entities.factories;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.Tuple;
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

  // TODO: include necromancer
  private static final ArrayList<String> MELEE_MOBS = new ArrayList<>(Arrays.asList(
      "Skeleton", "Coat", "DragonKnight"
  ));

  private static final ArrayList<ArrayList<String>> lvl1Structure = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList(
          "Coat"
      )), new ArrayList<>(Arrays.asList(
          "Coat",
          "WaterQueen"
      )), new ArrayList<>(Arrays.asList(
          "WaterQueen",
          "SplittingWaterSlime"
      )), new ArrayList<>(Arrays.asList(
          "Coat",
          "WaterQueen",
          "SplittingWaterSlime"
      ))
  ));

  private static final ArrayList<ArrayList<String>> lvl2Structure = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList(
          "Skeleton"
      )), new ArrayList<>(Arrays.asList(
          "Skeleton",
          "ArcaneArcher"
      )), new ArrayList<>(Arrays.asList(
          "Skeleton",
          "DeflectWizard"
      )), new ArrayList<>(Arrays.asList(
          "Skeleton",
          "NightBorne"
      )), new ArrayList<>(Arrays.asList(
          "DeflectWizard",
          "NightBorne"
      )), new ArrayList<>(Arrays.asList(
          "NightBorne",
          "Skeleton"
      )), new ArrayList<>(Arrays.asList(
          "DeflectWizard",
          "NightBorne"
      )), new ArrayList<>(Arrays.asList(
          "ArcaneArcher",
          "NightBorne",
          "DeflectWizard"
      )), new ArrayList<>(Arrays.asList(
          "Skeleton",
          "ArcaneArcher",
          "DeflectWizard",
          "NightBorne"
      ))
  ));

  private static final ArrayList<ArrayList<String>> lvl3Structure = new ArrayList<>(Arrays.asList(
      new ArrayList<>(Arrays.asList(
          "Coat"
      )), new ArrayList<>(Arrays.asList(
          "Coat",
          "DodgingDragon"
      )), new ArrayList<>(Arrays.asList(
          "Coat",
          "FireWorm"
      )), new ArrayList<>(Arrays.asList(
          "Coat",
          "Coat"
      )), new ArrayList<>(Arrays.asList(
          "Coat",
          "FireWorm"
      )), new ArrayList<>(Arrays.asList(
          "DodgingDragon",
          "FireWorm"
      )), new ArrayList<>(Arrays.asList(
          "DodgingDragon",
          "Coat"
      )), new ArrayList<>(Arrays.asList(
          "FireWorm",
          "Coat"
      )), new ArrayList<>(Arrays.asList(
          "Coat",
          "Coat"
      )), new ArrayList<>(Arrays.asList(
          "DodgingDragon",
          "Coat",
          "Coat"
      )), new ArrayList<>(Arrays.asList(
          "FireWorm",
          "Coat",
          "DodgingDragon"
      )), new ArrayList<>(Arrays.asList(
          "FireWorm",
          "Coat",
          "Coat"
      )), new ArrayList<>(Arrays.asList(
          "Coat",
          "Coat",
          "Coat",
          "DodgingDragon",
          "FireWorm"
      ))
  ));

  // Base health of the mobs
//  private static int BASE_HEALTH = 80;
  private static int MELEE_BASE_HEALTH = 80;
  private static int RANGE_BASE_HEALTH = 60;

  // Base health of the boss
  private static int BOSS_BASE_HEALTH = 200;

  /**
   * The function will create the waves depending on the level selected by the user.
   * */
  public static Entity createWaves() {

    int chosenLevel = GameLevelData.getSelectedLevel();
    int difficulty;
    int maxWaves;
    switch (chosenLevel) {
      case 0:
        difficulty = 2;
        maxWaves = 10;
        break;
      case 2:
        difficulty = 3;
        maxWaves = 15;
        break;
      default:
        difficulty = 1;
        maxWaves = 5;
    }

    LevelWaves level = createLevel(maxWaves, difficulty);
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
   * constructed from two random mobs of the possible ones allocated for that level. Based on the level chosen the health of the mobs will increase at a greater rate. For wave i the
   * health will be increased from BASE_HEALTH to BASE_HEALTH + (I * chosen_level) so the difficulty
   * increases quicker.
   *
   * Bosses are spawned every 5 waves and the health of the bosses increases as the level increases.
   * For every 5 levels another boss is included (5th wave -> 1 boss, 10th wave -> 2 bosses etc.)
   *
   * @param maxWaves - the maximum number of waves for the level
   * @param chosenLevel - the level selected by the user
   *
   * @return level - the level constructed with all the waves of mobs
   * */
  public static LevelWaves createLevel(int maxWaves, int chosenLevel) {
//    int minMobs = 3 + maxDiff;

    // The mob bosses assigned to the associated levels (planets)
    String boss1 = "IceBoss";
//    String boss1 = "PatrickBoss";
    String boss2 = "PatrickBoss";
    //String boss3 = "IceBoss";

    String boss3 = "FireBoss";

    int spawnDelay = 5;

    // Create new level entity with spawn delay of 5 seconds
    LevelWaves level = new LevelWaves(spawnDelay);
    // Tell the waveService what the spawn delay for levels will be (for UI team).
    ServiceLocator.getWaveService().setSpawnDelay(spawnDelay);

    ArrayList<ArrayList<String>> possibleMobs;

    // set the possible mobs and boss for the level
    String boss = "";
    int minMobs;
    switch (chosenLevel) {
      case 2:
        boss = boss2;
        possibleMobs = lvl2Structure;
        minMobs = 6;
        break;
      case 3:
        boss = boss3;
        possibleMobs = lvl3Structure;
        minMobs = 8;
        break;
      default:
        boss = boss1;
        possibleMobs = lvl1Structure;
        minMobs = 5;
        break;
    }

    // Create mxWaves number of waves with mob stats increasing
    int atWave = 1;
    for (ArrayList<String> wave : possibleMobs) {
      System.out.println("wave: " + wave);
//      for (int i = 1; i <= maxWaves; i++) {
//    for (int i = 1; i <= maxWaves; i++) {
      HashMap<String, int[]> mobs = new HashMap<>();

      int leftToSort = wave.size() - 1;
      int currentMobs = 0;
      for (String mob: wave) {
        System.out.println("mob: " + mob);
        int num;
        if (leftToSort == 0) {
          num = minMobs - currentMobs;
        } else {
          num = rand.nextInt(minMobs - currentMobs - (2 * leftToSort)) + 2;
          System.out.println("number of mobs: " + num + " for minMobs of " + minMobs + " and leftToSort of " + leftToSort);
          currentMobs += num;
        }

        int health = RANGE_BASE_HEALTH;
        if (MELEE_MOBS.contains(mob)) {
          health = MELEE_BASE_HEALTH;
        }
        int[] mobStats = {num, health + (atWave * chosenLevel)};
        mobs.put(mob, mobStats);

        leftToSort --;
      }
      minMobs ++;

      level.addWave(new WaveClass(mobs));
      atWave++;
    }

    HashMap<String, int[]> bossMob = new HashMap<>();
    bossMob.put(boss, new int[]{1, BOSS_BASE_HEALTH + (chosenLevel * maxWaves)});
    level.addWave(new WaveClass(bossMob));

      // add i/5 bosses every 5 waves with increased health where i is the i^th wave
      // 5/5 -> 1 boss, 10/5 -> 2 bosses etc
//      if (i % 5 == 0) {
//        int[] bossStats = {i/5, BOSS_BASE_HEALTH + (chosenLevel * i)};
//        mobs.put(boss, bossStats);
//      }

      // select 2 random mobs from the possible mobs
//      String mob1 = possibleMobs.get(rand.nextInt(possibleMobs.size()));
//      String mob2 = possibleMobs.get(rand.nextInt(possibleMobs.size()));

      // ensure the mobs are different
//      while (mob2 == mob1) {
//        mob2 = possibleMobs.get(rand.nextInt(possibleMobs.size()));
//      }
//
//      int mob1Num = rand.nextInt(minMobs - 3) + 2;
//      int mob2Num = minMobs - mob1Num;
//
//      int[] mob1Stats = {mob1Num, BASE_HEALTH + (chosenLevel * i)};
//      int[] mob2Stats = {mob2Num, BASE_HEALTH + (chosenLevel * i)};
//
//
//      mobs.put(mob1, mob1Stats);
//      mobs.put(mob2, mob2Stats);
//
//      level.addWave(new WaveClass(mobs));
//      minMobs ++;
//    }

    logger.info("Level created: " + level);
    return level;
  }

  private WaveFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
