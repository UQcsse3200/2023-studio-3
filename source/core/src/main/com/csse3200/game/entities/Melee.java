package com.csse3200.game.entities;

/**
 * Melee is a Weapon which has a minimum range to be used at
 *
 * damage: the damage of the weapon
 * attackRange: the minimum range of the weapon (target must be less than or equal to this distance away)
 * element: the element of the weapon (fire, water, earth, air, etc)
 * castTime: the time it takes to cast the weapon
 * cooldown: the time it takes to be used again
 * */
public class Melee implements Weapon {
    private final int damage;

    private final int attackRange;

    private final String element;

    private final int castTime;

    private final int cooldown;


    public Melee(int damage, int attackRange, String element, int castTime, int cooldown) {
        this.damage = damage;
        this.attackRange = attackRange;
        this.element = element;
        this.castTime = castTime;
        this.cooldown = cooldown;
    }

    public int getDamage() {
        return damage;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public String getElement() {
        return element;
    }

    public int getCastTime() {
        return castTime;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String toString() {
        return "Melee: " + damage + " " + attackRange + " " + element;
    }
}
