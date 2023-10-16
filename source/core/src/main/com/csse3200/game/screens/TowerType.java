package com.csse3200.game.screens;

public enum TowerType {
    WEAPON("Weapon Tower", "weapon_tower", "The Weapon Tower is a simple and basic turret that fires rapid shots at enemies dealing damage over time.",
            0, "15", "images/turret-select/Weapon-Tower-Default.png", "images/turret-select/Weapon-Tower-Clicked.png"),
    TNT("TNT Tower", "tnt_tower", "The TNT Tower launches explosive projectiles, dealing area damage to groups of enemies.",
            1, "30", "images/turret-select/tnt-tower-default.png", "images/turret-select/tnt-tower-clicked.png"),
    DROID("Droid Tower", "droid_tower", "Droid Towers deploy robotic helpers that assist in combat and provide support to nearby turrets.",
            2, "45", "images/turret-select/droid-tower-default.png", "images/turret-select/droid-tower-clicked.png"),
    WALL("Wall Tower", "wall", "The Wall Tower creates barriers to block enemy paths, slowing down their progress.",
            3, "45", "images/turret-select/wall-tower-default.png", "images/turret-select/wall-tower-clicked.png"),
    FIRE("Fire Tower", "fire_tower", "The Fire Tower emits flames, causing damage over time to enemies caught in its fiery radius.",
            4, "45", "images/turret-select/fire-tower-default.png", "images/turret-select/fire-tower-clicked.png"),
    STUN("Stun Tower", "stun_tower", "The Stun Tower releases electric shocks that temporarily immobilize and damage enemies.",
            5, "45", "images/turret-select/stun-tower-default.png", "images/turret-select/stun-tower-clicked.png"),
    INCOME("Income Tower", "income_tower", "The Income Tower generates additional in-game currency over time.",
            6, "10", "images/turret-select/mine-tower-default.png", "images/turret-select/mine-tower-clicked.png");

    private final String towerName;
    private final String skinName;
    private final String description;
    private final int id;
    private final String cost;
    private final String defaultImage;
    private final String clickedImage;


    TowerType(String towerName, String skinName, String description, int id, String cost, String defaultImage, String clickedImage) {
        this.towerName = towerName;
        this.skinName = skinName;
        this.description = description;
        this.id = id;
        this.cost = cost;
        this.defaultImage = defaultImage;
        this.clickedImage = clickedImage;
    }

    public int getID() { return id; }

    public String getTowerName() { return towerName; }

    public String getSkinName() { return skinName; }

    public String getDescription() { return description; }

    public String getPrice() { return cost; }

    public String getDefaultImage() {return defaultImage;}

    public String getClickedImage() {return clickedImage;}

}
