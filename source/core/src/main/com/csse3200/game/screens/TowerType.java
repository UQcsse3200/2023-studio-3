package com.csse3200.game.screens;

public enum TowerType {
    WEAPON("images/towers/turret_deployed.png", "Weapon Tower",
            "The Weapon Tower is a simple and basic turret that fires rapid shots at enemies dealing damage over time."),
    TNT("images/towers/turret_deployed.png", "TNT Tower",
            "The TNT Tower launches explosive projectiles, dealing area damage to groups of enemies."),
    DROID("images/towers/turret_deployed.png", "Droid Tower",
            "Droid Towers deploy robotic helpers that assist in combat and provide support to nearby turrets."),
    WALL("images/towers/turret_deployed.png", "Wall Tower",
            "The Wall Tower creates barriers to block enemy paths, slowing down their progress."),
    FIRE("images/towers/turret_deployed.png", "Fire Tower",
            "The Fire Tower emits flames, causing damage over time to enemies caught in its fiery radius."),
    STUN("images/towers/turret_deployed.png", "Stun Tower",
            "The Stun Tower releases electric shocks that temporarily immobilize and damage enemies."),
    INCOME("images/towers/turret_deployed.png", "Income Tower",
            "The Income Tower generates additional in-game currency over time.");

    private final String imagePath;
    private final String towerName;
    private final String description;

    TowerType(String imagePath, String towerName, String description) {
        this.imagePath = imagePath;
        this.towerName = towerName;
        this.description = description;
    }

    public String getImagePath() { return imagePath; }

    public String getTowerName() { return towerName; }

    public String getDescription() { return description; }

}
