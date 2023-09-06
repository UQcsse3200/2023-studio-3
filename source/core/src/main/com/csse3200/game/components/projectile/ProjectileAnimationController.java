package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class ProjectileAnimationController extends Component{
    /** Event name constants */
    private static final String START = "startProjectileStart1";
    private static final String PHASE1 = "startProjectile2";
    private static final String PHASE2 = "startProjectile3";
    private static final String PHASE3 = "startProjectile4";
    private static final String FINAL = "startProjectileFinish5";

    /** Animation name constants */
    private static final String START_ANIM = "projectileStart1";
    private static final String PHASE1_ANIM = "projectile2";
    private static final String PHASE2_ANIM = "projectile3";
    private static final String PHASE3_ANIM = "projectile4";
    private static final String FINAL_ANIM = "projectileFinish5";

    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(START, this::animateStart);
        entity.getEvents().addListener(START, this::animatePhase1);
        entity.getEvents().addListener(START, this::animatePhase2);
        entity.getEvents().addListener(START, this::animatePhase3);
        entity.getEvents().addListener(START, this::animateFinal);

    }

    void animateStart() {
        animator.startAnimation(START_ANIM);
    }

    void animatePhase1() {
        animator.startAnimation(PHASE1_ANIM);
    }

    void animatePhase2() {
        animator.startAnimation(PHASE2_ANIM);
    }

    void animatePhase3() {
        animator.startAnimation(PHASE3_ANIM);
    }

    void animateFinal() {
        animator.startAnimation(FINAL_ANIM);
    }
}
