package com.csse3200.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import java.security.SecureRandom;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class SkeletonAnimationController extends Component {
    // // For on collision sounds later
    // private static final String COLLISION_SFX = "sounds/projectiles/on_collision.mp3";
    // Sound onCollisionSound = ServiceLocator.getResourceService().getAsset(
    //         COLLISION_SFX, Sound.class);
    AnimationRenderComponent animator;
    private SecureRandom rand = new SecureRandom();

     /** Sound variables */
     private static final String ATTACK_SOUND = "sounds/mobs/boneBreak.mp3";
     Sound deathSound = ServiceLocator.getResourceService().getAsset(
             ATTACK_SOUND, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderStart", this::animateWalk);
        entity.getEvents().addListener("shootStart", this::animateAttack);
        entity.getEvents().addListener("dieStart", this::animateDeath);
        entity.getEvents().addListener("freeze", this::animateFreeze);
    }

    void animateWalk() {
        animator.startAnimation("skeleton_walk");
    }

    void animateAttack() {
        animator.startAnimation("skeleton_attack");
    }

    void animateDeath() {
        animator.startAnimation("skeleton_death");
        deathSound.setVolume(1000, 5.5f);
        deathSound.play();
    }
	
	void animateFreeze()
	{
		animator.startAnimation("skeleton_freeze");
	}
}

