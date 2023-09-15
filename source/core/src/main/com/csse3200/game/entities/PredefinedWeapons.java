package com.csse3200.game.entities;

import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.entities.configs.ProjectileConfig;
import com.csse3200.game.files.FileLoader;

public class PredefinedWeapons {
    // Melee attacks
    public static Melee sword = new Melee(10, 4, "fire", 1, 1);
    public static Melee punch = new Melee(3, 1, "air", 1, 1);
    public static Melee axe = new Melee(9, 3, "fire", 1, 1);
    public static Melee kick = new Melee(2, 1, "earth", 1, 1);

    //TODO import defined projectiles for mobs
    public static ProjectileConfig fireBall = new ProjectileConfig();
    public static ProjectileConfig frostBall = new ProjectileConfig();

}
