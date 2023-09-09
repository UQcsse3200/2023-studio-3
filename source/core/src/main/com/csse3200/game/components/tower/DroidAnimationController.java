package com.csse3200.game.components.tower;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to DroidTower entity's state and plays the animation when one
 * of the events is triggered.
 */
public class DroidAnimationController extends Component {
    private AnimationRenderComponent animator;

    /**
     * Creation call for a DroidAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkStart", this::animateWalk);
        entity.getEvents().addListener("idleStart", this::animateDefault);
        entity.getEvents().addListener("goUpStart",this::animateGoUp);
        entity.getEvents().addListener("goDownStart",this::animateGoDown);
        entity.getEvents().addListener("attackUpStart",this::animateAttackUp);
        entity.getEvents().addListener("attackDownStart",this::animateAttackDown);
        entity.getEvents().addListener("deathStart",this::animateDeath);

    }

    /**
     * Initiates the walking animation for the robot.
     * This method should be invoked when the robot is moving but not in combat.
     */
    void animateWalk() {
        animator.startAnimation("walk");
    }

    /**
     * Starts the animation sequence for switching aim from above.
     * Use this method when the robot is preparing to target mobs after aiming from below.
     */
    void animateGoUp() {
        animator.startAnimation("goUp");
    }

    /**
     * Activates the animation sequence for switching aim from below.
     * Use this method when the robot is preparing to target mobs after aiming from above.
     */
    void animateGoDown() {
        animator.startAnimation("goDown");
    }

    /**
     * Triggers the animation for firing projectiles from an elevated aim.
     * Invoke this method when the robot engages with mobs and aiming above.
     */
    void animateAttackUp() {
        animator.startAnimation("attackUp");
    }

    /**
     * Starts the animation sequence for firing projectiles from a lowered aim.
     * Use this method when the robot engages with mobs and aiming below.
     */
    void animateAttackDown() {
        animator.startAnimation("attackDown");
    }

    /**
     * Triggers the robot's death animation.
     * This method should be invoked when the robot's health reaches zero.
     */
    void animateDeath() {
        animator.startAnimation("death");
    }


    /**
     * Triggers the "default" animation for the entity.
     * This method should be invoked when the entity returns to its default state.
     */
    void animateDefault() { animator.startAnimation("idle");}

}
