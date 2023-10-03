package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to the SHip entity's state and plays the animation when one
 * of the events is triggered.
 */
public class BombShipAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("start", this::animateStart);
        entity.getEvents().addListener("destroy", this::animateDestroy);
        entity.getEvents().addListener("idle", this::animateIdle);
    }


    void animateIdle() {
        animator.startAnimation("ship_Idle");
    }

    void animateDestroy() {
        animator.startAnimation("ship_Destroy");
    }

    void animateStart() {
        animator.startAnimation("ship_Start");
    }
}
