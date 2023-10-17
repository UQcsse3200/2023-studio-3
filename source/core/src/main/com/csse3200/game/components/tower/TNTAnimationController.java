package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class listens to events relevant to TNTTower entity's state and plays the animation when one
 * of the events is triggered.
 */
public class TNTAnimationController extends Component {
    private AnimationRenderComponent animator;
    private static final String EXPLOSION_PATH = "sounds/towers/explosion.mp3";

    private final Sound explosion = ServiceLocator.getResourceService().getAsset(
            EXPLOSION_PATH, Sound.class);

    /**
     * Creation call for a TNTAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("digStart", this::animateDig);
        entity.getEvents().addListener("defaultStart", this::animateDefault);
        entity.getEvents().addListener("explodeStart",this::animateExplode);

    }

    /**
     * Triggers the "dig" animation for the entity.
     * This method should be invoked when the entity enters the digging state.
     */
    void animateDig() {
        animator.startAnimation("dig");
    }

    /**
     * Triggers the "default" animation for the entity.
     * This method should be invoked when the entity returns to its default state.
     */
    void animateDefault() { animator.startAnimation("default");}

    /**
     * Triggers the "explode" animation for the entity.
     * This method should be invoked when the entity enters the explosion state.
     */
    void animateExplode() { animator.startAnimation("explode");
        explosion.play();
    }

}
