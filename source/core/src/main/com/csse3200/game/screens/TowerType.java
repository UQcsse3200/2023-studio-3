package com.csse3200.game.screens;

public enum TowerType {
    WEAPON("images/towers/turret01.atlas", "Weapon Tower"),
    TNT("images/towers/TNTTower.atlas", "TNT Tower"),
    DROID("images/towers/DroidTower.atlas", "Droid Tower"),
    WALL("images/towers/wallTower.png", "Wall Tower"),
    FIRE("images/towers/fire_tower_atlas.atlas", "Fire Tower"),
    STUN("images/towers/stun_tower.atlas", "Stun Tower"),
    INCOME("images/economy/econ-tower.atlas", "Income Tower");

    private final String imagePath;
    private final String towerName;

    TowerType(String imagePath, String towerName) {
        this.imagePath = imagePath;
        this.towerName = towerName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTowerName() {
        return towerName;
    }
}
