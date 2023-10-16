package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import java.security.SecureRandom;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class XenoAnimationController extends Component {
    // // For on collision sounds later
    // private static final String COLLISION_SFX = "sounds/projectiles/on_collision.mp3";
    // Sound onCollisionSound = ServiceLocator.getResourceService().getAsset(
    //         COLLISION_SFX, Sound.class);
    AnimationRenderComponent animator;
    private SecureRandom rand = new SecureRandom();

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderStart", this::animateRun);
        entity.getEvents().addListener("runHurt", this::animateHurt);
        entity.getEvents().addListener("meleeStart", this::animateMelee);
        entity.getEvents().addListener("shootStart", this::animateShoot);
        entity.getEvents().addListener("dieStart", this::animateDie);
        entity.getEvents().addListener("stop", this::stopAnimation);
    }

    void animateRun() {
        animator.startAnimation("xeno_run");
    }

    void animateHurt() {
        animator.startAnimation("xeno_hurt");
    }

    void animateShoot() {
        animator.startAnimation("xeno_shoot");
    }

    void animateMelee() {
        int randAnim = rand.nextInt(2);
        if (randAnim == 1) {
            animator.startAnimation("xeno_melee_1");
        } else {
            animator.startAnimation("xeno_melee_2");
        }
    }

    void animateDie() {
        animator.startAnimation("xeno_die");
    }

    void stopAnimation() {
        animator.startAnimation("default");
    }
}
