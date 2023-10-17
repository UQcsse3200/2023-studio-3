package com.csse3200.game.entities.configs;

public class HealTowerConfig {
    private int health = 1;
    private int baseAttack = 0;
    private int cost = 1;

    /**
     * Function for getting tower's health
     * @return The health of this tower
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Function for getting tower's base attack
     * @return The base attach of this tower
     */
    public int getBaseAttack() {
        return this.baseAttack;
    }

    /**
     * Function for getting tower's cost
     * @return The cost of this tower
     */
    public int getCost() {
        return this.cost;
    }
}
