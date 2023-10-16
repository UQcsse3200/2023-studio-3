package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.Weapon;

/**
 * Configuration for projectiles.
 */
public class ProjectileConfig extends BaseEntityConfig implements Weapon {
  public int health = 1;
  public int baseAttack = 0;

  public int getDamage() {
    return baseAttack;
  }

  public int getAttackRange() {
    return 0;
  }
}
