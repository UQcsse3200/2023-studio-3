package com.csse3200.game.components.bosses;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class PatrickAnimationController extends Component {
    AnimationRenderComponent animator;
    Sound patrickAttack = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickAttack.mp3", Sound.class);
    Sound patrickAppear = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickAppear.mp3", Sound.class);
    Sound patrickScream = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickScream.mp3", Sound.class);
    Sound patrickSpell = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickSpell.mp3", Sound.class);
    Sound patrickSpawn = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickSpawn.mp3", Sound.class);
    Sound patrickCast = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickCast.mp3", Sound.class);
    Sound patrickThunder = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickThunder.mp3", Sound.class);
    Sound patrickHit = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickHit.mp3", Sound.class);

    /**
     * Creation call for a DemonAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("patrick_attack", this::patrickAttack);
        entity.getEvents().addListener("patrick_cast", this::patrickCast);
        entity.getEvents().addListener("patrick_death", this::patrickDeath);
        entity.getEvents().addListener("patrick_hurt", this::patrickHurt);
        entity.getEvents().addListener("patrick_idle", this::patrickIdle);
        entity.getEvents().addListener("patrick_spell", this::patrickSpell);
        entity.getEvents().addListener("patrick_walk", this::patrickWalk);
        entity.getEvents().addListener("patrick_appear_sound", this::patrickAppearSound);
        entity.getEvents().addListener("patrick_scream_sound", this::patrickScreamSound);
        entity.getEvents().addListener("patrick_spawn_sound", this::patrickSpawnSound);
        entity.getEvents().addListener("patrick_cast_sound", this::patrickCastSound);
        entity.getEvents().addListener("patrick_thunder_sound", this::patrickThunderSound);
        entity.getEvents().addListener("patrick_hit_sound", this::patrickHitSound);

    }

    private void patrickAttack() {
        animator.startAnimation("patrick_attack");
        patrickAttack.setVolume(1000, 5.5f);
        patrickAttack.play();
    }
    private void patrickCast() {
        animator.startAnimation("patrick_cast");
    }
    private void patrickDeath() {
        animator.startAnimation("patrick_death");
    }
    private void patrickHurt() {
        animator.startAnimation("patrick_hurt");
    }
    private void patrickIdle() {
        animator.startAnimation("patrick_idle");
    }
    private void patrickSpell() {
        animator.startAnimation("patrick_spell");
        patrickSpell.setVolume(1000, 5.5f);
        patrickSpell.play();
    }
    private void patrickWalk() {
        animator.startAnimation("patrick_walk");
    }
    private void patrickAppearSound() {
        patrickAppear.setVolume(1000, 5.5f);
        patrickAppear.play();
    }
    private void patrickScreamSound() {
        patrickScream.setVolume(1000, 5.5f);
        patrickScream.play();
    }
    private void patrickSpawnSound() {
        patrickSpawn.setVolume(1000, 5.5f);
        patrickSpawn.play();
    }
    private void patrickCastSound() {
        patrickCast.setVolume(1000, 5.5f);
        patrickCast.play();
    }
    private void patrickThunderSound() {
        patrickThunder.setVolume(1000, 5.5f);
        patrickThunder.play();
    }
    private void patrickHitSound() {
        patrickHit.setVolume(1000, 5.5f);
        patrickHit.play();
    }
}