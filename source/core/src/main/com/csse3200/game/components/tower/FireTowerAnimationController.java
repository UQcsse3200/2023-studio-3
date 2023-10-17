package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Listens for events relevant to a weapon tower state.
 * Each event will have a trigger phrase and have a certain animation attached.
 */
public class FireTowerAnimationController extends Component{
    //Event name constants
    private static final String IDLE = "startIdle";
    private static final String PREP_ATTACK = "startAttackPrep";
    private static final String ATTACK = "startAttack";
    private static final String DEATH = "startDeath";

    //animation name constants
    private static final String IDLE_ANIM = "idle";
    private static final String PREP_ATTACK_ANIM = "prepAttack";
    private static final String ATTACK_ANIM = "attack";
    private static final String DEATH_ANIM = "death";

    private static final String FIRE_SINGLE_SFX = "sounds/towers/Desert-Eagle-Far-Single-Gunshot.mp3";

    private final Sound fireSingleSound = ServiceLocator.getResourceService().getAsset(
            FIRE_SINGLE_SFX, Sound.class);

    AnimationRenderComponent animator;

    /**
     * Create method for FireTowerAnimationController.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(IDLE, this::animateIdle);
        entity.getEvents().addListener(PREP_ATTACK, this::animatePrepAttack);
        entity.getEvents().addListener(ATTACK, this::animateAttack);
        entity.getEvents().addListener(DEATH, this::animateDeath);
    }

    /**
     * Starts the idle animation.
     */
    void animateIdle() {
        animator.startAnimation(IDLE_ANIM);
    }

    /**
     * Starts the prep_attack animation.
     */
    void animatePrepAttack() {
        animator.startAnimation(PREP_ATTACK_ANIM);
    }

    /**
     * Starts the attack animation.
     */
    void animateAttack() {
        animator.startAnimation(ATTACK_ANIM);
        fireSingleSound.play();
    }

    void animateDeath() {
        animator.startAnimation(DEATH_ANIM);
    }
}
