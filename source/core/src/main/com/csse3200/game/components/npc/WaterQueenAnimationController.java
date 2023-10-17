package com.csse3200.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class WaterQueenAnimationController extends Component {
    AnimationRenderComponent animator;

    private static final String ATTACK_SOUND = "sounds/mobs/waterQueenSpell.mp3";
    Sound attackSound = ServiceLocator.getResourceService().getAsset(
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
        animator.startAnimation("water_queen_walk");
    }

    void animateAttack() {
        animator.startAnimation("water_queen_attack");
        attackSound.setVolume(1000, 5.5f);
        attackSound.play();
    }

    void animateDeath() {
        animator.startAnimation("water_queen_death");
    }
	
    void animateFreeze() {
        animator.startAnimation("water_queen_freeze");
    }
}

