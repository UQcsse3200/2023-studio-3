package com.csse3200.game.entities.factories;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.WaveClass;
import com.csse3200.game.components.tasks.waves.WaveTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.screens.GameLevelData;
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

  // Base health of the mobs
  private static int BASE_HEALTH = 60;

  // Base health of the boss
  private static int BOSS_BASE_HEALTH = 80;

  /**
   * The function will create the waves depending on the level selected by the user.
   * */
  public static Entity createWaves() {

    int chosenLevel = GameLevelData.getSelectedLevel();
    int difficulty;
    int maxWaves;
    switch (chosenLevel) {
      case 0:
        difficulty = 3;
        maxWaves = 10;
        break;
      case 2:
        difficulty = 5;
        maxWaves = 15;
        break;
      default:
        difficulty = 2;
        maxWaves = 5;
    }

    LevelWaves level = createLevel(difficulty, maxWaves, chosenLevel);
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
   * @param maxDiff - the maximum difficulty of the level (the start number of mobs - 3)
   * @param maxWaves - the maximum number of waves for the level
   * @param chosenLevel - the level selected by the user
   *
   * @return level - the level constructed with all the waves of mobs
   * */
  public static LevelWaves createLevel(int maxDiff, int maxWaves, int chosenLevel) {
    int minMobs = 3 + maxDiff;
    // These are the mobs assigned to the associated levels (planets)
    ArrayList<String> level1Mobs = new ArrayList<>(Arrays.asList("Coat", "SplittingWaterSlime", "WaterQueen"));
    // TODO switch to hashed to demo the bosses and make sure to do this for mobs as well
    // TODO hash out level1 test in NPCFactory when doing this. 
//    ArrayList<String> level1Mobs = new ArrayList<>(Arrays.asList("Xeno", "PatrickBoss", "WaterQueen"));
//    ArrayList<String> level1Mobs = new ArrayList<>(Arrays.asList("Xeno", "IceBoss", "WaterQueen"));
    ArrayList<String> level2Mobs = new ArrayList<>(Arrays.asList("ArcaneArcher","SplittingNightBorne"));
    ArrayList<String> level3Mobs = new ArrayList<>(Arrays.asList("Xeno", "DodgingDragon", "FireWorm"));

    // The mob bosses assigned to the associated levels (planets)
    String boss1 = "IceBoss";
//    String boss1 = "PatrickBoss";
    String boss2 = "PatrickBoss";
    // String boss3 = "IceBoss";
    //TODO change this to a fire boss in sprint 4
   String boss3 = "FireBoss";
    LevelWaves level = new LevelWaves(5);

    ArrayList<String> possibleMobs;

    // set the possible mobs and boss for the level
    String boss = "";
    switch (chosenLevel) {
      case 0:
        boss = boss2;
        possibleMobs = level2Mobs;
        chosenLevel = 2;
        break;
      case 2:
        boss = boss3;
        possibleMobs = level3Mobs;
        chosenLevel = 3;
        break;
      default:
        boss = boss1;
        possibleMobs = level1Mobs;
        chosenLevel = 1;
        break;
    }

    // Create mxWaves number of waves with mob stats increasing
    for (int i = 1; i <= maxWaves; i++) {
      HashMap<String, int[]> mobs = new HashMap<>();

      // add i/5 bosses every 5 waves with increased health where i is the i^th wave
      // 5/5 -> 1 boss, 10/5 -> 2 bosses etc
      if (i % 5 == 0) {
        int[] bossStats = {i/5, BOSS_BASE_HEALTH + (chosenLevel * i)};
        mobs.put(boss, bossStats);
      }

      // select 2 random mobs from the possible mobs
      String mob1 = possibleMobs.get(rand.nextInt(possibleMobs.size()));
      String mob2 = possibleMobs.get(rand.nextInt(possibleMobs.size()));

      // ensure the mobs are different
      while (mob2 == mob1) {
        mob2 = possibleMobs.get(rand.nextInt(possibleMobs.size()));
      }

      int mob1Num = rand.nextInt(minMobs - 3) + 2;
      int mob2Num = minMobs - mob1Num;

      int[] mob1Stats = {mob1Num, BASE_HEALTH + (chosenLevel * i)};
      int[] mob2Stats = {mob2Num, BASE_HEALTH + (chosenLevel * i)};


      mobs.put(mob1, mob1Stats);
      mobs.put(mob2, mob2Stats);

      level.addWave(new WaveClass(mobs));
      minMobs ++;
    }

    logger.info("Level created: " + level);
    return level;
  }

  private WaveFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
