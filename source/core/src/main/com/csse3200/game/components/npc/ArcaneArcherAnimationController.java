package com.csse3200.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;


/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ArcaneArcherAnimationController extends Component {
    
    AnimationRenderComponent animator;
    
    private static final String ATTACK_SOUND = "sounds/mobs/archerArrow.mp3";
     Sound attackSound = ServiceLocator.getResourceService().getAsset(
             ATTACK_SOUND, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("mob_walk", this::animateWalk);
        entity.getEvents().addListener("mob_attack", this::animateAttack);
        entity.getEvents().addListener("mob_death", this::animateDeath);
        entity.getEvents().addListener("mob_dodge", this::animateDodge);

    }

    void animateWalk() {
        animator.startAnimation("arcane_archer_run");
    }

    void animateAttack() {
        animator.startAnimation("arcane_archer_attack");
        attackSound.setVolume(1000, 5.5f);
        attackSound.play();
    }

    void animateDeath() {
        animator.startAnimation("arcane_archer_death");
    }

    void animateDodge() {
        animator.startAnimation("arcane_archer_dodge");
    }
}

