package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;

import java.util.*;


public class WaveClass extends Entity {
  private HashMap<String, Integer> entities = new HashMap<>();
  private float spawnDelay;

  public WaveClass(HashMap<String, Integer> entities, float spawnDelay) {
    this.entities = entities;
    this.spawnDelay = spawnDelay;
  }

  private List<String> entitiesToWave() {
    List<String> enemies = new ArrayList<>();
    for (Map.Entry<String, Integer> set : entities.entrySet()) {
      for (int i = 0; i < set.getValue(); i++) {
        enemies.add(set.getKey());
      }
    }
    Collections.shuffle(enemies);
    return enemies;
  }

  public void spawnWave() {
    List<String> wave = entitiesToWave();
    Random rand = new Random();
    for (int i = 0; i < wave.size(); i++) {
      GridPoint2 randomPos = new GridPoint2(19, rand.nextInt(1, 7));
      this.getEvents().trigger("spawnWave", wave.get(i), randomPos);
    }
  }

}
