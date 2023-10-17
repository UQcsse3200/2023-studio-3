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
    private static final String DEATH = "startDeath";
    //animation name constants
    private static final String IDLE_ANIM = "idle";
    private static final String ATTACK_ANIM = "attack";
    private static final String DEATH_ANIM = "death";

    private static final String FIRE_SINGLE_SFX = "sounds/towers/ar15_single_shot_far.mp3";

    private final Sound fireSingleSound = ServiceLocator.getResourceService().getAsset(
            FIRE_SINGLE_SFX, Sound.class);

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
        entity.getEvents().addListener(DEATH, this::animateDeath);
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
        fireSingleSound.play();
    }

    /**
     * starts the death animation
     */
    void animateDeath() {
        animator.startAnimation(DEATH_ANIM);
    }
}
