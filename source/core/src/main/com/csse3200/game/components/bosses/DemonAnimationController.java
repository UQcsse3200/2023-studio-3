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
}
