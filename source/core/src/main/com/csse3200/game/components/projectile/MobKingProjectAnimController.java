package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

public class MobKingProjectAnimController extends Component {
    /** Event names */
    private static final String START = "startMobKing";
    private static final String FINAL = "startMobKingFinal";

    /** Animation name constants */
    private static final String START_ANIM = "mob_boss";
    private static final String FINAL_ANIM = "mob_bossFinal";

    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(START, this::animateStart);
        entity.getEvents().addListener(FINAL, this::animateFinal);
    }

    void animateStart() {
        animator.startAnimation(START_ANIM);
    }

    void animateFinal() {
        animator.startAnimation(FINAL_ANIM);
    }
}
