package com.csse3200.game.screens;

public enum TowerType {
    WEAPON("Weapon Tower", "The Weapon Tower is a simple and basic turret that fires rapid shots at enemies dealing damage over time.", 0, "0"),
    TNT("TNT Tower", "The TNT Tower launches explosive projectiles, dealing area damage to groups of enemies.", 1, "0"),
    DROID("Droid Tower", "Droid Towers deploy robotic helpers that assist in combat and provide support to nearby turrets.", 2, "0"),
    WALL("Wall Tower", "The Wall Tower creates barriers to block enemy paths, slowing down their progress.", 3, "0"),
    FIRE("Fire Tower", "The Fire Tower emits flames, causing damage over time to enemies caught in its fiery radius.", 4, "0"),
    STUN("Stun Tower", "The Stun Tower releases electric shocks that temporarily immobilize and damage enemies.", 5, "0"),
    INCOME("Income Tower", "The Income Tower generates additional in-game currency over time.", 5, "0");

    private final String towerName;
    private final String description;
    private final int id;
    private final String cost;


    TowerType(String towerName, String description, int id, String cost) {
        this.towerName = towerName;
        this.description = description;
        this.id = id;
        this.cost = cost;
    }

    public int getID() { return id; }

    public String getTowerName() { return towerName; }

    public String getDescription() { return description; }

    public String getPrice() { return cost; }

}
