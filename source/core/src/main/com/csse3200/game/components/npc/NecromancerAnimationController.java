package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class NecromancerAnimationController extends Component {

    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("mob_walk", this::animateWalk);
        entity.getEvents().addListener("mob_attack", this::animateAttack);
        entity.getEvents().addListener("mob_death", this::animateDeath);


    }

    void animateWalk() {
        animator.startAnimation("necromancer_walk");
    }

    void animateAttack() {
        animator.startAnimation("necromancer_attack");
    }

    void animateDeath() {
        animator.startAnimation("necromancer_death");
    }
}
