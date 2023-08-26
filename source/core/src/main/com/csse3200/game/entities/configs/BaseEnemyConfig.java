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

    private final int speed;

    private final int fullHeath;

    private final int id;

    //TODO: change to item class
    private final ArrayList<Integer> drops;
    private final ArrayList<String> views;
    private String state;

    //TODO: change to class Ability
//    private final ArrayList<String> abilities;
    private ArrayList<Melee> closeRangeAbilities;
    private ArrayList<Weapon> longRangeAbilities;

    /**
     * Creates a new enemy config with default values.
     */
    public BaseEnemyConfig(ArrayList<Integer> drops, ArrayList<String> views, ArrayList<Melee> closeRangeAbilities, ArrayList<Weapon> longRangeAbilities) {
        this.speed = 1;
        this.drops = drops;
        if (views.size() < 3) {
            throw new IllegalArgumentException("Enemy must have at least 3 views");
        }
        this.views = views;
        this.state = views.get(0);
        this.fullHeath = this.health;
//        this.abilities = abilities;

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
     * @param views the views of the enemy
     * @param baseAttack the base damage to the target
     */
    public BaseEnemyConfig(int speed, int health, ArrayList<Integer> drops, ArrayList<String> views,
                           ArrayList<Melee> closeRangeAbilities, ArrayList<Weapon> longRangeAbilities, int baseAttack) {
        this.speed = speed;
        this.health = health;
        this.fullHeath = health;
        this.drops = drops;
        if (views.size() < 3) {
            throw new IllegalArgumentException("Enemy must have at least 3 views");
        }
        this.views = views;
        this.state = views.get(0);
        this.closeRangeAbilities = closeRangeAbilities;
        this.longRangeAbilities = longRangeAbilities;
        this.baseAttack = baseAttack;
        this.id = generateId();
    }

    public int generateId() {
        if (idCounter == 0) {
            idCounter = this.hashCode();
            return idCounter;
        }
        return ++idCounter;
    }

    /* return the unique id of the enemy */
    public int getId() {
        return this.id;
    }

    public int getHealth() {
        return this.health;
    }

    public int getSpeed() {
        return this.speed;
    }

    public String getState() {
        return this.state;
    }

    public String toString() {
        return "Enemy: " + this.id + " Drops: "
                + this.drops + " Views: " + this.views + " State: " + this.state
                + " Speed: " + this.speed + " Full Health: " + this.fullHeath
                + " Current Health: " + this.health + " Base Attack: " + this.baseAttack;
    }


}
