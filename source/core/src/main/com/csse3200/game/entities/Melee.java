package com.csse3200.game.entities;

import java.util.ArrayList;

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

    private final ArrayList<String> views;

    public Melee(int damage, int attackRange, String Element, int castTime, int cooldown, ArrayList<String> views) {
        this.damage = damage;
        this.attackRange = attackRange;
        this.element = Element;
        this.castTime = castTime;
        this.cooldown = cooldown;
        this.views = views;
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

    public ArrayList<String> getViews() {
        return views;
    }
}
