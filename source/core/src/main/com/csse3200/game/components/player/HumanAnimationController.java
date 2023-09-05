package com.csse3200.game.components.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Listens for events relevant to a Human character (Just engineers at this stage)
 * Each event will trigger a certain animation
 */
public class HumanAnimationController extends Component {
    // Event name constants
    private static final String IDLEL = "idleLeft";
    private static final String IDLER = "idleRight";
    private static final String WALKL = "walkLeftStart";
    private static final String WALKR = "walkRightStart";
    private static final String FIRING = "firingStart";
    private static final String HIT = "hitStart";
    private static final String DEATH = "deathStart";
    // Animation name constants
    private static final String IDLEL_ANIM = "idle_left";
    private static final String IDLER_ANIM = "idle_right";
    private static final String WALKL_ANIM = "walk_left";
    private static final String WALKR_ANIM = "walk_right";
    private static final String FIRE_ANIM = "firing";
    private static final String HIT_ANIM = "hit";
    private static final String DEATH_ANIM = "death";
    // Sound effects constants
//    private static final String RUN_SFX = "run/todeploy.mp3";
    private static final String FIRE_AUTO_SFX = "sounds/engineers/firing_auto.mp3";
//    private static final String FIRE_SINGLE_SFX = "sounds/engineers/firing_single.mp3";
//    private static final String HIT_SFX = "sounds/stow.mp3";
//    private static final String DEATH_SFX = "sounds/stow.mp3";

    AnimationRenderComponent animator;
//    Sound runSound = ServiceLocator.getResourceService().getAsset(
//            RUN_SFX, Sound.class);
    Sound fireAutoSound = ServiceLocator.getResourceService().getAsset(
            FIRE_AUTO_SFX, Sound.class);
//    Sound fireSingleSound = ServiceLocator.getResourceService().getAsset(
//            FIRE_SINGLE_SFX, Sound.class);
//    Sound hitSound = ServiceLocator.getResourceService().getAsset(
//            HIT_SFX, Sound.class);
//    Sound deathSound = ServiceLocator.getResourceService().getAsset(
//            HIT_SFX, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(IDLEL, this::animateIdleLeft);
        entity.getEvents().addListener(IDLER, this::animateIdleRight);
        entity.getEvents().addListener(WALKL, this::animateLeftWalk);
        entity.getEvents().addListener(WALKR, this::animateRightWalk);
        entity.getEvents().addListener(FIRING, this::animateFiring);
        entity.getEvents().addListener(HIT, this::animateHit);
        entity.getEvents().addListener(DEATH, this::animateDeath);
    }

    void animateIdleLeft() {
        animator.startAnimation(IDLEL_ANIM);
    }
    void animateIdleRight() {
        animator.startAnimation(IDLER_ANIM);
    }

    void animateLeftWalk() {
        animator.startAnimation(WALKL_ANIM);
//        runSound.play();
    }
    void animateRightWalk() {
        animator.startAnimation(WALKR_ANIM);
//        runSound.play();
    }

    void animateFiring() {
        animator.startAnimation(FIRE_ANIM);
        fireAutoSound.play();
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