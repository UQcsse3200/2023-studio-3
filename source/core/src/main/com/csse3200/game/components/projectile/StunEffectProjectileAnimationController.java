package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class StunEffectProjectileAnimationController extends Component {
    /** Event name constants */
    private static final String START = "startProjectile";

    /** Animation name constants */
    private static final String START_ANIM = "projectile";
    AnimationRenderComponent animator;


    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(START, this::animateStart);

    }

    void animateStart() {
        animator.startAnimation(START_ANIM);
    }

}
