package com.csse3200.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class SkeletonAnimationController extends Component {
    AnimationRenderComponent animator;

     /** Sound variables */
     private static final String ATTACK_SOUND = "sounds/mobs/boneBreak.mp3";
     Sound deathSound = ServiceLocator.getResourceService().getAsset(
             ATTACK_SOUND, Sound.class);

     private static final String DEATH_SOUND = "sounds/mobs/skeletonHit.mp3";
     Sound attackSound = ServiceLocator.getResourceService().getAsset(
             DEATH_SOUND, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("mob_walk", this::animateWalk);
        entity.getEvents().addListener("mob_attack", this::animateAttack);
        entity.getEvents().addListener("mob_death", this::animateDeath);
    }

    void animateWalk() {
        animator.startAnimation("skeleton_walk");
    }

    void animateAttack() {
        animator.startAnimation("skeleton_attack");
        attackSound.setVolume(1000, 0);
        attackSound.play();
    }

    void animateDeath() {
        animator.startAnimation("skeleton_death");
        deathSound.setVolume(1000, 5.5f);
        deathSound.play();
    }
}

