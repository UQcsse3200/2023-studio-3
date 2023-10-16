package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.ProjectileConfig;

public class PredefinedWeapons {

    private PredefinedWeapons() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // Melee attacks
    public static final Melee SWORD = new Melee(10, 4, "fire", 1, 1);
    public static final Melee PUNCH = new Melee(3, 1, "air", 1, 1);
    public static final Melee AXE = new Melee(9, 3, "fire", 1, 1);
    public static final Melee KICK = new Melee(2, 1, "earth", 1, 1);

    //TODO import defined projectiles for mobs
    public static final ProjectileConfig FIREBALL = new ProjectileConfig();
    public static final ProjectileConfig FROSTBALL = new ProjectileConfig();

}
