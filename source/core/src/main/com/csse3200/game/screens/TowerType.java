package com.csse3200.game.screens;

public enum TowerType {
    WEAPON("Weapon Tower", "The Weapon Tower is a simple and basic turret that fires rapid shots at enemies dealing damage over time.",
            0, "0", "images/turret-select/Weapon-Tower-Default.png", "images/turret-select/weapon-tower-selected.png"),
    TNT("TNT Tower", "The TNT Tower launches explosive projectiles, dealing area damage to groups of enemies.",
            1, "0", "images/turret-select/tnt-tower-default.png", "images/turret-select/tnt-tower-selected.png"),
    DROID("Droid Tower", "Droid Towers deploy robotic helpers that assist in combat and provide support to nearby turrets.",
            2, "0", "images/turret-select/droid-tower-default.png", "images/turret-select/droid-tower-selected.png"),
    WALL("Wall Tower", "The Wall Tower creates barriers to block enemy paths, slowing down their progress.",
            3, "100", "images/turret-select/wall-tower-default.png", "images/turret-select/wall-tower-selected.png"),
    FIRE("Fire Tower", "The Fire Tower emits flames, causing damage over time to enemies caught in its fiery radius.",
            4, "0", "images/turret-select/fire-tower-default.png", "images/turret-select/fire-tower-selected.png"),
    STUN("Stun Tower", "The Stun Tower releases electric shocks that temporarily immobilize and damage enemies.",
            5, "1000", "images/turret-select/stun-tower-default.png", "images/turret-select/stun-tower-selected.png"),
    INCOME("Income Tower", "The Income Tower generates additional in-game currency over time.",
            5, "0", "images/turret-select/mine-tower-default.png", "images/turret-select/mine-tower-selected.png"),
    PIERCE("Pierce Tower", "The Pierce Tower fires a projectile that pierces through targets and does not dissipate upon contact.",
            6, "0", "images/turret-select/pierce-tower-default.png", "images/turret-select/pierce-tower-selected.png"),
    RICOCHET("Ricochet Tower", "The Ricochet Tower fires a projectile that upon contact does damage and changes direction",
            7, "0", "images/turret-select/ricochet-tower-default.png", "images/turret-select/ricochet-tower-selected.png"),
    FIREWORK("Firework Tower", "The Firework Tower fires a projectile that splits on contact with its target",
            8, "0", "images/turret-select/firework-tower-default.png", "images/turret-select/firework-tower-selected.png");

    private final String towerName;
    private final String description;
    private final int id;
    private final String cost;
    private final String defaultImage;
    private final String clickedImage;


    TowerType(String towerName, String description, int id, String cost, String defaultImage, String clickedImage) {
        this.towerName = towerName;
        this.description = description;
        this.id = id;
        this.cost = cost;
        this.defaultImage = defaultImage;
        this.clickedImage = clickedImage;
    }

    public int getID() { return id; }

    public String getTowerName() { return towerName; }

    public String getDescription() { return description; }

    public String getPrice() { return cost; }

    public String getDefaultImage() {return defaultImage;}

    public String getClickedImage() {return clickedImage;}

}
