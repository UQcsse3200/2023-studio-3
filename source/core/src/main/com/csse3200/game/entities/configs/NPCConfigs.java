package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.Melee;
import com.csse3200.game.entities.Weapon;

import java.util.ArrayList;

/**
 * Defines all NPC configs to be loaded by the NPC Factory.
 */
public class NPCConfigs {
  public BaseEntityConfig ghost = new BaseEntityConfig();
  public BaseEntityConfig fireBall = new ProjectileConfig();
  // public GhostKingConfig ghostKing = new GhostKingConfig();
  public BaseEntityConfig projectile = new ProjectileConfig();
  public GhostKingConfig ghostKing = new GhostKingConfig();
  public BaseEnemyConfig xenoGrunt = new BaseEnemyConfig(
          new ArrayList<Integer>(),
          new ArrayList<Melee>(),
          new ArrayList<Weapon>());

  public BossKingConfigs BossKing = new BossKingConfigs();

}
