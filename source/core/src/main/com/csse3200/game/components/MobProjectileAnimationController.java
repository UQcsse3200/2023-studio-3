package com.csse3200.game.components;

import com.csse3200.game.rendering.AnimationRenderComponent;


public class MobProjectileAnimationController extends Component {
    AnimationRenderComponent animator;

    /**
     * Creation call for a TowerAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("rotate", this::animateStartRotate);
    }

    void animateStartRotate() {
        animator.startAnimation("rotate");
    }
}
