package com.csse3200.game.components.bosses;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class PatrickAnimationController extends Component {
    AnimationRenderComponent animator;

    /**
     * Creation call for a DemonAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
        registerAnimationListener("patrick_attack");
        registerAnimationListener("patrick_cast");
        registerAnimationListener("patrick_death");
        registerAnimationListener("patrick_hurt");
        registerAnimationListener("patrick_idle");
        registerAnimationListener("patrick_spell");
        registerAnimationListener("patrick_walk");
    }

    private void registerAnimationListener(String animationName) {
        entity.getEvents().addListener(animationName, () ->
                animator.startAnimation(animationName));
    }
}