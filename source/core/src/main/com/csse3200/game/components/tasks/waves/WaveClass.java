package com.csse3200.game.components.tasks.waves;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.GameTime;
import com.csse3200.game.services.ServiceLocator;

import java.util.*;

public class WaveClass {
  private HashMap<String, int[]> entities;
  private GameTime gameTime;
  private long startTime;
  private List<Tuple> wave;
  private Random rand = new Random();
  private int mobIndex;

  /**
   * Constructor for the WaveClass
   * @param entities: HashMap of entities and the quantity of them to be spawned in this wave
   */
  public WaveClass(HashMap<String, int[]> entities) {
    this.entities = entities;
    this.wave = entitiesToWave();
    this.mobIndex = 0;
  }

  /**
   * Get the entities that are part of this wave and randomise the order they are spawned
   * @return mobs for the wave in form of (mob name, mob health)
   */
  private List<Tuple> entitiesToWave() {
    List<Tuple> enemies = new ArrayList<>();
    for (Map.Entry<String, int[]> set : entities.entrySet()) {
      for (int i = 0; i < set.getValue()[0]; i++) {
        enemies.add(new Tuple(set.getKey(), set.getValue()[1]));
      }
    }
    Collections.shuffle(enemies);
    return enemies;
  }

  /**
   * Gets the current number of entities spawned in the wave
   * @return size of the current wave (number of entities)
   */
  public int getSize() {
    return this.wave.size();
  }

  /**
   * Gets the current entities that have spawned in the wave
   * @return list of mobs in the current wave
   */
  public List<Tuple> getMobs() {
    return this.wave;
  }

  @Override
  public String toString(){
    return this.wave.toString();
  }

  public HashMap<String, int[]> getEntities() {
    return this.entities;
  }

}
