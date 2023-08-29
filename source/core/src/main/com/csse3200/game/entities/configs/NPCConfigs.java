package com.csse3200.game.entities.configs;

/**
 * Defines all NPC configs to be loaded by the NPC Factory.
 */
public class NPCConfigs {
  public BaseEntityConfig ghost = new BaseEntityConfig();
  public BaseEntityConfig fireBall = new ProjectileConfig();
  public GhostKingConfig ghostKing = new GhostKingConfig();
  public BaseEntityConfig projectile = new ProjectileConfig();

  public BossKingConfigs BossKing = new BossKingConfigs();

}
