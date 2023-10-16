package com.csse3200.game.entities.configs;

import java.util.ArrayList;

/**
 * Defines all NPC configs to be loaded by the NPC Factory.
 */
public class NPCConfigs {
  public BaseEntityConfig ghost = new BaseEntityConfig();
  public BaseEntityConfig fireBall = new ProjectileConfig();
  public BaseEntityConfig projectile = new ProjectileConfig();
  public GhostKingConfig ghostKing = new GhostKingConfig();
  public BaseEnemyConfig xenoGrunt = new BaseEnemyConfig(
          10,
          100,
          new ArrayList<>(),
          new ArrayList<>(),
          new ArrayList<>(),
          10);

  public MobBossConfigs mobBoss = new MobBossConfigs();

}
