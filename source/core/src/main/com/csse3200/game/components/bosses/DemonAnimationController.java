package com.csse3200.game.components.bosses;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class DemonAnimationController extends Component {
    AnimationRenderComponent animator;
//    private static final String DEMON_JUMP = "sounds/mobBoss/demonBreath.mp3";
    Sound demonBreath = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonBreath.mp3", Sound.class);
    Sound demonSpawn = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonSpawn.wav", Sound.class);
    Sound demonAttack = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonAttack.wav", Sound.class);
    Sound demonBreathIn = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonBreathIn.mp3", Sound.class);
    Sound demonLand = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonLand.mp3", Sound.class);
    Sound demonRoar = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonJump.mp3", Sound.class);
    Sound demonHeal = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonHeal.mp3", Sound.class);
    Sound demonCleave = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonCleave.mp3", Sound.class);
    Sound demonDeath = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/demonDeath.mp3", Sound.class);
    Sound slimeySplat = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/slimeySplat.mp3", Sound.class);
    Sound slimeJump = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/slimeJump.mp3", Sound.class);
    Sound slimePop = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/slimePop.mp3", Sound.class);
    Sound spawnDemonSlime = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/spawnDemonSlime.mp3", Sound.class);

    /**
     * Creation call for a DemonAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("demon_walk", this::demonWalk);
        entity.getEvents().addListener("demon_cleave", this::demonCleave);
        entity.getEvents().addListener("demon_idle", this::demonIdle);
        entity.getEvents().addListener("demon_death", this::demonDeath);
        entity.getEvents().addListener("demon_cast_spell", this::demonCastSpell);
        entity.getEvents().addListener("demon_fire_breath", this::demonFireBreath);
        entity.getEvents().addListener("demon_smash", this::demonSmash);
        entity.getEvents().addListener("demon_take_hit", this::demonTakeHit);
        entity.getEvents().addListener("idle", this::idle);
        entity.getEvents().addListener("move", this::move);
        entity.getEvents().addListener("projectile_explosion", this::projectileExplosion);
        entity.getEvents().addListener("projectile_idle", this::projectileIdle);
        entity.getEvents().addListener("take_hit", this::takeHit);
        entity.getEvents().addListener("transform", this::transform);
        entity.getEvents().addListener("demon_spawn_sound", this::demonSpawnSound);
        entity.getEvents().addListener("demon_attack_sound", this::demonAttackSound);
        entity.getEvents().addListener("demon_breath_in_sound", this::demonBreathInSound);
        entity.getEvents().addListener("demon_landing_sound", this::demonLandSound);
        entity.getEvents().addListener("demon_roar_sound", this::demonRoarSound);
        entity.getEvents().addListener("demon_heal_sound", this::demonHealSound);
        entity.getEvents().addListener("demon_cleave_sound", this::demonCleaveSound);
        entity.getEvents().addListener("demon_death_sound", this::demonDeathSound);
        entity.getEvents().addListener("slimey_splat_sound", this::slimeySplatSound);
        entity.getEvents().addListener("slime_jump_sound", this::slimeJumpSound);
        entity.getEvents().addListener("slime_pop_sound", this::slimePopSound);
        entity.getEvents().addListener("spawn_demon_slime", this::spawnDemonSlimeSound);
    }

    private void demonWalk() {
        animator.startAnimation("demon_walk");
    }
    private void demonCleave() {
        animator.startAnimation("demon_cleave");
    }
    private void demonIdle() {
        animator.startAnimation("demon_idle");
    }
    private void demonDeath() {
        animator.startAnimation("demon_death");
    }
    private void demonCastSpell() {
        animator.startAnimation("demon_cast_spell");
    }
    private void demonFireBreath() {
        animator.startAnimation("demon_fire_breath");
        demonBreath.setVolume(1000,5.5f);
        demonBreath.play();
    }
    private void demonSmash() {
        animator.startAnimation("demon_smash");
    }
    private void demonTakeHit() {
        animator.startAnimation("demon_take_hit");
    }
    private void idle() {
        animator.startAnimation("idle");
    }
    private void move() {
        animator.startAnimation("move");
    }
    private void projectileExplosion() {
        animator.startAnimation("projectile_explosion");
    }
    private void projectileIdle() {
        animator.startAnimation("projectile_idle");
    }
    private void takeHit() {
        animator.startAnimation("take_hit");
    }
    private void transform() {
        animator.startAnimation("transform");
    }
    private void demonSpawnSound() {
        demonSpawn.setVolume(1000,5.5f);
        demonSpawn.play();
    }
    private void demonAttackSound() {
        demonAttack.setVolume(1000,5.5f);
        demonAttack.play();
    }
    private void demonBreathInSound() {
        demonBreathIn.setVolume(1000,5.5f);
        demonBreathIn.play();
    }
    private void demonLandSound() {
        demonLand.setVolume(1000,5.5f);
        demonLand.play();
    }
    private void demonRoarSound() {
        demonRoar.setVolume(1000,5.5f);
        demonRoar.play();
    }
    private void demonHealSound() {
        demonHeal.setVolume(1000,5.5f);
        demonHeal.play();
    }
    private void demonCleaveSound() {
        demonCleave.setVolume(1000,5.5f);
        demonCleave.play();
    }
    private void demonDeathSound() {
        demonDeath.setVolume(1000,5.5f);
        demonDeath.play();
    }
    private void slimeySplatSound() {
        slimeySplat.setVolume(1000,5.5f);
        slimeySplat.play();
    }
    private void slimeJumpSound() {
        slimeJump.setVolume(1000,5.5f);
        slimeJump.play();
    }
    private void slimePopSound() {
        slimePop.setVolume(1000,5.5f);
        slimePop.play();
    }
    private void spawnDemonSlimeSound() {
        spawnDemonSlime.setVolume(1000,5.5f);
        spawnDemonSlime.play();
    }
}
