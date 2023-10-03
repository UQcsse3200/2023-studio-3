package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in Bombship config files to be loaded by the Bombship Factory.
 */
public class BombshipConfigs extends BaseEntityConfig  {
  public BaseEntityConfig bombship = new BaseEntityConfig();
  public int health = 100;
  public int baseAttack = 20;
}
