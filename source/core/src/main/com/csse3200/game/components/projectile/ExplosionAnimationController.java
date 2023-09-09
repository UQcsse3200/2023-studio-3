package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.audio.Sound;


//can be used for aoe
public class ExplosionAnimationController extends Component {
    /** Event name constants */
    private static final String START = "startExplosion";
    private static final String FINAL = "startExplosionFinal";

    /** Animation name constants */
    private static final String START_ANIM = "explosion";
    private static final String FINAL_ANIM = "explosionFinal";
    /** Sound effects constant */
    private static final String FINAL_SFX = "sounds/projectiles/explosion.mp3";

    AnimationRenderComponent animator;

    Sound explosionSound = ServiceLocator.getResourceService().getAsset(
            FINAL_SFX, Sound.class);

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(START, this::animateStart);
        entity.getEvents().addListener(FINAL, this::animateFinal);

    }

    void animateStart() {
        animator.startAnimation(START_ANIM);
        explosionSound.play();
    }

    void animateFinal() {
        animator.startAnimation(FINAL_ANIM);
    }

}
