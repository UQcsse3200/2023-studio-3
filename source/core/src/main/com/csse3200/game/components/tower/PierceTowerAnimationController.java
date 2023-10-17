package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Listens to triggers phrases and executes the required animations.
 */
public class PierceTowerAnimationController extends Component {
    //Event name constants
    private static final String IDLE = "startIdle";
    private static final String ATTACK = "startAttack";
    private static final String DEATH = "startDeath";
    private static final String ALERT = "startAlert";
    //animation name constants
    private static final String IDLE_ANIM = "Idle";
    private static final String ATTACK_ANIM = "Attack";
    private static final String DEATH_ANIM = "Death";
    private static final String ALERT_ANIM = "Warning";

    //further sounds can be added for the tower attacks/movement
    private static final String FIRE_SINGLE_SFX = "sounds/towers/5.56_single_shot.mp3";

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
        entity .getEvents().addListener(ALERT,this::animateAlert);
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

    /**
     * starts the alert animation when enemy in range
     */
    void animateAlert() {
        animator.startAnimation(ALERT_ANIM);
    }
}
