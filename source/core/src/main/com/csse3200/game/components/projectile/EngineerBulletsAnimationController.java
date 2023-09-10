package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator; //used for sound

public class EngineerBulletsAnimationController extends Component{
    /** Event name constants */

    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("startProjectile", this::animateStart);
        entity.getEvents().addListener("startProjectileFinal", this::animateFinal);

    }

    void animateStart() {
        animator.startAnimation("bullet");
    }

    void animateFinal() {
        animator.startAnimation("bulletFinal");
    }
}

