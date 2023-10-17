package com.csse3200.game.components.tower;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.ProjectileEffects;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * This class listens to events relevant to DroidTower entity's state and plays the animation when one
 * of the events is triggered.
 */
public class DroidAnimationController extends Component {
    private AnimationRenderComponent animator;

    private static final String FIRE_SINGLE_SFX = "sounds/towers/5.56_single_shot.mp3";

    private final Sound fireSingleSound = ServiceLocator.getResourceService().getAsset(
            FIRE_SINGLE_SFX, Sound.class);

    /**
     * Creation call for a DroidAnimationController, fetches the animationRenderComponent that this controller will
     * be attached to and registers all the event listeners required to trigger the animations and sounds.
     */
    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkStart", this::animateWalk);
        entity.getEvents().addListener("idleStart", this::animateDefault);
        entity.getEvents().addListener("goUpStart",this::animateGoUp);
        entity.getEvents().addListener("goDownStart",this::animateGoDown);
        entity.getEvents().addListener("attackUpStart",this::animateAttackUp);
        entity.getEvents().addListener("attackDownStart",this::animateAttackDown);
        entity.getEvents().addListener("deathStart",this::animateDeath);
        entity.getEvents().addListener("ShootUp",this::shootUp);
        entity.getEvents().addListener("ShootDown",this::shootDown);

    }

    /**
     * Initiates the walking animation for the robot.
     * This method should be invoked when the robot is moving but not in combat.
     */
    void animateWalk() {
        animator.startAnimation("walk");
    }

    /**
     * Starts the animation sequence for switching aim from above.
     * Use this method when the robot is preparing to target mobs after aiming from below.
     */
    void animateGoUp() {
        animator.startAnimation("goUp");
    }

    /**
     * Activates the animation sequence for switching aim from below.
     * Use this method when the robot is preparing to target mobs after aiming from above.
     */
    void animateGoDown() {
        animator.startAnimation("goDown");
    }

    /**
     * Triggers the animation for firing projectiles from an elevated aim.
     * Invoke this method when the robot engages with mobs and aiming above.
     */
    void animateAttackUp() {
        animator.startAnimation("attackUp");
        fireSingleSound.play();
    }

    /**
     * Starts the animation sequence for firing projectiles from a lowered aim.
     * Use this method when the robot engages with mobs and aiming below.
     */
    void animateAttackDown() {
        animator.startAnimation("attackDown");
        fireSingleSound.play();
    }

    /**
     * Triggers the robot's death animation.
     * This method should be invoked when the robot's health reaches zero.
     */
    void animateDeath() {
        animator.startAnimation("death");
    }


    /**
     * Triggers the "default" animation for the entity.
     * This method should be invoked when the entity returns to its default state.
     */
    void animateDefault() { animator.startAnimation("idle");}


    /**
     * Fires a projectile upwards from the entity's current position.
     */
    void shootUp() {
        Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC, new Vector2(100,
                entity.getPosition().y), new Vector2(2,2), ProjectileEffects.SLOW, false);
        projectile.setScale(new Vector2(0.5f,0.5f));
        projectile.setPosition((float) (entity.getPosition().x + 0.2),
                (float) (entity.getPosition().y + 0.5));
        ServiceLocator.getEntityService().register(projectile);
    }

    /**
     * Fires a projectile downwards from the entity's current position.
     */
    void shootDown() {
        Entity projectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.NPC, new Vector2(100,
                entity.getPosition().y), new Vector2(2,2), ProjectileEffects.SLOW, false);
        projectile.setScale(new Vector2(0.5f,0.5f));
        projectile.setPosition((float) (entity.getPosition().x + 0.2),
                 (entity.getPosition().y));
        ServiceLocator.getEntityService().register(projectile);
    }

}
