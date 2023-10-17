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
public class DragonKnightAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("mob_walk", this::animateWalk);
        entity.getEvents().addListener("mob_attack", this::animateAttack);
        entity.getEvents().addListener("mob_death", this::animateDeath);

        entity.getEvents().addListener("wanderStart", this::animateWalk);
        entity.getEvents().addListener("shootStart", this::animateAttack);
        entity.getEvents().addListener("dieStart", this::animateDeath);
        entity.getEvents().addListener("freeze", this::animateFreeze);
    }

    void animateWalk() {
        animator.startAnimation("dragon_knight_run");
    }

    void animateAttack() {
        animator.startAnimation("dragon_knight_attack");
    }

    void animateDeath() {
        animator.startAnimation("dragon_knight_death");
    }

	void animateFreeze()
	{
		animator.startAnimation("dragon_knight_freeze");
	}
}

