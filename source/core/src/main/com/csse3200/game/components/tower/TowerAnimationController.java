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
    AnimationRenderComponent animator;
    Sound deploySound = ServiceLocator.getResourceService().getAsset(
            "sounds/deploy.mp3", Sound.class);
    Sound attackSound = ServiceLocator.getResourceService().getAsset(
            "sounds/gun_shot_trimmed.mp3", Sound.class);
    Sound stowSound = ServiceLocator.getResourceService().getAsset(
            "sounds/stow.mp3", Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("idleStart", this::animateIdle);
        entity.getEvents().addListener("stowStart", this::animateStow);
        entity.getEvents().addListener("deployStart", this::animateDeploy);
        entity.getEvents().addListener("firingStart", this::animateFiring);
    }

    void animateIdle() {
        animator.startAnimation("idle");
    }

    void animateStow() {
        animator.startAnimation("stow");
        stowSound.play();
    }

    void animateDeploy() {
        animator.startAnimation("deploy");
        deploySound.play();
    }

    void animateFiring() {
        animator.startAnimation("firing");
        attackSound.play();
    }
}