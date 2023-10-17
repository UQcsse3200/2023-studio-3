package com.csse3200.game.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.tower.TowerUpgraderComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.ServiceLocator;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

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
    private Array<CombatStatsComponent> burnEntities = new Array<>();
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
        if (effect == ProjectileEffects.FIREBALL) {
            if (aoe) {
                applyAoeEffect(ProjectileEffects.FIREBALL);
            }
        } else {
            if (aoe) {
                applyAoeEffect(effect);
            } else {
                applySingleEffect(effect, otherCombatStats, otherEntity);
            }
        }
    }

    /**
     * Used for singe targeting projectiles to apply effects entity it collides with.
     * @param effect effect to be applied to entity
     */
    public void applySingleEffect(ProjectileEffects effect, CombatStatsComponent targetCombatStats, Entity targetEntity) {
        Entity hostEntity = getEntity();
        CombatStatsComponent hostCombatStats = hostEntity.getComponent(CombatStatsComponent.class);

        if (hostCombatStats == null) {
            // The host entity does not have a CombatStatsComponent to deal damage
            return;
        }

        // Apply effect
        switch (effect) {
            case FIREBALL -> {}
            case BURN -> {
                burnEffect(targetCombatStats, hostCombatStats);
            }
            case SLOW -> {slowEffect(targetEntity);}
            case STUN -> {stunEffect(targetEntity);}
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

        // Iterate through nearby entities and apply effects
        for (int i = 0; i < nearbyEntities.size; i++) {
            Entity targetEntity = nearbyEntities.get(i);

            HitboxComponent targetHitbox = targetEntity.getComponent(HitboxComponent.class);
            if (targetHitbox == null) { return; }
            if (!PhysicsLayer.contains(targetLayer, targetHitbox.getLayer())) {
                // Doesn't match our target layer, ignore
                return;
            }

            CombatStatsComponent targetCombatStats = targetEntity.getComponent(CombatStatsComponent.class);
            if (targetCombatStats != null) {
                switch (effect) {
                    case FIREBALL -> {fireballEffect(targetCombatStats, hostCombatStats);}
                    case BURN -> {burnEffect(targetCombatStats, hostCombatStats);}
                    case SLOW -> {slowEffect(targetEntity);}
                    case STUN -> {
                        stunEffect(targetEntity);
                    }
                }
            } else {
                return;
            }
        }
    }

    /**
     * Deals damage to target based on hosts' CombatStatsComponent
     * @param target CombatStatsComponent of entity hit by projectile
     * @param host CombatStatsComponent of projectile
     */
    private void fireballEffect(CombatStatsComponent target, CombatStatsComponent host) {
        target.hit(host);
    }

    /**
     * Applies 5 ticks of damage from hosts' CombatStatsComponent over 5 seconds
     * @param target CombatStatsComponent of entity hit by projectile
     * @param host CombatStatsComponent of projectile
     */
    private void burnEffect(CombatStatsComponent target, CombatStatsComponent host) {
        // Ensure burn effects aren't applied multiple times by same projectile
        if (burnEntities.contains(target, false)) {
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

    /**
     * Applies slow effect to targetEntity. If entity is a mob, speed
     * and firing rate will be slowed. If entity is a tower, firing rate
     * will be slowed
     * @param targetEntity Entity for slow effect to be applied to
     */
    private void slowEffect(Entity targetEntity) {
        boolean towerFlag = false;
        boolean mobFlag = false;

        PhysicsMovementComponent targetPhysics = null;
        float xSpeed = 0;
        float ySpeed = 0;

        // Create a timer task to apply the effect repeatedly
        if (PhysicsLayer.contains(PhysicsLayer.HUMANS, targetEntity.getComponent(HitboxComponent.class).getLayer())) {
            // towers
            towerFlag = true;
            targetEntity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.FIRERATE, -30);
        } else if (PhysicsLayer.contains(PhysicsLayer.NPC, targetEntity.getComponent(HitboxComponent.class).getLayer())) {
            // mobs
            mobFlag = true;
            targetPhysics = targetEntity.getComponent(PhysicsMovementComponent.class);
            if (targetPhysics == null) {
                return;
            }

            // Halve the mob speed
            xSpeed = targetPhysics.getSpeed().x;
            ySpeed = targetPhysics.getSpeed().y;
            targetPhysics.setSpeed(new Vector2(xSpeed/2, ySpeed/2));
        } else {
            return;
        }

        // Reset speed
        boolean finalTowerFlag = towerFlag;
        boolean finalMobFlag = mobFlag;
        PhysicsMovementComponent finalTargetPhysics = targetPhysics;
        float finalXSpeed = xSpeed;
        float finalYSpeed = ySpeed;
        Timer.schedule(new Task() {
            @Override
            public void run() {
                if (finalTowerFlag) {
                    targetEntity.getEvents().trigger("upgradeTower", TowerUpgraderComponent.UPGRADE.FIRERATE, 30);
                } else if (finalMobFlag) {
                    finalTargetPhysics.setSpeed(new Vector2(finalXSpeed, finalYSpeed));
                }
            }
        }, 5); // 5 seconds delay
    }

    /**
     * Applies stun effect to a taget entity.
     * @param targetEntity Entity for stun effect to be applied to.
     */
    private void stunEffect(Entity targetEntity) {
        CombatStatsComponent hostCombatStats = targetEntity.getComponent(CombatStatsComponent.class);
        AITaskComponent taskComponent = targetEntity.getComponent(AITaskComponent.class);

        if (hostCombatStats == null || taskComponent == null) {
            return;
        }

        hostCombatStats.setBaseAttack(0);

        if (stunnedEntities.contains(targetEntity)) {
            return;
        }
        
        taskComponent.disposeAll();
        stunnedEntities.add(targetEntity);
    
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                taskComponent.restore();
                for (int i = 0; i < stunnedEntities.size(); i++) {
                    if (stunnedEntities.get(i).equals(targetEntity)) {
                        stunnedEntities.remove(stunnedEntities.get(i));
                    }
                }
                this.cancel();
            }
        }, 5000);
    }
}
