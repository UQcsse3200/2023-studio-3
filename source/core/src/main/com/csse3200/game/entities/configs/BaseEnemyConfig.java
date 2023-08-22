package com.csse3200.game.entities.configs;

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
    private final List<Integer> drops;
    private final List<String> views;
    private String state;

    //TODO: change to class Ability
    private final String abilities;

    /**
     * Creates a new enemy config with default values.
     */
    public BaseEnemyConfig(List<Integer> drops, List<String> views, String abilities) {
        this.speed = 1;
        this.drops = drops;
        if (views.size() < 3) {
            throw new IllegalArgumentException("Enemy must have at least 3 views");
        }
        this.views = views;
        this.state = views.get(0);
        this.fullHeath = this.health;
        this.abilities = abilities;
        this.id = generateId();
    }

    /**
     * Creates a new enemy config with the given values.
     *
     * @param speed the speed of the enemy
     * @param health the full of the enemy
     * @param drops the drops of the enemy
     * @param views the views of the enemy
     * @param abilities the abilities of the enemy
     * @param baseAttack the base damage to the target
     */
    public BaseEnemyConfig(int speed, int health, List<Integer> drops, List<String> views,
                           String abilities, int baseAttack) {
        this.speed = speed;
        this.health = health;
        this.fullHeath = health;
        this.drops = drops;
        if (views.size() < 3) {
            throw new IllegalArgumentException("Enemy must have at least 3 views");
        }
        this.views = views;
        this.state = views.get(0);
        this.abilities = abilities;
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

    //TODO: change to take class Ability
    /**
     * Process the damage taken by the enemy. Will decrease the health by the
     * damage of the Ability. If the health is less than or equal to 0, the enemy
     * will die. If the enemy is not dead, the state will be changed based on the
     * current health.
     * @param damage the damage taken
     *               //TODO: the ability that is attacking
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            die();
            return;
        }

        if (this.health <= (this.fullHeath * 0.33)) {
            this.state = this.views.get(2);
        } else if (this.health <= (this.fullHeath * 0.66)) {
            this.state = this.views.get(1);
        }
    }

    //TODO change the return type to the drop type and process to player
    /**
     * Drop a random item from the list of drops.
     * @return the item dropped
     */
    public Integer drop() {
        Random rand = new Random();
        int index = rand.nextInt(this.drops.size());
        return this.drops.get(index);
    }

    //TODO: Implement this by processing drops, removing from game, etc.
    /**
     * Process the death of an enemy. This function should handle the removal of the
     * enemy from the game and the dropping of resources.
     */
    public void die() {
        drop();
    }

    /**
     * Process an attack by triggering projectiles or attacking a tower.
     */
    public void attack() {}

    public String toString() {
        return "Enemy: " + this.id + " Abilities: " + this.abilities + " Drops: "
                + this.drops + " Views: " + this.views + " State: " + this.state
                + " Speed: " + this.speed + " Full Health: " + this.fullHeath
                + " Current Health: " + this.health + " Base Attack: " + this.baseAttack;
    }


}
