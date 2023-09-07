package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

import com.badlogic.gdx.utils.Array;

public class EffectsComponent extends Component {
    private final float radius;
    private final ProjectileFactory.ProjectileEffects effect;
    private final boolean aoe;
    private HitboxComponent hitboxComponent;
    private final short targetLayer;

    /**
     * Constructor for the AoEComponent.
     *
     * @param radius The radius of the area-of-effect.
     */
    public EffectsComponent(short targetLayer, float radius, ProjectileFactory.ProjectileEffects effect, boolean aoe) {
        this.targetLayer = targetLayer;
        this.radius = radius;
        this.effect = effect;
        this.aoe = aoe;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("projectileCollisionStart", this::onCollisionStart);
        entity.getEvents().addListener("projectileCollisionEnd", this::onCollisionEnd);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        // Nothing to do on collision start
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

        switch (effect) {
            case FIREBALL -> {
                if (aoe) {
                    applyAoeDamage();
                }
            }
            case BURN -> {}
            case SLOW -> {}
            case STUN -> {}
        }
    }
    /**
     * Apply damage to all entities within the area of effect (radius).
     */
    public void applyAoeDamage() {
        Entity hostEntity = getEntity();
        CombatStatsComponent hostCombatStats = hostEntity.getComponent(CombatStatsComponent.class);

        if (hostCombatStats == null) {
            // The host entity does not have a CombatStatsComponent to deal damage
            return;
        }

        Array<Entity> nearbyEntities = ServiceLocator.getEntityService().getNearbyEntities(hostEntity, radius);

        for (Entity targetEntity : nearbyEntities) {
            CombatStatsComponent targetCombatStats = targetEntity.getComponent(CombatStatsComponent.class);
            if (targetCombatStats != null) {
                targetCombatStats.hit(hostCombatStats);
            }
        }
    }
}
