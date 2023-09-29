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
    Integer chosenLevel = 1;
    int difficulty = 1;
    int maxWaves = 1;
    switch (chosenLevel) {
      case 1:
        difficulty = 2;
        maxWaves = 5;
      case 2:
        difficulty = 3;
        maxWaves = 10;
      case 3:
        difficulty = 5;
        maxWaves = 15;
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
    LevelWaves level = createLevel(difficulty, maxWaves);
    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WaveTask());
    return level.addComponent(aiComponent);
  }

  public static LevelWaves createLevel(int maxDiff, int maxWaves) {
    int minMobs = 3 + maxDiff;
//    int minMobs = maxDiff;
    ArrayList<String> possibleMobs = new ArrayList<>(Arrays.asList("Xeno", "SplittingXeno", "DodgingDragon", "DeflectXeno"));
    LevelWaves level = new LevelWaves(5);
    for (int i = 1; i < maxWaves; i++) {
      String mob1 = possibleMobs.get(rand.nextInt(4));
      String mob2 = possibleMobs.get(rand.nextInt(4));

      while (mob2 == mob1) {
        mob2 = possibleMobs.get(rand.nextInt(4));
      }

      int mob1Num = rand.nextInt(minMobs - 1);
      int mob2Num = minMobs - mob1Num;

      HashMap<String, Integer> mobs = new HashMap<>();
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
