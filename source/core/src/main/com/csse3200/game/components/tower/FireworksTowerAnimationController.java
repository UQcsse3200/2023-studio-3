package com.csse3200.game.components.tower;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to DroidTower entity's state and plays the animation when one
 * of the events is triggered.
 */
public class FireworksTowerAnimationController extends Component {
    private AnimationRenderComponent animator;

    /**
     * Creation call for a DroidAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("idleStart", this::animateDefault);
        entity.getEvents().addListener("chargeStart", this::animateChargeStart);
        entity.getEvents().addListener("chargeEnd", this::animateChargeEnd);
        entity.getEvents().addListener("attackStart", this::animateAttack);
        entity.getEvents().addListener("deathStart", this::animateDeath);

    }

    /**
     * the towers starts charging to shoot a projectile
     */
    void animateChargeStart() {
        animator.startAnimation("Charge");
    }

    /**
     * the towers starts charging to shoot a projectile and after that this
     * animation is the wind down of that charging up of the tower
     */
    void animateChargeEnd() {
        animator.startAnimation("Charge_end");
    }

    /**
     * THIS IS TO SHOW THE TOWER DEATH IN THIS SITUATION THE TOWER GETS BLASTED
     */
    void animateAttack() {
        animator.startAnimation("Attack");
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