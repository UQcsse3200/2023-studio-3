package com.csse3200.game.entities.configs;

public class FireworksTowerConfig {
    private int health = 1;
    private int baseAttack = 1;
    private int cost = 1;
    private int attackRate = 1;
    private int incomeRate = 0;

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

    /**
     * Function for getting tower's attack rate
     * @return The attack rate of this tower
     */
    public int getAttackRate() {
        return this.attackRate;
    }

    /**
     * Function for getting tower's income rate
     * @return The income rate of this tower
     */
    public int getIncomeRate() {
        return this.incomeRate;
    }
}
