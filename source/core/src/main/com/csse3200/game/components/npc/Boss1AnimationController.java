package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class Boss1AnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walk", this::animateWalk);
        entity.getEvents().addListener("death", this::animateDead);
        entity.getEvents().addListener("idle", this::animateIdle);
        entity.getEvents().addListener("attack1Start", this::animateAttack1);
    }


    void animateAttack1() {
        animator.startAnimation("Attack1");
    }
    void animateIdle() {
        animator.startAnimation("Idle");
    }

    void animateDead() {
        animator.startAnimation("Death");
    }

    void animateWalk() {
        animator.startAnimation("Walk");
    }
}
