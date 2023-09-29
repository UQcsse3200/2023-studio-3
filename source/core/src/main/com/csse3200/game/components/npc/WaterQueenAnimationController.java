package com.csse3200.game.components.npc;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

import java.util.Random;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class WaterQueenAnimationController extends Component {
    // // For on collision sounds later
    // private static final String COLLISION_SFX = "sounds/projectiles/on_collision.mp3";
    // Sound onCollisionSound = ServiceLocator.getResourceService().getAsset(
    //         COLLISION_SFX, Sound.class);
    AnimationRenderComponent animator;
    Random rand = new Random();

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderStart", this::animateWalk);
        entity.getEvents().addListener("shootStart", this::animateAttack);
        entity.getEvents().addListener("dieStart", this::animateDeath);
    }

    void animateWalk() {
        animator.startAnimation("water_queen_walk");
    }

    void animateAttack() {
        animator.startAnimation("water_queen_attack");
    }

    void animateDeath() {
        animator.startAnimation("water_queen_death");
    }
}

