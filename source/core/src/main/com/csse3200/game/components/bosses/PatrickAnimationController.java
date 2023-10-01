package com.csse3200.game.components.bosses;

import com.badlogic.gdx.audio.Sound;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

public class PatrickAnimationController extends Component {
    AnimationRenderComponent animator;
    Sound patrickAttack = ServiceLocator.getResourceService().getAsset(
            "sounds/mobBoss/patrickAttack.wav", Sound.class);

    /**
     * Creation call for a DemonAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("patrick_attack", this::patrickAttack);
        entity.getEvents().addListener("patrick_cast", this::patrickCast);
        entity.getEvents().addListener("patrick_death", this::patrickDeath);
        entity.getEvents().addListener("patrick_hurt", this::patrickHurt);
        entity.getEvents().addListener("patrick_idle", this::patrickIdle);
        entity.getEvents().addListener("patrick_spell", this::patrickSpell);
        entity.getEvents().addListener("patrick_walk", this::patrickWalk);
    }

    private void patrickAttack() {
        animator.startAnimation("patrick_attack");
    }
    private void patrickCast() {
        animator.startAnimation("patrick_cast");
    }
    private void patrickDeath() {
        animator.startAnimation("patrick_death");
    }
    private void patrickHurt() {
        animator.startAnimation("patrick_hurt");
    }
    private void patrickIdle() {
        animator.startAnimation("patrick_idle");
    }
    private void patrickSpell() {
        animator.startAnimation("patrick_spell");
    }
    private void patrickWalk() {
        animator.startAnimation("patrick_walk");
    }
}