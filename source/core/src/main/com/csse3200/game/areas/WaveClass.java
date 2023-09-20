package com.csse3200.game.areas;

import com.csse3200.game.entities.Entity;

public class WaveClass extends Entity {
  private String entityToSpawn;
  private int quantity;
  private float spawnDelay;

  public WaveDefinition(String entityToSpawn, int quantity, float spawnDelay) {
    this.entityToSpawn = entityToSpawn;
    this.quantity = quantity;
    this.spawnDelay = spawnDelay;
  }

  public String getEntityToSpawn() {
    return entityToSpawn;
  }

  public int getQuantity() {
    return quantity;
  }

  public float getSpawnDelay() {
    return spawnDelay;
  }
}

// TODO: Redo this whole thing :)