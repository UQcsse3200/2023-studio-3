package com.csse3200.game.areas;

import com.csse3200.game.entities.Entity;

import java.util.ArrayList;

public class WaveDefinition extends Entity {
  private int size;
  private float spawnDelay;
  private ArrayList<String> entities;

  public WaveDefinition(ArrayList<String> entities, int size, float spawnDelay) {
    this.entities = entities;
    this.size = size;
    this.spawnDelay = spawnDelay;
  }

  public String getEntityToSpawn(int index) {
    return this.entities.get(index);
  }

  public int getSize() {
    return size;
  }

  public float getSpawnDelay() {
    return spawnDelay;
  }
}

// TODO: Redo this whole thing :)