package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Listens for events relevant to a Human character (Just engineers at this stage)
 * Each event will trigger a certain animation
 */
public class HumanAnimationController extends Component {
    // Event name constants
    private static final String IDLE = "idleStart";
    private static final String RUN = "runStart";
    private static final String FIRING = "firingStart";
    private static final String HIT = "hitStart";
    private static final String DEATH = "deathStart";
    // Animation name constants
    private static final String IDLE_ANIM = "idle";
    private static final String RUN_ANIM = "run";
    private static final String FIRE_ANIM = "firing";
    private static final String HIT_ANIM = "hit";
    private static final String DEATH_ANIM = "death";
    // Sound effects constants
//    private static final String RUN_SFX = "run/todeploy.mp3";
//    private static final String FIRE_SFX = "sounds/gun_shot_trimmed.mp3";
//    private static final String HIT_SFX = "sounds/stow.mp3";
//    private static final String DEATH_SFX = "sounds/stow.mp3";

    AnimationRenderComponent animator;
//    Sound runSound = ServiceLocator.getResourceService().getAsset(
//            RUN_SFX, Sound.class);
//    Sound attackSound = ServiceLocator.getResourceService().getAsset(
//            FIRE_SFX, Sound.class);
//    Sound hitSound = ServiceLocator.getResourceService().getAsset(
//            HIT_SFX, Sound.class);
//    Sound deathSound = ServiceLocator.getResourceService().getAsset(
//            HIT_SFX, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(IDLE, this::animateIdle);
        entity.getEvents().addListener(RUN, this::animateRun);
        entity.getEvents().addListener(FIRING, this::animateFiring);
        entity.getEvents().addListener(HIT, this::animateHit);
        entity.getEvents().addListener(DEATH, this::animateDeath);
    }

    void animateIdle() {
        animator.startAnimation(IDLE_ANIM);
    }

    void animateRun() {
        animator.startAnimation(RUN_ANIM);
//        runSound.play();
    }

    void animateFiring() {
        animator.startAnimation(FIRE_ANIM);
//        attackSound.play();
    }

    void animateHit() {
        animator.startAnimation(HIT_ANIM);
//        hitSound.play();
    }

    void animateDeath() {
        animator.startAnimation(DEATH_ANIM);
//        deathSound.play();
    }
}