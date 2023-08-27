package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class BossAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkStart", this::animateWalk);
        entity.getEvents().addListener("deadStart", this::animateDead);
        entity.getEvents().addListener("idleStart", this::animateIdle);
        entity.getEvents().addListener("shutdownStart", this::animateShutdown);
        entity.getEvents().addListener("attack", this::animateAttack);
        entity.getEvents().addListener("attack2Start", this::animateAttack2);
        entity.getEvents().addListener("enablingStart", this::animateEnabling);
        entity.getEvents().addListener("hurtStart", this::animateHurt);
    }

    void animateHurt() {
        animator.startAnimation("Hurt");
    }
    void animateEnabling() {
        animator.startAnimation("Enabling");
    }
    void animateAttack2() {
        animator.startAnimation("Attack2");
    }
    void animateAttack() {
        animator.startAnimation("Attack");
    }
    void animateShutdown() {
        animator.startAnimation("Shutdown");
    }
    void animateIdle() {
        animator.startAnimation("Idle");
    }

    void animateDead() {
        animator.startAnimation("Dead");
    }

    void animateWalk() {
        animator.startAnimation("Walk");
    }
}
