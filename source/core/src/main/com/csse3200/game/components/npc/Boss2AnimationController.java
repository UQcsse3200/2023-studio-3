package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class Boss2AnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkStart", this::animateWalk);
        entity.getEvents().addListener("deadStart", this::animateDead);
        entity.getEvents().addListener("idleStart", this::animateIdle);
        entity.getEvents().addListener("chargingStart", this::animateCharging);
        entity.getEvents().addListener("attack1Start", this::animateAttack1);
        entity.getEvents().addListener("attack2Start", this::animateAttack2);
        entity.getEvents().addListener("hurtStart", this::animateHurt);
    }

    void animateHurt() {
        animator.startAnimation("Hurt");
    }
    void animateAttack2() {
        animator.startAnimation("A2");
    }
    void animateAttack1() {
        animator.startAnimation("A1");
    }
    void animateCharging() {
        animator.startAnimation("Charging");
    }
    void animateIdle() {
        animator.startAnimation("Idle");
    }

    void animateDead() {
        animator.startAnimation("boss_death");
    }

    void animateWalk() {
        animator.startAnimation("Walk");
    }
}
