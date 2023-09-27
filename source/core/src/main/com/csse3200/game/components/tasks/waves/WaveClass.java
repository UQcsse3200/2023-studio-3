package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.*;


public class WaveClass extends Entity {
  private HashMap<String, Integer> entities;
  private float spawnDelay;
  private GameTime gameTime;
  private long startTime;
  private List<String> wave;
  private Random rand = new Random();
  private int waveIndex;

  public WaveClass(HashMap<String, Integer> entities, float spawnDelay) {
    this.entities = entities;
    this.spawnDelay = spawnDelay;
    this.gameTime = ServiceLocator.getTimeSource();
    this.startTime = this.gameTime.getTime();
    this.wave = entitiesToWave();
    this.waveIndex = 0;
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
    if (gameTime.getTime() >= startTime + spawnDelay) {
      GridPoint2 randomPos = new GridPoint2(19, rand.nextInt(1, 7));
      this.getEvents().trigger("spawnWave", wave.get(waveIndex), randomPos);
      startTime = gameTime.getTime();
      waveIndex++;
    } else if (waveIndex == wave.size()) {
      this.getEvents().trigger("waveFinishedSpawning");
    }
  }

  public int getSize() {
    return this.wave.size();
  }

}
