package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator; //used for sound
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.components.npc.DeflectingComponent;

public class EngineerBulletsAnimationController extends Component{
    /** Event name constants */

    AnimationRenderComponent animator;
	
	private HitboxComponent hitboxComponent;
	short targetLayer;
	
	public EngineerBulletsAnimationController(short targetLayer)
	{
		this.targetLayer = targetLayer;
	}

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("startProjectile", this::animateStart);
        entity.getEvents().addListener("startProjectileFinal", this::animateFinal);
		entity.getEvents().addListener("collisionStart", this::animateCollide);
		hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    void animateStart() {
        animator.startAnimation("bullet");
    }

    void animateFinal() {
        animator.startAnimation("bulletFinal");
    }
	
	void animateCollide(Fixture me, Fixture other){
		if(me!=null)
		{
			if (hitboxComponent==null || hitboxComponent.getFixture() != me) {
			  // Not triggered by hitbox, ignore
			  return;
			}
		}else
		{
			animator.startAnimation("bulletCollide");
			return;
		}
			if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
			  // Doesn't match our target layer, ignore
			  return;
			}
			Component deflectComponent = ((BodyUserData) other.getBody().getUserData()).entity.getComponent(DeflectingComponent.class);
			if (deflectComponent != null && deflectComponent.enabled)
				return;
		animator.startAnimation("bulletCollide");
	}
}

