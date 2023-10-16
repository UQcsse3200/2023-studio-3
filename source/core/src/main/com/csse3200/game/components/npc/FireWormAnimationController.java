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
public class FireWormAnimationController extends Component {
    // // For on collision sounds later
    // private static final String COLLISION_SFX = "sounds/projectiles/on_collision.mp3";
    // Sound onCollisionSound = ServiceLocator.getResourceService().getAsset(
    //         COLLISION_SFX, Sound.class);
    AnimationRenderComponent animator;
    private SecureRandom rand = new SecureRandom();

    private static final String ATTACK_SOUND = "sounds/mobs/fireWormRoar.mp3";
    Sound attackSound = ServiceLocator.getResourceService().getAsset(
            ATTACK_SOUND, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("mob_walk", this::animateWalk);
        entity.getEvents().addListener("mob_attack", this::animateAttack);
        entity.getEvents().addListener("mob_death", this::animateDeath);
        entity.getEvents().addListener("default", this::stopAnimation);
    }

    void animateWalk() {
        animator.startAnimation("fire_worm_walk");
    }

    void animateAttack() {
        animator.startAnimation("fire_worm_attack");
        attackSound.setVolume(1000, 5.5f);
        attackSound.play();
    }

    void animateDeath() {
        animator.startAnimation("fire_worm_death");
    }

    void stopAnimation() {
        animator.startAnimation("default");
    }

	public void animateFreeze()
	{
		animator.startAnimation("fire_worm_freeze");
	}
}

