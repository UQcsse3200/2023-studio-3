package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator; //used for sound

public class EngineerBullets extends Component{
    /** Event name constants */

    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("start", this::animateStart);
        entity.getEvents().addListener("final", this::animateFinal);

    }

    void animateStart() {
        animator.startAnimation("bullet");
    }

    void animateFinal() {
        animator.startAnimation("bulletFinal");
    }
}

