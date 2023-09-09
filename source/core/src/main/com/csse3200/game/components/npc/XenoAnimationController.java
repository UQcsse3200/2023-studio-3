package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class XenoAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderStart", this::animateWander);
        entity.getEvents().addListener("chaseStart", this::animateChase);
        entity.getEvents().addListener("meleeStart", this::animateMelee2);
        entity.getEvents().addListener("shootStart", this::animateShoot);
        entity.getEvents().addListener("dieStart", this::animateDie);
    }

    void animateWander() {
        animator.startAnimation("xeno_run");
    }

    void animateChase() {
        animator.startAnimation("xeno_run");
    }

    void animateShoot() {
        animator.startAnimation("xeno_shoot");
    }

    void animateMelee1() {
        animator.startAnimation("xeno_melee_1");
    }

    void animateMelee2() {
        animator.startAnimation("xeno_melee_2");
    }

    void animateDie() {
        animator.startAnimation("xeno_die");
    }
}
