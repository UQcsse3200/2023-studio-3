package com.csse3200.game.entities.factories;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tasks.waves.LevelWaves;
import com.csse3200.game.components.tasks.waves.WaveClass;
import com.csse3200.game.components.tasks.waves.WaveTask;
import com.csse3200.game.entities.Entity;

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

  private static Random rand = new Random();

  public static Entity createWaves() {
    int chosenLevel = 3;
    int difficulty = 1;
    int maxWaves = 1;
    switch (chosenLevel) {
      case 2:
        difficulty = 3;
        maxWaves = 10;
        break;
      case 3:
        difficulty = 5;
        maxWaves = 15;
        break;
      default:
        difficulty = 2;
        maxWaves = 5;
    }

//    HashMap<String, Integer> mobs = new HashMap<>();
//    mobs.put("Xeno", 3);
//    mobs.put("DodgingDragon", 4);
//    HashMap<String, Integer> mobs2 = new HashMap<>();
//    mobs2.put("Xeno", 3);
//    WaveClass wave1 = new WaveClass(mobs);
//    WaveClass wave2 = new WaveClass(mobs2);
//    LevelWaves level = new LevelWaves(10);
//    level.addWave(wave1);
//    level.addWave(wave2);
    LevelWaves level = createLevel(difficulty, maxWaves, chosenLevel);
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WaveTask());
    return level.addComponent(aiComponent);
  }

  public static LevelWaves createLevel(int maxDiff, int maxWaves, int chosenLevel) {
    System.out.println("creating level difficulty: " + maxDiff + " maxWaves: " + maxWaves + " chosenLevel: " + chosenLevel);
    int minMobs = 3 + maxDiff;
//    int minMobs = maxDiff;
//    ArrayList<String> possibleMobs = new ArrayList<>(Arrays.asList("Xeno", "SplittingXeno", "DodgingDragon", "DeflectXeno"));
    ArrayList<String> level1Mobs = new ArrayList<>(Arrays.asList("Xeno", "SplittingXeno", "WaterSlime", "DeflectXeno"));
    ArrayList<String> level2Mobs = new ArrayList<>(Arrays.asList("Xeno", "SplittingXeno", "Skeleton", "DeflectXeno", "Wizard"));
    ArrayList<String> level3Mobs = new ArrayList<>(Arrays.asList("Xeno", "SplittingXeno", "DodgingDragon", "DeflectXeno", "FireWorm"));
    String boss1 = "WaterBoss";
    String boss2 = "MagicBoss";
    String boss3 = "FireBoss";
    LevelWaves level = new LevelWaves(5);

    ArrayList<String> possibleMobs;

    String boss = "";
    switch (chosenLevel) {
      case 2:
        boss = boss2;
        possibleMobs = level2Mobs;
        System.out.println("level 2");
        break;
      case 3:
        boss = boss3;
        possibleMobs = level3Mobs;
        System.out.println("level 3");
        break;
      default:
        boss = boss1;
        possibleMobs = level1Mobs;
        break;
    }

    for (int i = 1; i <= maxWaves; i++) {
      System.out.println("adding another mob: " + i);
      HashMap<String, Integer> mobs = new HashMap<>();

      if (i % 5 == 0) {
        mobs.put(boss, 1/5);
      }

      String mob1 = possibleMobs.get(rand.nextInt(possibleMobs.size()));
      String mob2 = possibleMobs.get(rand.nextInt(possibleMobs.size()));

      while (mob2 == mob1) {
        mob2 = possibleMobs.get(rand.nextInt(possibleMobs.size()));
      }

      int mob1Num = rand.nextInt(minMobs - 1);
      int mob2Num = minMobs - mob1Num;

      mobs.put(mob1, mob1Num);
      mobs.put(mob2, mob2Num);

      System.out.println(mobs);
      level.addWave(new WaveClass(mobs));
      minMobs ++;
    }

    return level;
  }

  private WaveFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
