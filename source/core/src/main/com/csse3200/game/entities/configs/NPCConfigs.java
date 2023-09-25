package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.Melee;
import com.csse3200.game.entities.Weapon;

import java.util.ArrayList;
import java.util.Currency;

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
          10,
          100,
          new ArrayList<Currency>(),
          new ArrayList<Melee>(),
          new ArrayList<Weapon>(),
          10);

  public MobBossConfigs mobBoss = new MobBossConfigs();

}
