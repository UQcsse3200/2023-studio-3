package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.*;


public class WaveClass {
  private HashMap<String, Integer> entities;
  private GameTime gameTime;
  private long startTime;
  private List<String> wave;
  private Random rand = new Random();
  private int mobIndex;

  public WaveClass(HashMap<String, Integer> entities) {
    this.entities = entities;
    this.wave = entitiesToWave();
    this.mobIndex = 0;
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

  public int getSize() {
    return this.wave.size();
  }

  public List<String> getMobs() {
    return this.wave;
  }

}
