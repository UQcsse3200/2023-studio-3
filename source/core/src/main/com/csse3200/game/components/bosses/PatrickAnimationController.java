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
        registerAnimationListener("Patrick_Attack");
        registerAnimationListener("Patrick_Cast");
        registerAnimationListener("Patrick_Death");
        registerAnimationListener("Patrick_Hurt");
        registerAnimationListener("Patrick_Idle");
        registerAnimationListener("Patrick_Spell");
        registerAnimationListener("Patrick_Walk");
    }

    private void registerAnimationListener(String animationName) {
        entity.getEvents().addListener(animationName, () ->
                animator.startAnimation(animationName));
    }
}