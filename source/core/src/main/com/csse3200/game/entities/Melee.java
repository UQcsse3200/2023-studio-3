package com.csse3200.game.entities;

import java.util.ArrayList;

public class Melee implements Weapon {
    private final int damage;

    private final int attackRange;

    private final String Element;

    private final int castTime;

    private final int cooldown;

    private final ArrayList<String> views;

    public Melee(int damage, int attackRange, String Element, int castTime, int cooldown, ArrayList<String> views) {
        this.damage = damage;
        this.attackRange = attackRange;
        this.Element = Element;
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
        return Element;
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
