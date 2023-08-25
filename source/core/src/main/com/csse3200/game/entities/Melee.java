package com.csse3200.game.entities;

import java.util.ArrayList;

public class Melee {
    private int damage;

    private int attackRange;

    private String Element;

    private int castTime;

    private int cooldown;

    private ArrayList<String> views;

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
