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
    private static final String IDLE = "idleStartEco";
    private static final String MOVE = "moveStartEco";
    // Animation name constants
    private static final String ECO_MOVE = "move1";
    private static final String ECO_IDLE = "idle";

    private static final String PING_PATH = "sounds/towers/eco_tower_ping.mp3";

    private final Sound ping = ServiceLocator.getResourceService().getAsset(
            PING_PATH, Sound.class);

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
        entity.getEvents().addListener(MOVE, this::animateMove);
    }

    /**
     * Starts the
     */
    void animateIdle() {
        animator.startAnimation(ECO_IDLE);
    }

    void animateMove() { animator.startAnimation(ECO_MOVE);
    ping.play();}


}