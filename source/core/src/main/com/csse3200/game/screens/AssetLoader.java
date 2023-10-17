package com.csse3200.game.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AssetLoader {
    private static final Set<String> loadedAssets = new HashSet<>();
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
            "images/projectiles/mobBoss_projectile.png",
            "images/projectiles/snow_ball.png",
            "images/projectiles/burn_effect.png",
            "images/projectiles/stun_effect.png",
            "images/projectiles/firework_anim.png",
            "images/projectiles/pierce_anim.png",
            "images/projectiles/snow_ball.png",
            "images/mobboss/demon.png",
            "images/mobboss/demon2.png",
            "images/mobs/fire_worm.png",
            "images/mobboss/patrick.png",
            "images/GrassTile/grass_tile_1.png",
            "images/GrassTile/grass_tile_2.png",
            "images/GrassTile/grass_tile_3.png",
            "images/GrassTile/grass_tile_4.png",
            "images/GrassTile/grass_tile_5.png",
            "images/GrassTile/grass_tile_6.png",
            "images/GrassTile/grass_tile_7.png",
            "images/highlight_tile.png",
            "images/ui/Sprites/UI_Glass_Toggle_Bar_01a.png",
            "images/green_tile.png",
            "images/red_tile.png",
            "images/HelpScreen/hs.jpg"
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
            "images/projectiles/mobBoss_projectile.atlas",
            "images/projectiles/snow_ball.atlas",
            "images/projectiles/pierce_anim.atlas",
            "images/projectiles/burn_effect.atlas",
            "images/projectiles/firework_anim.atlas",
            "images/projectiles/mobProjectile.atlas",
            "images/projectiles/stun_effect.atlas",
            "images/mobboss/demon.atlas",
            "images/mobs/fire_worm.atlas",
            "images/mobs/dragon_knight.atlas",
            "images/mobs/skeleton.atlas",
            "images/mobs/wizard.atlas",
            "images/mobs/water_queen.atlas",
            "images/mobs/water_slime.atlas",
            "images/mobboss/patrick.atlas",
            "images/mobboss/iceBaby.atlas"
    };


    public static final String[] music = {
            "sounds/background/Sci-Fi1.ogg"
    };

    public static final String[] Sounds = {
            "sounds/ui/Open_Close/NA_SFUI_Vol1_Open_01.ogg",
            "sounds/Impact4.ogg",
            "sounds/economy/click.wav",
            "sounds/economy/click_1.wav",
            "sounds/economy/buildSound.ogg",
            "sounds/towers/gun_shot_trimmed.mp3",
            "sounds/towers/deploy.mp3",
            "sounds/towers/stow.mp3",
            "sounds/engineers/firing_auto.mp3",
            "sounds/engineers/firing_single.mp3",
            "sounds/projectiles/on_collision.mp3",
            "sounds/projectiles/explosion.mp3",
            "sounds/waves/wave-start/Wave_Start_Alarm.ogg",
            "sounds/waves/wave-end/Wave_Over_01.ogg",
            "sounds/mobBoss/iceBabySound.mp3",
            "sounds/mobBoss/mobSpawnStomp.mp3",
            "sounds/mobBoss/iceBabyAOE.mp3",
            "sounds/mobs/wizardSpell.mp3",
            "sounds/mobs/waterQueenSpell.mp3",
            "sounds/mobs/boneBreak.mp3",
            "sounds/mobs/fireWormRoar.mp3",
            "sounds/mobBoss/demonBreath.mp3",
            "sounds/mobBoss/demonSpawn.wav",
            "sounds/mobBoss/demonAttack.wav",
            "sounds/mobBoss/demonBreathIn.mp3",
            "sounds/mobBoss/demonLand.mp3",
            "sounds/mobBoss/demonJump.mp3",
            "sounds/mobBoss/demonHeal.mp3",
            "sounds/mobBoss/demonCleave.mp3",
            "sounds/mobBoss/demonDeath.mp3",
            "sounds/mobBoss/slimeySplat.mp3",
            "sounds/mobBoss/slimeJump.mp3",
            "sounds/mobBoss/slimePop.mp3",
            "sounds/mobBoss/patrickAttack.mp3",
            "sounds/mobBoss/patrickAppear.mp3",
            "sounds/mobBoss/patrickScream.mp3",
            "sounds/mobBoss/patrickSpell.mp3",
            "sounds/mobBoss/patrickSpawn.mp3",
            "sounds/mobBoss/patrickCast.mp3",
            "sounds/mobBoss/patrickThunder.mp3",
            "sounds/mobBoss/patrickHit.mp3",
            "sounds/ui/click/click_01.ogg",
            "sounds/ui/hover/hover_01.ogg",
            "sounds/ui/open_close/close_01.ogg",
            "sounds/ui/open_close/open_01.ogg",
            "sounds/ui/switch/switch_01.ogg",
            "sounds/background/desert/Elements.ogg",
            "sounds/background/desert/Rocks1.ogg",
            "sounds/background/desert/Rocks2.ogg",
            "sounds/background/ice/Sequences1.ogg",
            "sounds/background/ice/Sequences2.ogg",
            "sounds/background/ice/Sequences3.ogg",
            "sounds/background/lava/Burst.ogg",
            "sounds/background/lava/Glitch_ripples.ogg",
            "sounds/background/lava/Sizzling.ogg",
            "sounds/background/lava/Swoosh.ogg",
            "sounds/background/loss/RisingScreams.ogg",
            "sounds/waves/wave-start/Wave_Start_Alarm.ogg",
            "sounds/waves/wave-end/Wave_Over_01.ogg"
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

    public static boolean areAllAssetsLoaded() {
        ResourceService resourceService = ServiceLocator.getResourceService();

        return loadedAssets.containsAll(Arrays.asList(textures)) &&
                loadedAssets.containsAll(Arrays.asList(textureAtlases)) &&
                loadedAssets.containsAll(Arrays.asList(Sounds)) &&
                loadedAssets.containsAll(Arrays.asList(music));
    }
}
