package com.csse3200.game.screens;

public enum TowerType {
    WEAPON("images/towers/turret_deployed.png", "Weapon Tower"),
    TNT("images/towers/turret_deployed.png", "TNT Tower"),
    DROID("images/towers/turret_deployed.png", "Droid Tower"),
    WALL("images/towers/turret_deployed.png", "Wall Tower"),
    FIRE("images/towers/turret_deployed.png", "Fire Tower"),
    STUN("images/towers/turret_deployed.png", "Stun Tower"),
    INCOME("images/towers/turret_deployed.png", "Income Tower");

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
