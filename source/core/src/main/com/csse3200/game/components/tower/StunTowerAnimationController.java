package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Listens to triggers phrases and executes the required animations.
 */
public class StunTowerAnimationController extends Component {
    //Event name constants
    private static final String IDLE = "startIdle";
    private static final String ATTACK = "startAttack";
    //animation name constants
    private static final String IDLE_ANIM = "idle";
    private static final String ATTACK_ANIM = "attack";

    //further sounds can be added for the tower attacks/movement

    AnimationRenderComponent animator;

    /**
     * Creation method for StunTowerAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(IDLE, this::animateIdle);
        entity.getEvents().addListener(ATTACK, this::animateAttack);
    }

    /**
     * Starts the idle animation
     */
    void animateIdle() {
        animator.startAnimation(IDLE_ANIM);
    }

    /**
     * starts the attack animation
     */
    void animateAttack() {
        animator.startAnimation(ATTACK_ANIM);
    }
}
