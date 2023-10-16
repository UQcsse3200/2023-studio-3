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
public class WizardAnimationController extends Component {
    // // For on collision sounds later
    // private static final String COLLISION_SFX = "sounds/projectiles/on_collision.mp3";
    // Sound onCollisionSound = ServiceLocator.getResourceService().getAsset(
    //         COLLISION_SFX, Sound.class);
    AnimationRenderComponent animator;
    private SecureRandom rand = new SecureRandom();

    /** Sound variables */
    private static final String ATTACK_SOUND = "sounds/mobs/wizardSpell.mp3";
    Sound attackSound = ServiceLocator.getResourceService().getAsset(
            ATTACK_SOUND, Sound.class);
    

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("mob_walk", this::animateWalk);
        entity.getEvents().addListener("mob_attack", this::animateAttack);
        entity.getEvents().addListener("mob_death", this::animateDeath);


    }

    void animateWalk() {
        animator.startAnimation("wizard_run");
    }

    void animateAttack() {
        animator.startAnimation("wizard_attack");
        attackSound.setVolume(1000, 5.5f);
        attackSound.play();
    }

    void animateDeath() {
        animator.startAnimation("wizard_death");
    }
	
	public void animateFreeze()
	{
		animator.startAnimation("wizard_freeze");
	}
}

