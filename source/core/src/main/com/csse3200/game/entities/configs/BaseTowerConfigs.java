package com.csse3200.game.entities.configs;

/**
 * Defines all tower configs to be loaded by the Tower Factory.
 */
public class BaseTowerConfigs {
    private WeaponTowerConfig weapon = new WeaponTowerConfig();
    private WallTowerConfig wall = new WallTowerConfig();
    private IncomeTowerConfig income = new IncomeTowerConfig();
    private FireTowerConfig fireTower = new FireTowerConfig();
    private StunTowerConfig stunTower = new StunTowerConfig();
    private TNTTowerConfigs TNTTower = new TNTTowerConfigs();
    private DroidTowerConfig DroidTower = new DroidTowerConfig();
    private FireworksTowerConfig fireworksTower = new FireworksTowerConfig();
    private PierceTowerConfig pierceTower = new PierceTowerConfig();
    private RicochetTowerConfig ricochetTower = new RicochetTowerConfig();
    private HealTowerConfig healTower = new HealTowerConfig();

    /**
     * Function for getting the wall tower's config
     * @return The config of wall tower
     */
    public WallTowerConfig getWall() {
        return wall;
    }

    /**
     * Function for getting the weapon tower's config
     * @return The config of weapon tower
     */
    public WeaponTowerConfig getWeapon() {
        return weapon;
    }

    /**
     * Function for getting the income tower's config
     * @return The config of income tower
     */
    public IncomeTowerConfig getIncome() {
        return income;
    }

    /**
     * Function for getting the fire tower's config
     * @return The config of fire tower
     */
    public FireTowerConfig getFireTower() {
        return fireTower;
    }

    /**
     * Function for getting the stun tower's config
     * @return The config of stun tower
     */
    public StunTowerConfig getStunTower() {
        return stunTower;
    }

    /**
     * Function for getting the TNT tower's config
     * @return The config of TNT tower */
    public TNTTowerConfigs getTNTTower() {
        return TNTTower;
    }

    /**
     * Function for getting the droid tower's config
     * @return The config of droid tower
     */
    public DroidTowerConfig getDroidTower() {
        return DroidTower;
    }

    /**
     * Function for getting the fireworks tower's config
     * @return The config of fireworks tower
     */
    public FireworksTowerConfig getFireworksTower() {
        return fireworksTower;
    }

    /**
     * Function for getting the pierce tower's config
     * @return The config of pierce tower
     */
    public PierceTowerConfig getPierceTower() {
        return pierceTower;
    }

    /**
     * Function for getting the ricochet tower's config
     * @return The config of ricochet tower
     */
    public RicochetTowerConfig getRicochetTower() {
        return ricochetTower;
    }

    /**
     * Function for getting the heal tower's config
     * @return The config of heal tower
     */
    public HealTowerConfig getHealTower() {
        return healTower;
    }
}