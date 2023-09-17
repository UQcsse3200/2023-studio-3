package com.csse3200.game.components.bosses;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class DemonAnimationController extends Component {
    AnimationRenderComponent animator;

    /**
     * Creation call for a TowerAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
        registerAnimationListener("demon_walk");
        registerAnimationListener("demon_cleave");
        registerAnimationListener("demon_take_hit");
        registerAnimationListener("demon_idle");
        registerAnimationListener("demon_death");
    }

    private void registerAnimationListener(String animationName) {
        entity.getEvents().addListener(animationName, () -> animator.startAnimation(animationName));
    }
}
