package com.csse3200.game.components.tasks.MobTask;

public enum MobType {
    SKELETON(true),
    WIZARD(false),
    WATER_QUEEN(false),
    WATER_SLIME(true),
    FIRE_WORM(false),
    DRAGON_KNIGHT(true),
    COAT(true),
    NIGHT_BORNE(true),
    ARCANE_ARCHER(false),
    ROCKY(true),
    NECROMANCER(true),
    FIREWIZARD(true);
    private boolean isMelee;

    MobType(boolean melee) {
        this.isMelee = melee;
    }

    public boolean isMelee() {
        return isMelee;
    }
}
