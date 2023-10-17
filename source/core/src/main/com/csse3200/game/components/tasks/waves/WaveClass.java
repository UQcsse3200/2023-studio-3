package com.csse3200.game.components.tasks.waves;

import java.util.*;

public class WaveClass {
  private HashMap<String, int[]> entities;
  private List<Tuple> wave;

  /**
   * Constructor for the WaveClass
   * @param entities: HashMap of entities and the quantity of them to be spawned in this wave
   */
  public WaveClass(HashMap<String, int[]> entities) {
    this.entities = entities;
    this.wave = entitiesToWave();
  }

  /**
   * Get the entities that are part of this wave and randomise the order they are spawned
   * @return mobs for the wave in form of (mob name, mob health)
   */

  public List<Tuple> entitiesToWave() {
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
