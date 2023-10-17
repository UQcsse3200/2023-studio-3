package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class FirewizardAnimationController extends Component {

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
        animator.startAnimation("firewizard_move");
    }

    void animateAttack() {
        animator.startAnimation("firewizard_attack");
    }

    void animateDeath() {
        animator.startAnimation("firewizard_death");
    }
}
