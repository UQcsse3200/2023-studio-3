package com.csse3200.game.components.tower;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.physics.BodyUserData;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.components.HitboxComponent;
import com.csse3200.game.physics.components.PhysicsComponent;
import com.csse3200.game.physics.components.PhysicsMovementComponent;
import com.csse3200.game.services.ServiceLocator;

/**
 * Class responsible for applying damage and knock-back to nearby entities when triggered.
 * Utilizes HitboxComponent and CombatStatsComponent for functionality.
 */
public class TNTDamageComponent extends Component {
    private short targetLayer;
    private float knockbackForce = 0f;
    private float radius;
    private CombatStatsComponent combatStats;
    private HitboxComponent hitboxComponent;

    /**
     * Default constructor for creating a component without knockback.
     *
     * @param targetLayer The physics layer of the target entities' collider.
     */
    public TNTDamageComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    /**
     * Overloaded constructor for creating a component with knockback and radius.
     *
     * @param targetLayer The physics layer of the target entities' collider.
     * @param knockback The force of the knockback.
     * @param radius The radius within which entities will be affected.
     */
    public TNTDamageComponent(short targetLayer, float knockback, float radius) {
        this.targetLayer = targetLayer;
        this.knockbackForce = knockback;
        this.radius = radius;
    }

    /**
     * Initializes the component and registers event listeners.
     */
    @Override
    public void create() {
        entity.getEvents().addListener("TNTDamageStart", this::applyTNTDamage);
        combatStats = entity.getComponent(CombatStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    /**
     * Scans all nearby entities within a given radius and applies damage and knockback to them.
     */
    private void applyTNTDamage() {
        // Fetch nearby entities
        Array<Entity> allEntities = ServiceLocator.getEntityService().getEntities();

        for (int i = 0; i < allEntities.size; i++) {
            Entity otherEntity = allEntities.get(i);

            Vector2 positionSource = entity.getPosition();
            Vector2 positionOther = otherEntity.getPosition();

            if (positionSource.dst(positionOther) <= radius && positionSource.y -1 <= positionOther.y && positionSource.y +1 >= positionOther.y) {
                HitboxComponent sourceHitbox = entity.getComponent(HitboxComponent.class);
                HitboxComponent otherHitbox = otherEntity.getComponent(HitboxComponent.class);

                // Check for null components and log specifics
                if (sourceHitbox == null || otherHitbox == null || entity == otherEntity) {

                    continue;
                }

                applyDamage(sourceHitbox.getFixture(), otherHitbox.getFixture());
            }
        }
    }


    /**
     * Applies damage and knockback to a specific entity based on their physics fixtures.
     *
     * @param me The fixture representing this entity.
     * @param other The fixture representing the target entity.
     */
    private void applyDamage(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to attack target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);
        if (targetStats != null) {
            targetStats.hit(combatStats);
        }

        // apply knock-back
        PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);
        PhysicsMovementComponent movementComponent = target.getComponent(PhysicsMovementComponent.class);

        if (physicsComponent != null && knockbackForce > 0f) {
            // Disable regular movement temporarily
            if (movementComponent != null) {
                movementComponent.applyKnockback(0.5f);  // Disable movement for 0.5 seconds
            }

            Body targetBody = physicsComponent.getBody();
            Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition()).nor();
            direction.y = 0;
            Vector2 impulse = direction.scl(knockbackForce);
            targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
        }
    }
}