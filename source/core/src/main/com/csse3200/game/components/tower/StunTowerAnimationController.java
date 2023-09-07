package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class StunTowerAnimationController extends Component {
    //Event name constants
    private static final String IDLE = "startIdle";
    private static final String ATTACK = "startAttack";
    //animation name constants
    private static final String IDLE_ANIM = "idle";
    private static final String ATTACK_ANIM = "attack";

    //further sounds can be added for the tower attacks/movement

    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(IDLE, this::animateIdle);
        entity.getEvents().addListener(ATTACK, this::animateAttack);
    }

    void animateIdle() {
        animator.startAnimation(IDLE_ANIM);
    }

    void animateAttack() {
        animator.startAnimation(ATTACK_ANIM);
    }
}
