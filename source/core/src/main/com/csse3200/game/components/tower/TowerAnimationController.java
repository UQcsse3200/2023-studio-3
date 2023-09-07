package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Listens for events relevant to a weapon tower state.
 * Each event will trigger a certain animation
 */
public class TowerAnimationController extends Component {
    // Event name constants
    private static final String IDLE = "idleStart";
    private static final String DEPLOY = "deployStart";
    private static final String FIRING = "firingStart";
    private static final String STOW = "stowStart";
    // Animation name constants
    private static final String IDLE_ANIM = "idle";
    private static final String DEPLOY_ANIM = "deploy";
    private static final String FIRE_ANIM = "firing";
    private static final String STOW_ANIM = "stow";
    // Sound effects constants
    private static final String DEPLOY_SFX = "sounds/towers/deploy.mp3";
    private static final String FIRE_SFX = "sounds/towers/gun_shot_trimmed.mp3";
    private static final String STOW_SFX = "sounds/towers/stow.mp3";

    AnimationRenderComponent animator;
    Sound deploySound = ServiceLocator.getResourceService().getAsset(
            DEPLOY_SFX, Sound.class);
    Sound attackSound = ServiceLocator.getResourceService().getAsset(
            FIRE_SFX, Sound.class);
    Sound stowSound = ServiceLocator.getResourceService().getAsset(
            STOW_SFX, Sound.class);

    /**
     * Creation call for a TowerAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(IDLE, this::animateIdle);
        entity.getEvents().addListener(STOW, this::animateStow);
        entity.getEvents().addListener(DEPLOY, this::animateDeploy);
        entity.getEvents().addListener(FIRING, this::animateFiring);
    }

    /**
     * Starts the
     */
    void animateIdle() {
        animator.startAnimation(IDLE_ANIM);
    }

    void animateStow() {
        animator.startAnimation(STOW_ANIM);
        stowSound.play();
    }

    void animateDeploy() {
        animator.startAnimation(DEPLOY_ANIM);
        deploySound.play();
    }

    void animateFiring() {
        animator.startAnimation(FIRE_ANIM);
        attackSound.play();
    }
}