package com.csse3200.game.components.tower;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * Listens for events relevant to a weapon tower state.
 * Each event will have a trigger phrase and have a certain animation attached.
 */
public class WallTowerAnimationController extends Component{
    //Event name constants
    private static final String DEATH = "startDeath";
    private static final String IDLE = "startIdle";

    //animation name constants
    private static final String DEATH_ANIM = "Death";
    private static final String IDLE_ANIM = "Idle";
    //here we can add the sounds for the implemented animations

    AnimationRenderComponent animator;

    /**
     * Create method for FireTowerAnimationController.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(DEATH, this::animateDeath);
        entity.getEvents().addListener(IDLE, this::animateIdle);
    }

    /**
     * Starts the idle animation.
     */
    void animateDeath() {
        animator.startAnimation(DEATH_ANIM);
    }

    void animateIdle(){
        animator.startAnimation(IDLE_ANIM);
    }
}
