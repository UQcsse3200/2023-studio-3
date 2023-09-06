package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Listens for events relevant to a weapon tower state.
 * Each event will trigger a certain animation
 */
public class EconTowerAnimationController extends Component {
    // Event name constants
    private static final String IDLE = "idleStart";
    // Animation name constants
    private static final String IDLE_ANIM = "move1";

    AnimationRenderComponent animator;

    /**
     * Creation call for a TowerAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(IDLE, this::animateIdle);
    }

    /**
     * Starts the
     */
    void animateIdle() {
        animator.startAnimation(IDLE_ANIM);
    }

}