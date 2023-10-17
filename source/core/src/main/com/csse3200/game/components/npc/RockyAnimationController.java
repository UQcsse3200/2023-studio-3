package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.security.SecureRandom;

public class RockyAnimationController extends Component {
    AnimationRenderComponent animator;
    private SecureRandom rand = new SecureRandom();


    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("mob_walk", this::animateWalk);
        entity.getEvents().addListener("mob_attack", this::animateAttack);
        entity.getEvents().addListener("mob_death", this::animateDeath);


    }

    void animateWalk() {
        animator.startAnimation("rocky_move");
    }

    void animateAttack() {
        animator.startAnimation("rocky_attack");
    }

    void animateDeath() {
        animator.startAnimation("rocky_death");
    }

}
