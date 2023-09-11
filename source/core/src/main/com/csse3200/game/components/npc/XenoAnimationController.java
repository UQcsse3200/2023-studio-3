package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.util.Objects;

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
        entity.getEvents().addListener("wanderStart", this::animateRun);
        entity.getEvents().addListener("runHurt", this::animateHurt);
        entity.getEvents().addListener("meleeStart", this::animateMelee1);
        entity.getEvents().addListener("meleeStart", this::animateMelee2);
        entity.getEvents().addListener("shootStart", this::animateShoot);
        entity.getEvents().addListener("dieStart", this::animateDie);
        entity.getEvents().addListener("stop", this::stopAnimation);
    }

    void animateRun() {
        animator.stopAnimation();
        animator.startAnimation("xeno_run");
    }

    void animateHurt() {
        animator.stopAnimation();
        animator.startAnimation("xeno_hurt");
    }

    void animateShoot() {
        animator.stopAnimation();
        animator.startAnimation("xeno_shoot");
    }

    void animateMelee1() {
        animator.stopAnimation();
        animator.startAnimation("xeno_melee_1");
    }

    void animateMelee2() {
        animator.stopAnimation();
        animator.startAnimation("xeno_melee_2");
    }

    void animateDie() {
        animator.stopAnimation();
        animator.startAnimation("xeno_die");
    }

    void stopAnimation() {
        animator.stopAnimation();
        animator.startAnimation("default");
    }
}
