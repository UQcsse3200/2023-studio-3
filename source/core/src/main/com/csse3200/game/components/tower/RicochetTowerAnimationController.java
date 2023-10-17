package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class listens to events relevant to DroidTower entity's state and plays the animation when one
 * of the events is triggered.
 */
public class RicochetTowerAnimationController extends Component {
    private static final String FIRE_SINGLE_SFX = "sounds/towers/5.56_single_shot.mp3";

    private final Sound fireSingleSound = ServiceLocator.getResourceService().getAsset(
            FIRE_SINGLE_SFX, Sound.class);
    private AnimationRenderComponent animator;

    /**
     * Creation call for a DroidAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("startIdle", this::animateDefault);
        entity.getEvents().addListener("startAttack", this::animateAttack);
        entity.getEvents().addListener("startDeath", this::animateDeath);

    }


    /**
     * THIS IS TO SHOW THE TOWER DEATH IN THIS SITUATION THE TOWER GETS BLASTED
     */
    void animateAttack() {
        animator.startAnimation("Attack");
        fireSingleSound.play();
    }


    void animateDeath() {
        animator.startAnimation("Death");
    }


    /**
     * Triggers the "default" or "Idle animation for the entity.
     * This method should be invoked when the entity returns to its default state.
     */
    void animateDefault() {
        animator.startAnimation("Idle");
    }


}