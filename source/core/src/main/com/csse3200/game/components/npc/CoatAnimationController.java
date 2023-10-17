package com.csse3200.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;


/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class CoatAnimationController extends Component {
    AnimationRenderComponent animator;

    private static final String ATTACK_SOUND = "sounds/mobs/coatAttack.mp3";
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
        animator.startAnimation("coat_run");
    }

    void animateAttack() {
        animator.startAnimation("coat_attack");
        attackSound.setVolume(1000, 5.5f);
        attackSound.play();
    }

    void animateDeath() {
        animator.startAnimation("coat_death");
    }
}

