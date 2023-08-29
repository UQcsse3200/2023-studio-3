package com.csse3200.game.entities;

public class PredefinedWeapons {
    // Melee attacks
    public static Melee sword = new Melee(10, 4, "fire", 1, 1);
    public static Melee punch = new Melee(3, 1, "air", 1, 1);
    public static Melee axe = new Melee(9, 3, "fire", 1, 1);
    public static Melee kick = new Melee(2, 1, "earth", 1, 1);

    // Projectile attacks TODO: change Weapon and Melee to Projectile class
    public static Weapon fireBall = new Melee(9, 20, "fire", 1, 1);
    public static Weapon frostBall = new Melee(6, 20, "ice", 1, 1);
    public static Weapon hurricane = new Melee(7, 20, "air", 1, 1);
}
