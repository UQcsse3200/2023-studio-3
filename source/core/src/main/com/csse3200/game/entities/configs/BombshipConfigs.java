package com.csse3200.game.entities.configs;

/**
 * Defines the properties stored in Bombship config files to be loaded by the Bombship Factory.
 */
public class BombshipConfigs extends BaseEntityConfig  {
  public BaseEntityConfig bombship = new BaseEntityConfig();

  private int bombshipHealth = 100;
  private int bombshipBaseAttack = 20;

  /**
   * Function for getting bombship's health
   * @return The health of this bombship
   */
  public int getHealth() {
    return bombshipHealth;
  }

  /**
   * Function for getting bombship's base attack
   * @return The base attack of this bomb ship
   */
  public int getBaseAttack() {
    return bombshipBaseAttack;
  }
}
