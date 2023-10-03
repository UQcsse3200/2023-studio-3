package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.audio.Sound;

public class OnCollisionAnimationController extends Component {
    private static final String START = "startProjectile";
    private static final String START_ANIM = "projectile";
    private static final String COLLISION_SFX = "sounds/projectiles/explosion.mp3";
    private static final String PLAYSOUND = "collisionStart";
    Sound onCollisionSound = ServiceLocator.getResourceService().getAsset(
            COLLISION_SFX, Sound.class);
    AnimationRenderComponent animator;
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener(PLAYSOUND, this::animateCollision);

        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(START, this::animateStart);
    }
    void animateStart() {
        animator.startAnimation(START_ANIM);
    }
    void animateCollision() {
        onCollisionSound.play();
    }
}
