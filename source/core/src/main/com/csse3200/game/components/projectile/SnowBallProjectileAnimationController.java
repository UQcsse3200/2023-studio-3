package com.csse3200.game.components.projectile;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class SnowBallProjectileAnimationController extends Component{
    private static final String START = "startProjectile";
    private static final String FINAL = "startProjectileFinal";

    /** Animation name constants */
    private static final String START_ANIM = "projectile";
    private static final String FINAL_ANIM = "projectileFinal";
    AnimationRenderComponent animator;
    private static final String SNOW_SFX = "sounds/projectiles/Snowball_throw_sound.mp3";
    Sound snowSound = ServiceLocator.getResourceService().getAsset(
            SNOW_SFX, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);

        entity.getEvents().addListener(START, this::animateStart);
        entity.getEvents().addListener(FINAL, this::animateFinal);

    }

    void animateStart() {
        animator.startAnimation(START_ANIM);
        snowSound.play();
    }

    void animateFinal() {
        animator.startAnimation(FINAL_ANIM);

    }
}
