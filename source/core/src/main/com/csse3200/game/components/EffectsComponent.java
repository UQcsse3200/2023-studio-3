package com.csse3200.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

import com.badlogic.gdx.utils.Array;

/**
 * This component applies an effect from the ProjectileEffects enum. This consists of fireball, burn,
 * slow, and stun. Component also handles the targeting of specific layers and an area of effect
 * application of effects.
 */
public class EffectsComponent extends Component {
    private final float radius;
    private final ProjectileEffects effect;
    private final boolean aoe;
    private HitboxComponent hitboxComponent;
    private final short targetLayer;
    private ArrayList<Entity> stunnedEntities = new ArrayList<>();

    /**
     * Constructor for the AoEComponent.
     *
     * @param radius The radius of the area-of-effect.
     */
    public EffectsComponent(short targetLayer, float radius, ProjectileEffects effect, boolean aoe) {
        this.targetLayer = targetLayer;
        this.radius = radius;
        this.effect = effect;
        this.aoe = aoe;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        // Nothing to do in collision start
    }

    private void onCollisionEnd(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Get the other entity involved in the collision
        Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent otherCombatStats = otherEntity.getComponent(CombatStatsComponent.class);
        if (otherCombatStats == null) {
            // The other entity does not have a CombatStatsComponent
            return;
        }

        // Apply effect
        if (aoe) {
            applyAoeEffect(effect);
        } else {
            applySingleEffect(effect, otherEntity);
        }
    }

    /**
     * Used for singe targeting projectiles to apply effects entity it collides with.
     * @param effect effect to be applied to entity
     */
    public void applySingleEffect(ProjectileEffects effect, Entity targetEntity) {
        Entity hostEntity = getEntity();
        CombatStatsComponent hostCombatStats = hostEntity.getComponent(CombatStatsComponent.class);

        if (hostCombatStats == null) {
            // The host entity does not have a CombatStatsComponent to deal damage
            return;
        }

        // apply effect
        EffectComponent effectComponent = targetEntity.getComponent(EffectComponent.class);
        if (effectComponent == null) {
            return;
        }
        effectComponent.applyEffect(effect, hostEntity, targetEntity);
    }

    /**
     * Used for aoe projectiles to apply effects to all entities within the area of effect (radius).
     * @param effect effect to be applied to entities within radius
     */
    public void applyAoeEffect(ProjectileEffects effect) {
        Entity hostEntity = getEntity();
        CombatStatsComponent hostCombatStats = hostEntity.getComponent(CombatStatsComponent.class);

        if (hostCombatStats == null) {
            // The host entity does not have a CombatStatsComponent to deal damage
            return;
        }

        Array<Entity> nearbyEntities = ServiceLocator.getEntityService().getNearbyEntities(hostEntity, radius);

        // Iterate through nearby entities and apply effects
        for (int i = 0; i < nearbyEntities.size; i++) {
            Entity targetEntity = nearbyEntities.get(i);

            HitboxComponent targetHitbox = targetEntity.getComponent(HitboxComponent.class);
            if (targetHitbox == null) { return; }
            if (!PhysicsLayer.contains(targetLayer, targetHitbox.getLayer())) {
                // Doesn't match our target layer, ignore
                return;
            }

            // apply effect
            EffectComponent effectComponent = targetEntity.getComponent(EffectComponent.class);
            if (effectComponent == null) {
                return;
            }
            effectComponent.applyEffect(effect, hostEntity, targetEntity);
        }
    }
}