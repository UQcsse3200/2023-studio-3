package com.csse3200.game.entities.configs;

import com.csse3200.game.entities.Melee;
import com.csse3200.game.entities.Weapon;

import java.util.*;

/**
 * Defines all enemy configs to be loaded by the Enemy Factory.
 * Variables:
 *      idCounter - counter for the unique id of the enemy
 *      speed - factor of how fast the enemy moves
 *      fullHealth - the full health of the enemy
 *      health - the current health of the enemy
 *      id - unique identifier for the enemy
 *      drops - the items that the enemy drops when it dies
 *      views - the different images of enemy over different states
 *      state - the current state of the enemy (full health, half health, low health)
 *      baseAttack - the base damage of an enemy's attack
 * Functions
 *      takeDamage - process the damge taken by the enemy
 *      attack - process an attack
 *      die - process of death (remove from game and drop resources)
 *
 */
public class BaseEnemyConfig extends BaseEntityConfig {

    private static int idCounter = 0;

    public final int speed;

    public final int fullHeath;

    private final int id;

    //TODO: change to item class
    private final List<Currency> drops;
    private List<Melee> closeRangeAbilities;
    private List<Weapon> longRangeAbilities;

    /**
     * Creates a new enemy config with default values.
     */
    public BaseEnemyConfig(List<Currency> drops,
                           List<Melee> closeRangeAbilities,
                           List<Weapon> longRangeAbilities) {
        this.speed = 1;
        this.drops = drops;
        this.fullHeath = this.health;
        this.closeRangeAbilities = closeRangeAbilities;
        this.longRangeAbilities = longRangeAbilities;

        this.id = generateId();

    }

    /**
     * Creates a new enemy config with the given values.
     *
     * @param speed the speed of the enemy
     * @param health the full of the enemy
     * @param drops the drops of the enemy
     * @param baseAttack the base damage to the target
     */
    public BaseEnemyConfig(int speed, int health, List<Currency> drops,
                           List<Melee> closeRangeAbilities, List<Weapon> longRangeAbilities, int baseAttack) {
        this.speed = speed;
        this.health = health;
        this.fullHeath = health;
        this.drops = drops;
        this.closeRangeAbilities = closeRangeAbilities;
        this.longRangeAbilities = longRangeAbilities;
        this.baseAttack = baseAttack;
        this.id = generateId();
    }

    /**
     * Generate a unique id based on the previous enemy id
     * */
    public int generateId() {
        if (idCounter == 0) {
            idCounter = this.hashCode();
            return idCounter;
        }
        return ++idCounter;
    }

    /** return the unique id of the enemy */
    public int getId() {
        return this.id;
    }

    /** return the currency the enemy will drop when it dies */
    //TODO change to Currency class
    public String getDrops() {
        return this.drops.toString();
    }

    /*** return the close range (Melee) attacks of the enemy */
    public List<Melee> getCloseRangeAbilities() {
        return this.closeRangeAbilities;
    }

    /** return the long range (Projectile) attacks of the enemy */
    //TODO change to projectile
    public List<Weapon> getLongRangeAbilities() {
        return this.longRangeAbilities;
    }

    public String toString() {
        return "Enemy: " + this.id + " Drops: " + this.drops + " Close Range: "
                + this.closeRangeAbilities + " Long Range: " + this.longRangeAbilities
                + " Speed: " + this.speed + " Full Health: " + this.fullHeath
                + " Current Health: " + this.health + " Base Attack: " + this.baseAttack;
    }


}
