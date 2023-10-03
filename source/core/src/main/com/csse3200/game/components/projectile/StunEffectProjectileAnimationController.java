package com.csse3200.game.components.projectile;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class StunEffectProjectileAnimationController extends Component {
    /** Event name constants */
    private static final String START = "startProjectile";

    /** Animation name constants */
    private static final String START_ANIM = "projectile";
    AnimationRenderComponent animator;
    private static final String STUN_SFX = "sounds/projectiles/projectile firing.mp3";
    Sound stunsound = ServiceLocator.getResourceService().getAsset(
            STUN_SFX, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(START, this::animateStart);

    }

    void animateStart() {
        animator.startAnimation(START_ANIM);
      stunsound.play();
    }

}
