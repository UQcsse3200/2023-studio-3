package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;

public class StunEffectProjectileAnimationController extends Component {
    /** Event name constants */
    private static final String START = "startProjectile";

    /** Animation name constants */
    private static final String START_ANIM = "projectile";
    AnimationRenderComponent animator;

	private HitboxComponent hitboxComponent;
	short targetLayer;
	
	public StunEffectProjectileAnimationController(short targetLayer)
	{
		this.targetLayer = targetLayer;
	}

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener(START, this::animateStart);
		entity.getEvents().addListener("collisionStart", this::animateCollide);

		hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    void animateStart() {
        animator.startAnimation(START_ANIM);
    }
	
	void animateCollide(Fixture me, Fixture other)
	{
		if (hitboxComponent.getFixture() != me) {
		  // Not triggered by hitbox, ignore
		  return;
		}
		if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
		  // Doesn't match our target layer, ignore
		  return;
		}

		animator.startAnimation("stun");
	}
}
