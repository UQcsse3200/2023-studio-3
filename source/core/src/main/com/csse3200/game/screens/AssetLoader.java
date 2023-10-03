package com.csse3200.game.screens;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class AssetLoader {
    // Define your asset file paths here
    public static final String[] textures = {
            "images/desert_bg.png",
            "images/ice_bg.png",
            "images/lava_bg.png",
            "images/projectiles/projectile.png",
            "images/ingamebg.png",
            "images/box_boy_leaf.png",
            "images/background/building1.png",
            "images/ghost_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/hex_grass_1.png",
            "images/background/mountain.png",
            "images/ghost_king.png",
            "images/ghost_1.png",
            "images/terrain 2 normal.png",
            "images/terrain 2 hex.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/towers/turret.png",
            "images/towers/turret01.png",
            "images/towers/turret_deployed.png",
            "images/towers/fire_tower_atlas.png",
            "images/towers/stun_tower.png",
            "images/background/building2.png",
            "images/mobs/robot.png",
            "images/mobs/boss2.png",
            "images/mobs/Attack_1.png",
            "images/mobs/Attack_2.png",
            "images/mobs/Charge_1.png",
            "images/mobs/Charge_2.png",
            "images/mobs/Dead.png",
            "images/mobs/Enabling-5.png",
            "images/mobs/satyr.png",
            "images/mobs/Hurt.png",
            "images/mobs/Idle.png",
            "images/mobs/rangeBossRight.png",
            "images/towers/wallTower.png",
            "images/background/building2.png",
            "images/iso_grass_3.png",
            "images/terrain_use.png",
            "images/Dusty_MoonBG.png",
            "images/economy/scrap.png",
            "images/economy/crystal.png",
            "images/economy/econ-tower.png",
            "images/projectiles/bossProjectile.png",
            "images/towers/mine_tower.png",
            "images/towers/TNTTower.png",
            "images/towers/DroidTower.png",
            "images/projectiles/basic_projectile.png",
            "images/projectiles/mobProjectile.png",
            "images/projectiles/engineer_projectile.png",
            "images/projectiles/mobKing_projectile.png",
            "images/projectiles/snow_ball.png",
            "images/projectiles/burn_effect.png",
            "images/projectiles/stun_effect.png",
            "images/projectiles/firework_anim.png",
            "images/projectiles/pierce_anim.png",
            "images/projectiles/snow_ball.png"
    };

    public static final String[] textureAtlases = {
            "images/economy/econ-tower.atlas",
            "images/terrain_iso_grass.atlas",
            "images/ghost.atlas",
            "images/mobs/boss2.atlas",
            "images/ghostKing.atlas",
            "images/towers/turret.atlas",
            "images/towers/turret01.atlas",
            "images/mobs/xenoGrunt.atlas",
            "images/towers/fire_tower_atlas.atlas",
            "images/towers/stun_tower.atlas",
            "images/mobs/xenoGruntRunning.atlas",
            "images/xenoGrunt.atlas",
            "images/mobs/robot.atlas",
            "images/mobs/rangeBossRight.atlas",
            "images/towers/DroidTower.atlas",
            "images/mobs/robot.atlas",
            "images/mobs/rangeBossRight.atlas",
            "images/towers/TNTTower.atlas",
            "images/projectiles/basic_projectile.atlas",
            "images/projectiles/bossProjectile.atlas",
            "images/projectiles/mobProjectile.atlas",
            "images/projectiles/mobProjectile.atlas",
            "images/projectiles/engineer_projectile.atlas",
            "images/projectiles/mobKing_projectile.atlas",
            "images/projectiles/snow_ball.atlas",
            "images/projectiles/pierce_anim.atlas",
            "images/projectiles/burn_effect.atlas",
            "images/projectiles/firework_anim.atlas",
            "images/projectiles/mobProjectile.atlas",
            "images/projectiles/stun_effect.atlas"
    };


    public static final String[] music = {
            "sounds/background/Sci-Fi1.ogg"
    };

    public static final String[] Sounds = {
            "sounds/Impact4.ogg",
            "sounds/economy/click.wav",
            "sounds/economy/click_1.wav",
            "sounds/towers/gun_shot_trimmed.mp3",
            "sounds/towers/deploy.mp3",
            "sounds/towers/stow.mp3",
            "sounds/engineers/firing_auto.mp3",
            "sounds/engineers/firing_single.mp3",
            "sounds/projectiles/on_collision.mp3",
            "sounds/projectiles/explosion.mp3",
    };


    public static void loadAllAssets() {
        ResourceService resourceService = ServiceLocator.getResourceService();

        resourceService.loadTextures(textures);
        resourceService.loadTextureAtlases(textureAtlases);
        resourceService.loadSounds(Sounds);
        resourceService.loadMusic(music);

        // Wait for the assets to finish loading (you can implement a loading screen)
        while (!resourceService.loadForMillis(10)) {
            // Display loading progress if needed
        }
    }

    public static void unloadAllAssets() {
        ResourceService resourceService = ServiceLocator.getResourceService();

        resourceService.unloadAssets(textures);
        resourceService.unloadAssets(textureAtlases);
        resourceService.unloadAssets(Sounds);
        resourceService.unloadAssets(music);
    }

    public static Texture getTexture(String assetPath) {
        return ServiceLocator.getResourceService().getAsset(assetPath, Texture.class);
    }

    public static TextureAtlas getTextureAtlas(String assetPath) {
        return ServiceLocator.getResourceService().getAsset(assetPath, TextureAtlas.class);
    }

    public static Sound getSound(String assetPath) {
        return ServiceLocator.getResourceService().getAsset(assetPath, Sound.class);
    }

    public static Music getMusic(String assetPath) {
        return ServiceLocator.getResourceService().getAsset(assetPath, Music.class);
    }
}

