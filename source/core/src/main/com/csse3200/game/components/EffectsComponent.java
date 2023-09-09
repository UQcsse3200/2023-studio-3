package com.csse3200.game.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.services.ServiceLocator;

import com.badlogic.gdx.utils.Timer;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class EffectsComponent extends Component {
    private final float radius;
    private final ProjectileEffects effect;
    private final boolean aoe;
    private HitboxComponent hitboxComponent;
    private final short targetLayer;
    private ArrayList<CombatStatsComponent> burnEntities = new ArrayList<>();

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

        // Get the other entity involved in the collision
        Entity otherEntity = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent otherCombatStats = otherEntity.getComponent(CombatStatsComponent.class);
        if (otherCombatStats == null) {
            // The other entity does not have a CombatStatsComponent
            return;
        }

        switch (effect) {
            case FIREBALL -> {
                if (aoe) {
                    applyAoeEffect(ProjectileEffects.FIREBALL);
                }
            }
            case BURN -> {
                if (aoe) {
                    applyAoeEffect(ProjectileEffects.BURN);
                } else {
                    applySingleEffect(ProjectileEffects.BURN, otherCombatStats);
                }
            }
            case SLOW -> {}
            case STUN -> {}
        }
    }

    /**
     * Used for singe targeting projectiles to apply effects entity it collides with.
     * @param effect effect to be applied to entity
     */
    public void applySingleEffect(ProjectileEffects effect, CombatStatsComponent targetCombatStats) {
        Entity hostEntity = getEntity();
        CombatStatsComponent hostCombatStats = hostEntity.getComponent(CombatStatsComponent.class);

        if (hostCombatStats == null) {
            // The host entity does not have a CombatStatsComponent to deal damage
            return;
        }

        switch (effect) {
            case FIREBALL -> {}
            case BURN -> {
                burnEffect(targetCombatStats, hostCombatStats);
            }
            case SLOW -> {}
            case STUN -> {}
        }
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

        for (int i = 0; i < nearbyEntities.size; i++) {
            Entity targetEntity = nearbyEntities.get(i);
            CombatStatsComponent targetCombatStats = targetEntity.getComponent(CombatStatsComponent.class);
            if (targetCombatStats != null) {
                switch (effect) {
                    case FIREBALL -> {
                        fireballEffect(targetCombatStats, hostCombatStats);
                    }
                    case BURN -> {
                        burnEffect(targetCombatStats, hostCombatStats);
                    }
                    case SLOW -> {}
                    case STUN -> {}
                }
            }
        }
    }

    private void fireballEffect(CombatStatsComponent target, CombatStatsComponent host) {
        target.hit(host);
    }

    private void burnEffect(CombatStatsComponent target, CombatStatsComponent host) {
        // Ensure burn effects aren't applied multiple times by same projectile
        if (burnEntities.contains(target)) {
            return;
        }
        burnEntities.add(target);

        // Create a timer task to apply the effect repeatedly
        int numberOfTicks = 5;
        long delay = 1;
        Timer.schedule(new Timer.Task() {
            private int count = 0;

            @Override
            public void run() {
                if (count < numberOfTicks) {
                    target.hit(host);
                    count++;
                } else {
                    // Ensure to cancel the task when it's done
                    this.cancel();
                }
            }
        }, delay, delay);
    }
}
